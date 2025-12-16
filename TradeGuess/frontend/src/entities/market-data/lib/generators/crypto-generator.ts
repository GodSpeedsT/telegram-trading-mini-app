import type { CandleData } from '../../../../shared/types/market';

export function generateCryptoData(symbol: string, count: number, minPrice: number, maxPrice: number): CandleData[] {
  const data: CandleData[] = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    const volatility = 0.08 + Math.random() * 0.04;
    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.04);
    const low = Math.min(open, close) * (1 - Math.random() * 0.04);
    const volume = Math.floor(Math.random() * 50000000) + 10000000;

    data.push({
      date: date.toISOString().split('T')[0],
      timestamp: date.getTime(),
      open: Number(open.toFixed(symbol === 'BTC' ? 0 : 2)),
      close: Number(close.toFixed(symbol === 'BTC' ? 0 : 2)),
      high: Number(high.toFixed(symbol === 'BTC' ? 0 : 2)),
      low: Number(low.toFixed(symbol === 'BTC' ? 0 : 2)),
      volume: volume,
      symbol: symbol
    });

    price = close;
  }

  return data;
}
