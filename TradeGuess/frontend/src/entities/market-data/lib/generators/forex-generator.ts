import type { CandleData } from '../../../../shared/types/market';

export function generateForexData(symbol: string, count: number, minPrice: number, maxPrice: number): CandleData[] {
  const data: CandleData[] = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    const volatility = 0.005 + Math.random() * 0.005;
    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.001);
    const low = Math.min(open, close) * (1 - Math.random() * 0.001);
    const volume = Math.floor(Math.random() * 100000000) + 50000000;

    data.push({
      date: date.toISOString().split('T')[0],
      timestamp: date.getTime(),
      open: Number(open.toFixed(4)),
      close: Number(close.toFixed(4)),
      high: Number(high.toFixed(4)),
      low: Number(low.toFixed(4)),
      volume: volume,
      symbol: symbol
    });

    price = close;
  }

  return data;
}
