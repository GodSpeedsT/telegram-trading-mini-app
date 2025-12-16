export interface CandleData {
  date: string;
  timestamp: number;
  open: number;
  close: number;
  high: number;
  low: number;
  volume: number;
  symbol: string;
}

export type VolatilityLevel = 'very high' | 'high' | 'medium' | 'low';

export interface AssetData {
  name: string;
  symbol: string;
  description: string;
  data: CandleData[];
  period: string;
  volatility: VolatilityLevel;
  color: string;
}

export interface AssetCategory {
  [key: string]: AssetData;
}

export interface HistoricalDatasets {
  crypto: AssetCategory;
  stocks: AssetCategory;
  forex: AssetCategory;
}

export interface AssetInfo {
  category: keyof HistoricalDatasets;
  assetKey: string;
  asset: AssetData;
}

export interface AssetWithMeta extends AssetData {
  category: keyof HistoricalDatasets;
  assetKey: string;
}
