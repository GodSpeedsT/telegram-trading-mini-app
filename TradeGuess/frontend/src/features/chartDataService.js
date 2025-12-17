// Исторические данные по различным активам
export const historicalDatasets = {
  // КРИПТОВАЛЮТЫ
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

  // АКЦИИ
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

  // FOREX
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

// Генератор данных для криптовалют (высокая волатильность)
function generateCryptoData(symbol, count, minPrice, maxPrice) {
  const data = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    // Криптовалюты имеют высокую волатильность (до 10% в день)
    const volatility = 0.08 + Math.random() * 0.04;

    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;

    // Сильные движения для крипто
    const high = Math.max(open, close) * (1 + Math.random() * 0.04);
    const low = Math.min(open, close) * (1 - Math.random() * 0.04);

    // Объемы для крипто обычно выше
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

// Генератор данных для акций (средняя волатильность)
function generateStockData(symbol, count, minPrice, maxPrice) {
  const data = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    // Пропускаем выходные для акций
    const dayOfWeek = date.getDay();
    if (dayOfWeek === 0 || dayOfWeek === 6) continue;

    // Акции имеют среднюю волатильность (2-5% в день)
    const volatility = 0.03 + Math.random() * 0.02;

    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;

    // Акции имеют более узкий спред
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
    const lastDate = new Date(data[data.length - 1].timestamp);
    lastDate.setDate(lastDate.getDate() + 1);

    // Пропускаем выходные
    const dayOfWeek = lastDate.getDay();
    if (dayOfWeek === 0 || dayOfWeek === 6) {
      lastDate.setDate(lastDate.getDate() + 1);
    }

    const volatility = 0.03 + Math.random() * 0.02;
    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) * (1 + Math.random() * 0.02);
    const low = Math.min(open, close) * (1 - Math.random() * 0.02);
    const volume = Math.floor(Math.random() * 5000000) + 1000000;

    data.push({
      date: lastDate.toISOString().split('T')[0],
      timestamp: lastDate.getTime(),
      open: Number(open.toFixed(2)),
      close: Number(close.toFixed(2)),
      high: Number(high.toFixed(2)),
      low: Number(low.toFixed(2)),
      volume: volume,
      symbol: symbol
    });

    price = close;
  }

  return data.slice(0, count);
}

// Генератор данных для Forex (низкая волатильность)
function generateForexData(symbol, count, minPrice, maxPrice) {
  const data = [];
  let price = (minPrice + maxPrice) / 2;
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    // Forex торгуется 24/5, но оставляем данные на каждый день
    const volatility = 0.005 + Math.random() * 0.005; // 0.5-1% волатильность

    const open = price;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;

    // Forex имеет очень узкий спред
    const high = Math.max(open, close) * (1 + Math.random() * 0.001);
    const low = Math.min(open, close) * (1 - Math.random() * 0.001);

    // Объемы в Forex огромные
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

// Получить случайный актив
export function getRandomAsset() {
  const categories = Object.keys(historicalDatasets);
  const randomCategory = categories[Math.floor(Math.random() * categories.length)];
  const assets = Object.keys(historicalDatasets[randomCategory]);
  const randomAsset = assets[Math.floor(Math.random() * assets.length)];

  return {
    category: randomCategory,
    assetKey: randomAsset,
    asset: historicalDatasets[randomCategory][randomAsset]
  };
}

// Получить конкретный актив
export function getAsset(category, assetKey) {
  return historicalDatasets[category]?.[assetKey] || null;
}

// Получить все активы
export function getAllAssets() {
  const allAssets = [];

  Object.keys(historicalDatasets).forEach(category => {
    Object.keys(historicalDatasets[category]).forEach(assetKey => {
      allAssets.push({
        category,
        assetKey,
        ...historicalDatasets[category][assetKey]
      });
    });
  });

  return allAssets;
}

// Получить данные для графика
export function getChartData(category, assetKey, candleCount = 100) {
  const asset = getAsset(category, assetKey);
  if (!asset) return null;

  return {
    ...asset,
    data: asset.data.slice(0, candleCount)
  };
}
