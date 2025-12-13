// Бесплатные API для криптовалют и акций
const API_URLS = {
  binance: 'https://api.binance.com/api/v3',
  coincap: 'https://api.coincap.io/v2',
  coinranking: 'https://api.coinranking.com/v2',
  fmp: 'https://financialmodelingprep.com/api/v3',
  alphavantage: 'https://www.alphavantage.co/query', // Альтернатива FMP
  yahooFinance: 'https://query1.finance.yahoo.com/v8/finance/chart' // Yahoo Finance (требует CORS proxy)
};

// Кеширование
const cache = new Map();
const CACHE_DURATION = 2 * 60 * 1000; // 2 минуты для свежих данных

const getCachedData = (key) => {
  const cached = cache.get(key);
  if (cached && Date.now() - cached.timestamp < CACHE_DURATION) {
    return cached.data;
  }
  return null;
};

const setCachedData = (key, data) => {
  cache.set(key, {
    data,
    timestamp: Date.now()
  });
};

// Основной сервис для криптовалют (используем Binance как основной)
export const cryptoAPI = {
  // Получить список криптовалют
  async getCryptocurrencies() {
    const cacheKey = 'crypto_list';
    const cached = getCachedData(cacheKey);
    if (cached) return cached;

    try {
      // Пробуем Binance (самый надежный)
      return await this.getFromBinance();
    } catch (error) {
      console.log('Binance failed, trying CoinCap...');
      try {
        return await this.getFromCoinCap();
      } catch (error2) {
        console.log('CoinCap failed, trying CoinRanking...');
        try {
          return await this.getFromCoinRanking();
        } catch (error3) {
          console.error('All crypto APIs failed:', error3);
          return getFallbackCryptos();
        }
      }
    }
  },

  // Binance API
  async getFromBinance() {
    const response = await fetch(`${API_URLS.binance}/ticker/24hr`);
    if (!response.ok) throw new Error('Binance API error');

    const data = await response.json();

    // Фильтруем только пары с USDT
    const usdtPairs = data.filter(item => item.symbol.endsWith('USDT'));

    const result = usdtPairs.slice(0, 30).map(coin => ({
      id: coin.symbol.toLowerCase().replace('usdt', ''),
      symbol: coin.symbol.replace('USDT', ''),
      name: coin.symbol.replace('USDT', ''),
      price: parseFloat(coin.lastPrice),
      priceChangePercent: parseFloat(coin.priceChangePercent),
      volume: parseFloat(coin.volume),
      high24h: parseFloat(coin.highPrice),
      low24h: parseFloat(coin.lowPrice),
      api: 'binance'
    }));

    setCachedData('crypto_list', result);
    return result;
  },

  // CoinCap API
  async getFromCoinCap() {
    const response = await fetch(`${API_URLS.coincap}/assets?limit=30`);
    if (!response.ok) throw new Error('CoinCap API error');

    const data = await response.json();

    const result = data.data.map(coin => ({
      id: coin.id,
      symbol: coin.symbol,
      name: coin.name,
      price: parseFloat(coin.priceUsd),
      priceChangePercent: parseFloat(coin.changePercent24Hr),
      volume: parseFloat(coin.volumeUsd24Hr),
      marketCap: parseFloat(coin.marketCapUsd),
      api: 'coincap'
    }));

    setCachedData('crypto_list', result);
    return result;
  },

  // CoinRanking API
  async getFromCoinRanking() {
    const response = await fetch(`${API_URLS.coinranking}/coins?limit=30`);
    if (!response.ok) throw new Error('CoinRanking API error');

    const data = await response.json();

    const result = data.data.coins.map(coin => ({
      id: coin.uuid,
      symbol: coin.symbol,
      name: coin.name,
      price: parseFloat(coin.price),
      priceChangePercent: parseFloat(coin.change),
      volume: parseFloat(coin['24hVolume']),
      marketCap: parseFloat(coin.marketCap),
      icon: coin.iconUrl,
      api: 'coinranking'
    }));

    setCachedData('crypto_list', result);
    return result;
  },

  // Получить исторические данные
  async getCryptoHistory(coinId, days = 90) {
    const cacheKey = `crypto_history_${coinId}_${days}`;
    const cached = getCachedData(cacheKey);
    if (cached) return cached;

    try {
      // Для Binance нужно добавить USDT к символу
      const symbol = coinId.toUpperCase() === 'BTC' ? 'BTCUSDT' :
        coinId.toUpperCase() + 'USDT';

      // Вычисляем интервал в зависимости от периода
      let interval = '1d';
      let limit = days;

      if (days <= 7) {
        interval = '1h';
        limit = days * 24;
      } else if (days <= 30) {
        interval = '4h';
        limit = days * 6;
      }

      const response = await fetch(
        `${API_URLS.binance}/klines?symbol=${symbol}&interval=${interval}&limit=${limit}`
      );

      if (!response.ok) throw new Error('Binance history error');

      const data = await response.json();

      const ohlcData = data.map(kline => ({
        timestamp: kline[0],
        date: new Date(kline[0]).toISOString().split('T')[0],
        open: parseFloat(kline[1]),
        high: parseFloat(kline[2]),
        low: parseFloat(kline[3]),
        close: parseFloat(kline[4]),
        volume: parseFloat(kline[5])
      }));

      setCachedData(cacheKey, ohlcData);
      return ohlcData;
    } catch (error) {
      console.error('Error fetching crypto history:', error);
      return getFallbackCryptoHistory(days);
    }
  }
};

// Сервис для акций (бесплатные альтернативы)
export const stocksAPI = {
  // Получить список популярных акций
  async getStocks() {
    const cacheKey = 'stocks_list';
    const cached = getCachedData(cacheKey);
    if (cached) return cached;

    try {
      // Используем статический список популярных акций
      const popularStocks = [
        { symbol: 'AAPL', name: 'Apple Inc.' },
        { symbol: 'MSFT', name: 'Microsoft Corporation' },
        { symbol: 'GOOGL', name: 'Alphabet Inc. (Google)' },
        { symbol: 'AMZN', name: 'Amazon.com Inc.' },
        { symbol: 'TSLA', name: 'Tesla Inc.' },
        { symbol: 'META', name: 'Meta Platforms Inc.' },
        { symbol: 'NVDA', name: 'NVIDIA Corporation' },
        { symbol: 'JPM', name: 'JPMorgan Chase & Co.' },
        { symbol: 'V', name: 'Visa Inc.' },
        { symbol: 'JNJ', name: 'Johnson & Johnson' }
      ];

      // Параллельно запрашиваем цены для всех акций
      const stockPromises = popularStocks.map(stock =>
        this.getStockPrice(stock.symbol).then(priceData => ({
          ...stock,
          price: priceData.price,
          change: priceData.change,
          changePercentage: priceData.changePercentage
        })).catch(() => ({
          ...stock,
          price: this.getFallbackPrice(stock.symbol),
          change: 0,
          changePercentage: 0
        }))
      );

      const stocksWithPrices = await Promise.all(stockPromises);

      setCachedData(cacheKey, stocksWithPrices);
      return stocksWithPrices;
    } catch (error) {
      console.error('Error fetching stocks:', error);
      return getFallbackStocks();
    }
  },

  // Получить цену акции через Yahoo Finance (требует CORS proxy в браузере)
  async getStockPrice(symbol) {
    try {
      // Используем public CORS proxy
      const proxyUrl = 'https://api.allorigins.win/raw?url=';
      const yahooUrl = `${API_URLS.yahooFinance}/${symbol}?range=1d&interval=1d`;

      const response = await fetch(proxyUrl + encodeURIComponent(yahooUrl));

      if (!response.ok) throw new Error('Yahoo Finance error');

      const data = await response.json();

      if (data.chart?.result?.[0]?.meta) {
        const meta = data.chart.result[0].meta;
        return {
          price: meta.regularMarketPrice,
          change: meta.regularMarketPrice - meta.previousClose,
          changePercentage: ((meta.regularMarketPrice - meta.previousClose) / meta.previousClose) * 100
        };
      }

      throw new Error('Invalid data format');
    } catch (error) {
      // Fallback на Alpha Vantage
      return await this.getStockPriceAlphaVantage(symbol);
    }
  },

  // Альтернатива через Alpha Vantage (бесплатно, 5 запросов/мин)
  async getStockPriceAlphaVantage(symbol) {
    try {
      // Демо ключ (лучше получить свой на alphavantage.co)
      const apiKey = 'demo';
      const response = await fetch(
        `${API_URLS.alphavantage}?function=GLOBAL_QUOTE&symbol=${symbol}&apikey=${apiKey}`
      );

      if (!response.ok) throw new Error('Alpha Vantage error');

      const data = await response.json();

      if (data['Global Quote']) {
        const quote = data['Global Quote'];
        return {
          price: parseFloat(quote['05. price']),
          change: parseFloat(quote['09. change']),
          changePercentage: parseFloat(quote['10. change percent'].replace('%', ''))
        };
      }

      throw new Error('Invalid data format');
    } catch (error) {
      console.error('Alpha Vantage failed:', error);
      // Возвращаем fallback
      return {
        price: this.getFallbackPrice(symbol),
        change: 0,
        changePercentage: 0
      };
    }
  },

  // Получить исторические данные акции
  async getStockHistory(symbol, period = '3month') {
    const cacheKey = `stock_history_${symbol}_${period}`;
    const cached = getCachedData(cacheKey);
    if (cached) return cached;

    try {
      // Пробуем Yahoo Finance через CORS proxy
      const intervals = {
        '7': '1d',      // 7 дней - дневные данные
        '30': '1d',     // 30 дней - дневные
        '90': '1d',     // 3 месяца - дневные
        '180': '1wk',   // 6 месяцев - недельные
        '365': '1mo'    // 1 год - месячные
      };

      const interval = intervals[period] || '1d';
      const range = period === '365' ? '1y' : `${period}d`;

      const proxyUrl = 'https://api.allorigins.win/raw?url=';
      const yahooUrl = `${API_URLS.yahooFinance}/${symbol}?range=${range}&interval=${interval}`;

      const response = await fetch(proxyUrl + encodeURIComponent(yahooUrl));

      if (!response.ok) throw new Error('Yahoo Finance history error');

      const data = await response.json();

      if (data.chart?.result?.[0]) {
        const result = data.chart.result[0];
        const timestamps = result.timestamp;
        const quotes = result.indicators.quote[0];

        const ohlcData = timestamps.map((timestamp, index) => ({
          timestamp: timestamp * 1000,
          date: new Date(timestamp * 1000).toISOString().split('T')[0],
          open: quotes.open[index],
          high: quotes.high[index],
          low: quotes.low[index],
          close: quotes.close[index],
          volume: quotes.volume[index]
        })).filter(item => item.open && item.close); // Фильтруем пустые данные

        setCachedData(cacheKey, ohlcData);
        return ohlcData;
      }

      throw new Error('Invalid data format');
    } catch (error) {
      console.error('Error fetching stock history:', error);
      return getFallbackStockHistory(symbol, parseInt(period));
    }
  },

  // Вспомогательные методы
  getFallbackPrice(symbol) {
    const prices = {
      'AAPL': 180, 'MSFT': 330, 'GOOGL': 135, 'AMZN': 145,
      'TSLA': 245, 'META': 320, 'NVDA': 500, 'JPM': 150,
      'V': 250, 'JNJ': 155
    };
    return prices[symbol] || 100;
  }
};

// Fallback данные
function getFallbackCryptos() {
  return [
    { id: 'bitcoin', symbol: 'BTC', name: 'Bitcoin', price: 45000, priceChangePercent: 2.5 },
    { id: 'ethereum', symbol: 'ETH', name: 'Ethereum', price: 2500, priceChangePercent: 1.8 },
    { id: 'solana', symbol: 'SOL', name: 'Solana', price: 95, priceChangePercent: 5.2 },
    { id: 'cardano', symbol: 'ADA', name: 'Cardano', price: 0.45, priceChangePercent: -0.5 }
  ];
}

function getFallbackCryptoHistory(days = 90) {
  const data = [];
  let price = 45000;
  const volatility = 0.04;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - days);

  for (let i = 0; i < days; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.02);
    const low = Math.min(open, close) * (1 - Math.random() * 0.02);

    data.push({
      timestamp: date.getTime(),
      date: date.toISOString().split('T')[0],
      open: Number(open.toFixed(2)),
      high: Number(high.toFixed(2)),
      low: Number(low.toFixed(2)),
      close: Number(close.toFixed(2)),
      volume: Math.floor(Math.random() * 100000000) + 50000000
    });

    price = close;
  }

  return data;
}

function getFallbackStocks() {
  return [
    { symbol: 'AAPL', name: 'Apple Inc.', price: 180.50, change: 2.5, changePercentage: 1.4 },
    { symbol: 'MSFT', name: 'Microsoft', price: 330.45, change: 3.2, changePercentage: 0.98 },
    { symbol: 'GOOGL', name: 'Alphabet', price: 135.20, change: 1.8, changePercentage: 1.35 }
  ];
}

function getFallbackStockHistory(symbol, days) {
  const basePrices = {
    'AAPL': 180, 'MSFT': 330, 'GOOGL': 135, 'AMZN': 145,
    'TSLA': 245, 'META': 320, 'NVDA': 500
  };

  const basePrice = basePrices[symbol] || 100;
  const data = [];
  let price = basePrice;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - days);

  for (let i = 0; i < days; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    // Пропускаем выходные
    if (date.getDay() === 0 || date.getDay() === 6) continue;

    const volatility = 0.02;
    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.01);
    const low = Math.min(open, close) * (1 - Math.random() * 0.01);

    data.push({
      timestamp: date.getTime(),
      date: date.toISOString().split('T')[0],
      open: Number(open.toFixed(2)),
      high: Number(high.toFixed(2)),
      low: Number(low.toFixed(2)),
      close: Number(close.toFixed(2)),
      volume: Math.floor(Math.random() * 50000000) + 10000000
    });

    price = close;
  }

  return data;
}

// Экспортируемые функции
export async function getMarketData(type = 'crypto') {
  if (type === 'crypto') {
    return await cryptoAPI.getCryptocurrencies();
  } else {
    return await stocksAPI.getStocks();
  }
}

export async function getHistoricalData(type, id, symbol, period = '90') {
  if (type === 'crypto') {
    return await cryptoAPI.getCryptoHistory(id, parseInt(period));
  } else {
    return await stocksAPI.getStockHistory(symbol, period);
  }
}
