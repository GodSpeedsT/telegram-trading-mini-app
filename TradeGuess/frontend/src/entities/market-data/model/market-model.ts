import { historicalDatasets } from '@shared/config/market-config';
import { generateCryptoData } from '../lib/generators/crypto-generator';
import { generateStockData } from '../lib/generators/stock-generator';
import { generateForexData } from '../lib/generators/forex-generator';
import { getRandomAsset, getAsset, getAllAssets, getChartData } from '../lib/helpers/market-helpers';

// Инициализация данных
function initializeData() {
  // Криптовалюты
  historicalDatasets.crypto.bitcoin.data = generateCryptoData('BTC', 150, 30000, 60000);
  historicalDatasets.crypto.ethereum.data = generateCryptoData('ETH', 150, 1500, 4000);
  historicalDatasets.crypto.solana.data = generateCryptoData('SOL', 150, 20, 200);

  // Акции
  historicalDatasets.stocks.apple.data = generateStockData('AAPL', 150, 140, 200);
  historicalDatasets.stocks.tesla.data = generateStockData('TSLA', 150, 150, 300);
  historicalDatasets.stocks.microsoft.data = generateStockData('MSFT', 150, 250, 450);

  // Forex
  historicalDatasets.forex.eurusd.data = generateForexData('EURUSD', 150, 1.05, 1.15);
  historicalDatasets.forex.gbpusd.data = generateForexData('GBPUSD', 150, 1.20, 1.35);
  historicalDatasets.forex.usdjpy.data = generateForexData('USDJPY', 150, 130, 150);
}

// Экспорт публичного API модели
export const marketModel = {
  initializeData,
  getRandomAsset,
  getAsset,
  getAllAssets,
  getChartData,
  datasets: historicalDatasets
};

// Инициализируем данные при импорте
initializeData();
