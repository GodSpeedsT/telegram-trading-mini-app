import type { HistoricalDatasets } from '../types/market';

export const historicalDatasets: HistoricalDatasets = {
  crypto: {
    bitcoin: {
      name: 'Bitcoin (BTC/USD)',
      symbol: 'BTC-USD',
      description: 'Курс Bitcoin к доллару США',
      data: generateCryptoData('BTC', 150, 30000, 60000),
      period: '2023-2024',
      volatility: 'high',
      color: '#F7931A'
    },
    ethereum: {
      name: 'Ethereum (ETH/USD)',
      symbol: 'ETH-USD',
      description: 'Курс Ethereum к доллару США',
      data: generateCryptoData('ETH', 150, 1500, 4000),
      period: '2023-2024',
      volatility: 'high',
      color: '#627EEA'
    },
    solana: {
      name: 'Solana (SOL/USD)',
      symbol: 'SOL-USD',
      description: 'Курс Solana к доллару США',
      data: generateCryptoData('SOL', 150, 20, 200),
      period: '2023-2024',
      volatility: 'very high',
      color: '#00FFA3'
    }
  },
  stocks: {
    apple: {
      name: 'Apple Inc. (AAPL)',
      symbol: 'AAPL',
      description: 'Акции Apple Inc.',
      data: generateStockData('AAPL', 150, 140, 200),
      period: '2023-2024',
      volatility: 'medium',
      color: '#A2AAAD'
    },
    tesla: {
      name: 'Tesla Inc. (TSLA)',
      symbol: 'TSLA',
      description: 'Акции Tesla Inc.',
      data: generateStockData('TSLA', 150, 150, 300),
      period: '2023-2024',
      volatility: 'high',
      color: '#CC0000'
    },
    microsoft: {
      name: 'Microsoft Corp. (MSFT)',
      symbol: 'MSFT',
      description: 'Акции Microsoft Corporation',
      data: generateStockData('MSFT', 150, 250, 450),
      period: '2023-2024',
      volatility: 'low',
      color: '#F65314'
    }
  },
  forex: {
    eurusd: {
      name: 'EUR/USD',
      symbol: 'EURUSD',
      description: 'Курс Евро к доллару США',
      data: generateForexData('EURUSD', 150, 1.05, 1.15),
      period: '2023-2024',
      volatility: 'low',
      color: '#16377B'
    },
    gbpusd: {
      name: 'GBP/USD',
      symbol: 'GBPUSD',
      description: 'Курс Британского фунта к доллару США',
      data: generateForexData('GBPUSD', 150, 1.20, 1.35),
      period: '2023-2024',
      volatility: 'medium',
      color: '#B22234'
    },
    usdjpy: {
      name: 'USD/JPY',
      symbol: 'USDJPY',
      description: 'Курс доллара США к Японской иене',
      data: generateForexData('USDJPY', 150, 130, 150),
      period: '2023-2024',
      volatility: 'medium',
      color: '#BC002D'
    }
  }
};
