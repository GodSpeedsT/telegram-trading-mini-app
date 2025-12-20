export interface ServerCandle {
  t: number;
  o: number;
  h: number;
  l: number;
  c: number;
  v: number;
}

export interface Candle {
  date: string;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
}

export interface ChartResponse {
  success: boolean;
  data?: {
    segmentId: number;
    candles: ServerCandle[];
    attemptsLeft: number;
  };
  message?: string;
}

export interface GuessResponse {
  success: boolean;
  data: {
    isCorrect: boolean;
    resultCandles: ServerCandle[];
    message: string;
    priceChangePercent: number;
  };
}
