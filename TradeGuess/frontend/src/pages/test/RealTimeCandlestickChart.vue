<template>
  <div class="crypto-chart">
    <!-- Статус загрузки -->
    <div v-if="status === 'loading'" class="status-message loading">
      <div class="spinner"></div>
      <p>Загрузка данных с биржи...</p>
    </div>

    <div v-else-if="status === 'error'" class="status-message error">
      <p>⚠️ Не удалось загрузить реальные данные. Показываем демо-график.</p>
      <button @click="retryLoad" class="retry-btn">Повторить попытку</button>
    </div>

    <!-- Основной интерфейс -->
    <div v-else>
      <!-- Панель выбора -->
      <div class="selection-panel">
        <div class="market-switch">
          <button
            v-for="market in markets"
            :key="market.id"
            :class="['market-btn', { active: selectedMarket === market.id }]"
            @click="switchMarket(market.id)"
          >
            {{ market.label }}
          </button>
        </div>

        <div class="asset-selection">
          <select v-model="selectedAsset" @change="loadChartData" class="asset-select">
            <option value="">-- Выберите {{ selectedMarket === 'crypto' ? 'криптовалюту' : 'акцию' }} --</option>
            <option
              v-for="asset in assets"
              :key="asset.id || asset.symbol"
              :value="asset"
            >
              {{ asset.name }} ({{ asset.symbol }})
              <template v-if="asset.price">
                - ${{ formatNumber(asset.price) }}
                <span :class="getChangeClass(asset)">
                  {{ formatPercent(asset.priceChangePercent || asset.changePercentage) }}
                </span>
              </template>
            </option>
          </select>

          <div class="period-buttons">
            <button
              v-for="period in periods"
              :key="period.days"
              :class="['period-btn', { active: selectedPeriod === period.days }]"
              @click="selectPeriod(period.days)"
            >
              {{ period.label }}
            </button>
          </div>
        </div>
      </div>

      <!-- График -->
      <div v-if="chartData.length > 0" class="chart-area">
        <div class="chart-header">
          <h3>{{ chartTitle }}</h3>
          <div class="chart-stats">
            <div class="stat">
              <span class="label">Период:</span>
              <span class="value">{{ formatDateRange() }}</span>
            </div>
            <div class="stat">
              <span class="label">Свечей:</span>
              <span class="value">{{ chartData.length }}</span>
            </div>
            <div v-if="currentAsset" class="stat price-stat" :class="priceChangeClass">
              <span class="label">Цена:</span>
              <span class="value">
                ${{ formatNumber(currentPrice) }}
                <span class="change">
                  {{ formatPercent(priceChangePercent) }}
                </span>
              </span>
            </div>
          </div>
        </div>

        <div ref="chartContainer" class="chart-container"></div>

        <!-- Легенда -->
        <div class="chart-legend">
          <div class="legend-item">
            <span class="legend-color" style="background-color: #10b981;"></span>
            <span>Рост (закрытие > открытия)</span>
          </div>
          <div class="legend-item">
            <span class="legend-color" style="background-color: #ef4444;"></span>
            <span>Падение (закрытие < открытия)</span>
          </div>
          <div class="legend-item">
            <span class="api-source">Источник: {{ dataSource }}</span>
          </div>
        </div>
      </div>

      <!-- Информационная панель -->
      <div v-if="currentAsset && chartData.length > 0" class="info-panel">
        <div class="info-cards">
          <div class="info-card">
            <div class="card-title">Открытие</div>
            <div class="card-value">${{ formatNumber(lastCandle.open) }}</div>
          </div>
          <div class="info-card">
            <div class="card-title">Закрытие</div>
            <div class="card-value" :class="priceChangeClass">
              ${{ formatNumber(lastCandle.close) }}
            </div>
          </div>
          <div class="info-card">
            <div class="card-title">Максимум</div>
            <div class="card-value positive">${{ formatNumber(lastCandle.high) }}</div>
          </div>
          <div class="info-card">
            <div class="card-title">Минимум</div>
            <div class="card-value negative">${{ formatNumber(lastCandle.low) }}</div>
          </div>
          <div class="info-card">
            <div class="card-title">Изменение</div>
            <div class="card-value" :class="priceChangeClass">
              {{ formatPercent(priceChangePercent) }}
            </div>
          </div>
          <div class="info-card">
            <div class="card-title">Объем (24ч)</div>
            <div class="card-value">{{ formatVolume(lastCandle.volume) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { getMarketData, getHistoricalData } from '@/features/apiService';

// Состояние
const status = ref('loading'); // 'loading', 'error', 'ready'
const selectedMarket = ref('crypto');
const selectedAsset = ref(null);
const selectedPeriod = ref('90');
const assets = ref([]);
const chartData = ref([]);
const chartContainer = ref(null);
let chartInstance = null;

// Конфигурация
const markets = [
  { id: 'crypto', label: 'Криптовалюты' },
  { id: 'stocks', label: 'Акции' }
];

const periods = [
  { days: '7', label: '7 дней' },
  { days: '30', label: '30 дней' },
  { days: '90', label: '3 месяца' },
  { days: '180', label: '6 месяцев' }
];

// Вычисляемые свойства
const chartTitle = computed(() => {
  if (!selectedAsset.value) return 'Выберите актив для отображения графика';
  return `${selectedAsset.value.name} (${selectedAsset.value.symbol})`;
});

const currentPrice = computed(() => {
  return selectedAsset.value?.price ||
    chartData.value[chartData.value.length - 1]?.close ||
    0;
});

const priceChangePercent = computed(() => {
  if (!selectedAsset.value) return 0;

  // Используем данные из актива или вычисляем из свечей
  const assetChange = selectedAsset.value.priceChangePercent ||
    selectedAsset.value.changePercentage;

  if (assetChange !== undefined) return assetChange;

  // Вычисляем из последней свечи
  const lastCandle = chartData.value[chartData.value.length - 1];
  if (!lastCandle) return 0;

  return ((lastCandle.close - lastCandle.open) / lastCandle.open) * 100;
});

const priceChangeClass = computed(() => {
  return priceChangePercent.value >= 0 ? 'positive' : 'negative';
});

const lastCandle = computed(() => {
  return chartData.value[chartData.value.length - 1] || {};
});

const dataSource = computed(() => {
  if (!selectedAsset.value) return 'Демо данные';
  return selectedAsset.value.api === 'binance' ? 'Binance API' :
    selectedAsset.value.api === 'coincap' ? 'CoinCap API' :
      selectedAsset.value.api === 'coinranking' ? 'CoinRanking API' :
        'Демо данные';
});

// Методы
const switchMarket = async (market) => {
  selectedMarket.value = market;
  selectedAsset.value = null;
  chartData.value = [];
  await loadAssets();
};

const loadAssets = async () => {
  status.value = 'loading';

  try {
    assets.value = await getMarketData(selectedMarket.value);
    status.value = 'ready';

    // Автовыбор первого актива
    if (assets.value.length > 0 && !selectedAsset.value) {
      selectedAsset.value = assets.value[0];
      await loadChartData();
    }
  } catch (error) {
    console.error('Failed to load assets:', error);
    status.value = 'error';
  }
};

const loadChartData = async () => {
  if (!selectedAsset.value) return;

  status.value = 'loading';

  try {
    const data = await getHistoricalData(
      selectedMarket.value,
      selectedAsset.value.id || selectedAsset.value.symbol,
      selectedAsset.value.symbol,
      selectedPeriod.value
    );

    chartData.value = data;
    status.value = 'ready';

    // Инициализируем график
    setTimeout(initChart, 100);
  } catch (error) {
    console.error('Failed to load chart data:', error);
    status.value = 'error';
  }
};

const selectPeriod = (period) => {
  selectedPeriod.value = period;
  if (selectedAsset.value) {
    loadChartData();
  }
};

const initChart = () => {
  if (!chartContainer.value || chartData.value.length === 0) return;

  // Очищаем предыдущий график
  if (chartInstance) {
    chartInstance.dispose();
  }

  // Создаем новый
  chartInstance = echarts.init(chartContainer.value);

  // Подготовка данных
  const dates = chartData.value.map(item => item.date);
  const ohlcData = chartData.value.map(item => [
    item.open,
    item.close,
    item.low,
    item.high
  ]);

  // Настройки графика
  const option = {
    backgroundColor: '#ffffff',
    title: {
      text: chartTitle.value,
      left: 'center',
      textStyle: {
        color: '#2c3e50',
        fontSize: 18,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#ddd',
      borderWidth: 1,
      textStyle: {
        color: '#333'
      },
      formatter: (params) => {
        const candle = chartData.value[params[0].dataIndex];
        const change = ((candle.close - candle.open) / candle.open * 100).toFixed(2);

        return `
          <div style="padding: 12px; min-width: 200px;">
            <div style="font-weight: bold; margin-bottom: 8px; color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 4px;">
              📅 ${candle.date}
            </div>
            <div style="display: grid; grid-template-columns: auto auto; gap: 6px 16px;">
              <span style="color: #7f8c8d">Открытие:</span>
              <span style="text-align: right; font-weight: 500">$${formatNumber(candle.open)}</span>

              <span style="color: #7f8c8d">Закрытие:</span>
              <span style="text-align: right; font-weight: 500; color: ${change >= 0 ? '#27ae60' : '#e74c3c'}">
                $${formatNumber(candle.close)}
              </span>

              <span style="color: #7f8c8d">Максимум:</span>
              <span style="text-align: right; color: #27ae60">$${formatNumber(candle.high)}</span>

              <span style="color: #7f8c8d">Минимум:</span>
              <span style="text-align: right; color: #e74c3c">$${formatNumber(candle.low)}</span>

              <span style="color: #7f8c8d">Изменение:</span>
              <span style="text-align: right; color: ${change >= 0 ? '#27ae60' : '#e74c3c'}">
                ${change >= 0 ? '+' : ''}${change}%
              </span>

              <span style="color: #7f8c8d">Объем:</span>
              <span style="text-align: right;">${formatVolume(candle.volume)}</span>
            </div>
          </div>
        `;
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: {
        lineStyle: {
          color: '#bdc3c7'
        }
      },
      axisLabel: {
        color: '#7f8c8d',
        formatter: (value) => {
          const date = new Date(value);
          return `${date.getDate()}/${date.getMonth() + 1}`;
        }
      },
      splitLine: {
        show: true,
        lineStyle: {
          color: '#ecf0f1',
          type: 'solid'
        }
      }
    },
    yAxis: {
      type: 'value',
      scale: true,
      axisLine: {
        lineStyle: {
          color: '#bdc3c7'
        }
      },
      axisLabel: {
        color: '#7f8c8d',
        formatter: (value) => `$${formatNumber(value)}`
      },
      splitLine: {
        lineStyle: {
          color: '#ecf0f1',
          type: 'dashed'
        }
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(255,255,255,0.1)', 'rgba(0,0,0,0.02)']
        }
      }
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100,
        minValueSpan: 10
      },
      {
        show: true,
        type: 'slider',
        top: '90%',
        height: 20,
        start: 0,
        end: 100,
        backgroundColor: '#f8f9fa',
        borderColor: '#dee2e6',
        fillerColor: 'rgba(52, 152, 219, 0.2)',
        handleStyle: {
          color: '#3498db'
        },
        textStyle: {
          color: '#7f8c8d'
        }
      }
    ],
    series: [
      {
        name: 'Свечи',
        type: 'candlestick',
        data: ohlcData,
        itemStyle: {
          color: '#27ae60',
          color0: '#e74c3c',
          borderColor: '#27ae60',
          borderColor0: '#e74c3c',
          borderWidth: 1
        },
        emphasis: {
          itemStyle: {
            borderWidth: 2,
            shadowBlur: 8,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        }
      }
    ]
  };

  chartInstance.setOption(option);

  // Ресайз
  window.addEventListener('resize', handleResize);
};

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize();
  }
};

const retryLoad = () => {
  if (selectedAsset.value) {
    loadChartData();
  } else {
    loadAssets();
  }
};

const formatDateRange = () => {
  if (chartData.value.length === 0) return '';
  const first = chartData.value[0].date;
  const last = chartData.value[chartData.value.length - 1].date;
  return `${first} - ${last}`;
};

const formatNumber = (num) => {
  if (!num && num !== 0) return '0';
  if (num >= 1000) return num.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  if (num >= 1) return num.toFixed(2);
  if (num >= 0.01) return num.toFixed(4);
  return num.toFixed(6);
};

const formatPercent = (percent) => {
  if (!percent && percent !== 0) return '0%';
  return percent >= 0 ? `+${percent.toFixed(2)}%` : `${percent.toFixed(2)}%`;
};

const formatVolume = (volume) => {
  if (!volume) return '$0';
  if (volume >= 1e9) return `$${(volume / 1e9).toFixed(2)}B`;
  if (volume >= 1e6) return `$${(volume / 1e6).toFixed(2)}M`;
  if (volume >= 1e3) return `$${(volume / 1e3).toFixed(2)}K`;
  return `$${volume.toFixed(0)}`;
};

const getChangeClass = (asset) => {
  const change = asset.priceChangePercent || asset.changePercentage;
  return change >= 0 ? 'positive' : 'negative';
};

// Хуки жизненного цикла
onMounted(() => {
  loadAssets();
});

onUnmounted(() => {
  if (chartInstance) {
    chartInstance.dispose();
  }
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.crypto-chart {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  min-height: 600px;
}

.status-message {
  padding: 60px 20px;
  text-align: center;
}

.status-message.loading {
  color: #3498db;
}

.status-message.error {
  color: #e74c3c;
  background: rgba(231, 76, 60, 0.05);
  border-radius: 8px;
  margin: 20px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.retry-btn {
  margin-top: 20px;
  padding: 10px 25px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.retry-btn:hover {
  background: #2980b9;
}

.selection-panel {
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.market-switch {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.market-btn {
  padding: 10px 25px;
  background: white;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
}

.market-btn:hover {
  border-color: #3498db;
  color: #3498db;
}

.market-btn.active {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

.asset-selection {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.asset-select {
  padding: 12px 16px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  background: white;
  font-size: 15px;
  cursor: pointer;
}

.asset-select option {
  padding: 8px;
}

.period-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.period-btn {
  padding: 8px 16px;
  background: white;
  border: 1px solid #dee2e6;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.period-btn:hover {
  border-color: #3498db;
  color: #3498db;
}

.period-btn.active {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

.chart-area {
  padding: 20px;
}

.chart-header {
  margin-bottom: 20px;
}

.chart-header h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-size: 22px;
}

.chart-stats {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  color: #7f8c8d;
  font-size: 14px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 6px;
}

.stat .label {
  font-weight: 500;
}

.stat .value {
  font-weight: 600;
  color: #2c3e50;
}

.price-stat .value {
  font-size: 16px;
}

.price-stat.positive .value {
  color: #27ae60;
}

.price-stat.negative .value {
  color: #e74c3c;
}

.change {
  font-size: 14px;
  margin-left: 4px;
}

.chart-container {
  width: 100%;
  height: 400px;
  margin: 20px 0;
}

.chart-legend {
  display: flex;
  justify-content: center;
  gap: 30px;
  flex-wrap: wrap;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
  margin-top: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #7f8c8d;
  font-size: 14px;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 3px;
}

.api-source {
  font-style: italic;
  color: #3498db;
}

.info-panel {
  padding: 20px;
  background: #f8f9fa;
  border-top: 1px solid #dee2e6;
}

.info-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.info-card {
  padding: 15px;
  background: white;
  border-radius: 8px;
  border: 1px solid #dee2e6;
  text-align: center;
  transition: transform 0.3s;
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.card-title {
  font-size: 12px;
  color: #7f8c8d;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 8px;
}

.card-value {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
}

.card-value.positive {
  color: #27ae60;
}

.card-value.negative {
  color: #e74c3c;
}

@media (max-width: 768px) {
  .market-switch {
    flex-direction: column;
  }

  .market-btn {
    width: 100%;
  }

  .period-buttons {
    justify-content: center;
  }

  .chart-stats {
    flex-direction: column;
    gap: 10px;
  }

  .info-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .chart-legend {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
}
</style>
