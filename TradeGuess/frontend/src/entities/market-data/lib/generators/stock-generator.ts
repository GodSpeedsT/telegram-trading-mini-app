import type { CandleData } from '../../../../shared/types/market';

export function generateStockData(symbol: string, count: number, minPrice: number, maxPrice: number): CandleData[] {
  const data: CandleData[] = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);
    const dayOfWeek = date.getDay();
    if (dayOfWeek === 0 || dayOfWeek === 6) continue;

    const volatility = 0.03 + Math.random() * 0.02;
    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.02);
    const low = Math.min(open, close) * (1 - Math.random() * 0.02);
    const volume = Math.floor(Math.random() * 5000000) + 1000000;

    data.push({
      date: date.toISOString().split('T')[0],
      timestamp: date.getTime(),
      open: Number(open.toFixed(2)),
      close: Number(close.toFixed(2)),
      high: Number(high.toFixed(2)),
      low: Number(low.toFixed(2)),
      volume: volume,
      symbol: symbol
    });

    price = close;
  }

  // Дополняем до нужного количества, если пропустили выходные
  while (data.length < count) {
    // ... (остальной код генерации)
  }

  return data.slice(0, count);
}
