import os
import sys
import json
import hashlib
import logging
import argparse
from datetime import datetime, timedelta
from typing import List, Dict, Tuple, Optional
from decimal import Decimal
import asyncio

import pandas as pd
import numpy as np
import psycopg2
from psycopg2.extras import execute_batch
import yfinance as yf
import ccxt
from dataclasses import dataclass
from tqdm import tqdm

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('data_generation.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

@dataclass
class Candle:
    """Структура свечи OHLC"""
    timestamp: datetime
    open: Decimal
    high: Decimal
    low: Decimal
    close: Decimal
    volume: Decimal

    def to_dict(self):
        return {
            't': int(self.timestamp.timestamp() * 1000),
            'o': float(self.open),
            'h': float(self.high),
            'l': float(self.low),
            'c': float(self.close),
            'v': float(self.volume)
        }

@dataclass
class ChartSegment:
    """Структура сегмента графика"""
    symbol: str
    timeframe: str
    start_time: datetime
    display_candles: List[Candle]  # 50-100 свечей
    result_candles: List[Candle]   # 10-20 свечей
    price_at_decision: Decimal
    price_at_target: Decimal
    calculated_direction: int  # -1 = SHORT, 1 = LONG
    difficulty_level: int = 1
    segment_hash: str = ""

class DatabaseManager:
    """Менеджер работы с базой данных"""

    def __init__(self, db_url: str):
        self.db_url = db_url
        self.conn = None

    def connect(self):
        """Установка соединения с БД"""
        self.conn = psycopg2.connect(self.db_url)
        logger.info("Connected to database")

    def disconnect(self):
        """Закрытие соединения с БД"""
        if self.conn:
            self.conn.close()
            logger.info("Disconnected from database")

    def ensure_tables(self):
        """Создание таблиц если их нет"""
        with self.conn.cursor() as cur:
            # Проверяем существование таблицы symbols
            cur.execute("""
                CREATE TABLE IF NOT EXISTS symbols (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(50) UNIQUE NOT NULL,
                    description VARCHAR(255),
                    category VARCHAR(50) DEFAULT 'CRYPTO',
                    is_active BOOLEAN DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """)

            # Проверяем существование таблицы chart_segments (основная часть уже создана миграцией)
            cur.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                                  WHERE table_name='chart_segments' AND column_name='segment_hash') THEN
                        ALTER TABLE chart_segments ADD COLUMN segment_hash VARCHAR(64) UNIQUE;
                    END IF;

                    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                                  WHERE table_name='chart_segments' AND column_name='difficulty_level') THEN
                        ALTER TABLE chart_segments ADD COLUMN difficulty_level INTEGER DEFAULT 1;
                    END IF;

                    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                                  WHERE table_name='chart_segments' AND column_name='total_attempts') THEN
                        ALTER TABLE chart_segments ADD COLUMN total_attempts BIGINT DEFAULT 0;
                    END IF;
                END $$;
            """)

            self.conn.commit()
            logger.info("Tables ensured")

    def save_symbols(self, symbols: List[Dict]):
        """Сохранение символов в БД"""
        with self.conn.cursor() as cur:
            for symbol in symbols:
                cur.execute("""
                    INSERT INTO symbols (name, description, category, is_active)
                    VALUES (%s, %s, %s, %s)
                    ON CONFLICT (name) DO UPDATE SET
                        description = EXCLUDED.description,
                        category = EXCLUDED.category,
                        is_active = EXCLUDED.is_active
                """, (symbol['name'], symbol.get('description'),
                      symbol.get('category', 'CRYPTO'), symbol.get('is_active', True)))
            self.conn.commit()
        logger.info(f"Saved {len(symbols)} symbols")

    def save_chart_segments(self, segments: List[ChartSegment]):
        """Массовое сохранение сегментов графиков"""
        if not segments:
            return

        # Получаем id символов
        symbol_ids = {}
        with self.conn.cursor() as cur:
            cur.execute("SELECT id, name FROM symbols")
            for row in cur.fetchall():
                symbol_ids[row[1]] = row[0]

        # Подготавливаем данные для вставки
        data = []
        for segment in segments:
            if segment.symbol not in symbol_ids:
                logger.warning(f"Symbol {segment.symbol} not found in database")
                continue

            data.append((
                symbol_ids[segment.symbol],
                segment.timeframe,
                segment.start_time,
                json.dumps([c.to_dict() for c in segment.display_candles]),
                json.dumps([c.to_dict() for c in segment.result_candles]),
                float(segment.price_at_decision),
                float(segment.price_at_target),
                segment.calculated_direction,
                segment.difficulty_level,
                segment.segment_hash
            ))

        # Вставка с обработкой конфликтов
        with self.conn.cursor() as cur:
            execute_batch(cur, """
                INSERT INTO chart_segments
                (symbol_id, timeframe, start_time, display_candles, result_candles,
                 price_at_decision, price_at_target, calculated_direction,
                 difficulty_level, segment_hash)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT (segment_hash) DO UPDATE SET
                    display_candles = EXCLUDED.display_candles,
                    result_candles = EXCLUDED.result_candles,
                    difficulty_level = EXCLUDED.difficulty_level
            """, data)

            self.conn.commit()

        logger.info(f"Saved {len(segments)} chart segments")

class DataFetcher:
    """Класс для загрузки исторических данных"""

    @staticmethod
    def fetch_yahoo_data(symbol: str, period: str = "1y", interval: str = "1h") -> pd.DataFrame:
        """Загрузка данных с Yahoo Finance"""
        try:
            ticker = yf.Ticker(symbol)
            df = ticker.history(period=period, interval=interval)

            # Приведение к стандартному формату
            df = df.reset_index()
            df.columns = df.columns.str.lower()

            if 'datetime' in df.columns:
                df.rename(columns={'datetime': 'timestamp'}, inplace=True)
            elif 'date' in df.columns:
                df.rename(columns={'date': 'timestamp'}, inplace=True)

            required_cols = ['timestamp', 'open', 'high', 'low', 'close', 'volume']
            for col in required_cols:
                if col not in df.columns:
                    raise ValueError(f"Missing column {col} in data")

            return df[required_cols]
        except Exception as e:
            logger.error(f"Error fetching Yahoo data for {symbol}: {e}")
            return None

    @staticmethod
    def fetch_binance_data(symbol: str, timeframe: str = "1h", limit: int = 1000) -> pd.DataFrame:
        """Загрузка данных с Binance через CCXT"""
        try:
            exchange = ccxt.binance({
                'enableRateLimit': True,
                'options': {
                    'defaultType': 'spot',
                }
            })

            # Конвертируем timeframe в формат CCXT
            tf_mapping = {
                '15m': '15m', '30m': '30m', '1h': '1h',
                '4h': '4h', '1d': '1d'
            }

            if timeframe not in tf_mapping:
                timeframe = '1h'

            # Загружаем свечи
            ohlcv = exchange.fetch_ohlcv(symbol, tf_mapping[timeframe], limit=limit)

            # Конвертируем в DataFrame
            df = pd.DataFrame(ohlcv, columns=['timestamp', 'open', 'high', 'low', 'close', 'volume'])
            df['timestamp'] = pd.to_datetime(df['timestamp'], unit='ms')

            return df
        except Exception as e:
            logger.error(f"Error fetching Binance data for {symbol}: {e}")
            return None

    @staticmethod
    def load_from_csv(filepath: str) -> pd.DataFrame:
        """Загрузка данных из CSV файла"""
        try:
            df = pd.read_csv(filepath)

            # Автоопределение формата времени
            time_columns = ['timestamp', 'time', 'datetime', 'date']
            for col in time_columns:
                if col in df.columns:
                    df['timestamp'] = pd.to_datetime(df[col])
                    break

            return df
        except Exception as e:
            logger.error(f"Error loading CSV from {filepath}: {e}")
            return None

class ChartSegmentGenerator:
    """Генератор сегментов графиков для тренировки"""

    def __init__(self, display_candles: int = 70, result_candles: int = 15):
        self.display_candles = display_candles  # Сколько свечей показывать
        self.result_candles = result_candles    # Сколько свечей проверять

    def calculate_difficulty(self, candles: List[Candle]) -> int:
        """Расчет сложности сегмента (1-легко, 2-средне, 3-сложно)"""
        if len(candles) < 10:
            return 1

        # Вычисляем волатильность
        prices = [float(c.close) for c in candles]
        returns = np.diff(prices) / prices[:-1]
        volatility = np.std(returns) * 100  # В процентах

        # Анализ тренда
        from scipy import stats
        x = np.arange(len(prices))
        slope, _, _, _, _ = stats.linregress(x, prices)
        trend_strength = abs(slope * len(prices) / np.mean(prices) * 100)

        # Определяем сложность
        if volatility < 1.0 and trend_strength > 5.0:
            return 1  # Сильный тренд, низкая волатильность - легко
        elif volatility > 3.0 and trend_strength < 2.0:
            return 3  # Высокая волатильность, слабый тренд - сложно
        else:
            return 2  # Средняя сложность

    def generate_segments_from_df(self, df: pd.DataFrame, symbol: str, timeframe: str) -> List[ChartSegment]:
        """Генерация сегментов из DataFrame"""
        segments = []

        # Преобразуем DataFrame в список свечей
        candles = []
        for _, row in df.iterrows():
            try:
                candle = Candle(
                    timestamp=row['timestamp'],
                    open=Decimal(str(row['open'])),
                    high=Decimal(str(row['high'])),
                    low=Decimal(str(row['low'])),
                    close=Decimal(str(row['close'])),
                    volume=Decimal(str(row.get('volume', 0)))
                )
                candles.append(candle)
            except Exception as e:
                logger.warning(f"Error parsing candle: {e}")
                continue

        # Генерируем сегменты с шагом в 10 свечей для разнообразия
        step = 10
        for i in range(self.display_candles, len(candles) - self.result_candles, step):
            # Берем сегменты где достаточно данных
            if i + self.result_candles > len(candles):
                break

            display_slice = candles[i - self.display_candles:i]
            result_slice = candles[i:i + self.result_candles]

            # Пропускаем сегменты с пропусками данных
            if len(display_slice) != self.display_candles or len(result_slice) != self.result_candles:
                continue

            # Рассчитываем цены и направление
            price_at_decision = display_slice[-1].close
            price_at_target = result_slice[-1].close

            # Определяем направление (фильтруем боковики)
            price_change_pct = (price_at_target - price_at_decision) / price_at_decision * 100

            # Если изменение меньше 0.5%, считаем боковиком (direction = 0)
            if abs(price_change_pct) < 0.5:
                calculated_direction = 0  # Боковик
            else:
                calculated_direction = 1 if price_change_pct > 0 else -1

            # Пропускаем боковики (можно включить опционально)
            if calculated_direction == 0:
                continue

            # Рассчитываем сложность
            difficulty = self.calculate_difficulty(display_slice)

            # Генерируем уникальный хеш для дедупликации
            data_str = f"{symbol}_{timeframe}_{display_slice[0].timestamp}_{price_at_decision}"
            segment_hash = hashlib.sha256(data_str.encode()).hexdigest()

            # Создаем сегмент
            segment = ChartSegment(
                symbol=symbol,
                timeframe=timeframe,
                start_time=display_slice[0].timestamp,
                display_candles=display_slice,
                result_candles=result_slice,
                price_at_decision=price_at_decision,
                price_at_target=price_at_target,
                calculated_direction=calculated_direction,
                difficulty_level=difficulty,
                segment_hash=segment_hash
            )

            segments.append(segment)

        return segments

def main():
    """Основная функция"""
    parser = argparse.ArgumentParser(description='Generate chart data for TradeGuess')
    parser.add_argument('--db-url', required=True, help='Database connection URL')
    parser.add_argument('--symbols', nargs='+', default=['BTC-USD', 'ETH-USD', 'AAPL', 'TSLA', 'NVDA'],
                       help='Symbols to fetch data for')
    parser.add_argument('--timeframes', nargs='+', default=['1h', '4h', '1d'],
                       help='Timeframes to generate')
    parser.add_argument('--source', choices=['yahoo', 'binance', 'csv'], default='yahoo',
                       help='Data source')
    parser.add_argument('--csv-dir', help='Directory with CSV files for CSV source')
    parser.add_argument('--min-segments', type=int, default=1000,
                       help='Minimum number of segments to generate')
    parser.add_argument('--max-segments-per-symbol', type=int, default=500,
                       help='Maximum segments per symbol')

    args = parser.parse_args()

    # Инициализация менеджеров
    db = DatabaseManager(args.db_url)
    db.connect()
    db.ensure_tables()

    fetcher = DataFetcher()
    generator = ChartSegmentGenerator(display_candles=70, result_candles=15)

    # Сохраняем символы в БД
    symbols_to_save = []
    for symbol in args.symbols:
        symbols_to_save.append({
            'name': symbol,
            'description': f'{symbol} price data',
            'category': 'CRYPTO' if '-' in symbol else 'STOCK'
        })

    db.save_symbols(symbols_to_save)

    # Собираем все сегменты
    all_segments = []

    for symbol in tqdm(args.symbols, desc="Processing symbols"):
        for timeframe in args.timeframes:
            logger.info(f"Processing {symbol} with timeframe {timeframe}")

            # Загрузка данных в зависимости от источника
            df = None
            if args.source == 'yahoo':
                df = fetcher.fetch_yahoo_data(symbol, period="6mo", interval=timeframe)
            elif args.source == 'binance':
                df = fetcher.fetch_binance_data(symbol.replace('-', '/'), timeframe)
            elif args.source == 'csv' and args.csv_dir:
                csv_file = os.path.join(args.csv_dir, f"{symbol}_{timeframe}.csv")
                if os.path.exists(csv_file):
                    df = fetcher.load_from_csv(csv_file)

            if df is None or df.empty:
                logger.warning(f"No data for {symbol} {timeframe}")
                continue

            # Генерация сегментов
            segments = generator.generate_segments_from_df(df, symbol, timeframe)

            # Ограничиваем количество сегментов на символ
            if len(segments) > args.max_segments_per_symbol:
                segments = segments[:args.max_segments_per_symbol]

            all_segments.extend(segments)
            logger.info(f"Generated {len(segments)} segments for {symbol} {timeframe}")

            # Сохраняем порциями чтобы не перегружать память
            if len(all_segments) >= 500:
                db.save_chart_segments(all_segments)
                all_segments = []

    # Сохраняем оставшиеся сегменты
    if all_segments:
        db.save_chart_segments(all_segments)

    # Итоговая статистика
    with db.conn.cursor() as cur:
        cur.execute("SELECT COUNT(*) FROM chart_segments")
        total_count = cur.fetchone()[0]

        cur.execute("SELECT COUNT(DISTINCT symbol_id) FROM chart_segments")
        symbols_count = cur.fetchone()[0]

        cur.execute("""
            SELECT difficulty_level, COUNT(*),
                   ROUND(AVG(price_at_target - price_at_decision) / AVG(price_at_decision) * 100, 2)
            FROM chart_segments
            GROUP BY difficulty_level
            ORDER BY difficulty_level
        """)
        difficulty_stats = cur.fetchall()

    logger.info("\n" + "="*50)
    logger.info("GENERATION COMPLETE")
    logger.info("="*50)
    logger.info(f"Total segments in DB: {total_count}")
    logger.info(f"Symbols with data: {symbols_count}")
    logger.info("\nDifficulty distribution:")
    for level, count, avg_change in difficulty_stats:
        logger.info(f"  Level {level}: {count} segments, avg change: {avg_change}%")

    db.disconnect()
    logger.info("Data generation finished successfully")

if __name__ == "__main__":
    main()