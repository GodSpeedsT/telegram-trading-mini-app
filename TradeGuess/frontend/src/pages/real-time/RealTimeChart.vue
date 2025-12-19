<template>
  <div class="crypto-chart">
    <!-- Статус загрузки -->
    <div v-if="status === 'loading'" class="status-message loading">
      <div class="spinner"></div>
      <p class="status-text">Загрузка данных с биржи...</p>
    </div>

    <div v-else-if="status === 'error'" class="status-message error">
      <p class="status-text">⚠️ Не удалось загрузить реальные данные. Показываем демо-график.</p>
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
            <span class="btn-icon">
              {{ market.id === 'crypto' ? '₿' : '📈' }}
            </span>
            {{ market.label }}
          </button>
        </div>

        <div class="asset-selection">
          <div class="select-wrapper">
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
            <div class="select-arrow">▼</div>
          </div>

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
              <span class="label">Текущая цена:</span>
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
            <span class="legend-color" style="background: linear-gradient(135deg, #10b981, #059669);"></span>
            <span class="legend-text">Рост (закрытие > открытие)</span>
          </div>
          <div class="legend-item">
            <span class="legend-color" style="background: linear-gradient(135deg, #ef4444, #dc2626);"></span>
            <span class="legend-text">Падение (закрытие < открытие)</span>
          </div>
          <div class="legend-item">
            <span class="api-source">
              <span class="api-icon">🌐</span>
              Источник: {{ dataSource }}
            </span>
          </div>
        </div>
      </div>

      <!-- Информационная панель -->
      <div v-if="currentAsset && chartData.length > 0" class="info-panel">
        <div class="panel-header">
          <h4>📊 Статистика по последней свече</h4>
        </div>
        <div class="info-cards">
          <div class="info-card">
            <div class="card-icon">↗️</div>
            <div class="card-title">Открытие</div>
            <div class="card-value">${{ formatNumber(lastCandle.open) }}</div>
          </div>
          <div class="info-card">
            <div class="card-icon">↘️</div>
            <div class="card-title">Закрытие</div>
            <div class="card-value" :class="priceChangeClass">
              ${{ formatNumber(lastCandle.close) }}
            </div>
          </div>
          <div class="info-card">
            <div class="card-icon">📈</div>
            <div class="card-title">Максимум</div>
            <div class="card-value positive">${{ formatNumber(lastCandle.high) }}</div>
          </div>
          <div class="info-card">
            <div class="card-icon">📉</div>
            <div class="card-title">Минимум</div>
            <div class="card-value negative">${{ formatNumber(lastCandle.low) }}</div>
          </div>
          <div class="info-card">
            <div class="card-icon">↔️</div>
            <div class="card-title">Изменение</div>
            <div class="card-value" :class="priceChangeClass">
              {{ formatPercent(priceChangePercent) }}
            </div>
          </div>
          <div class="info-card">
            <div class="card-icon">💎</div>
            <div class="card-title">Объем (24ч)</div>
            <div class="card-value volume">{{ formatVolume(lastCandle.volume) }}</div>
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
const status = ref('loading');
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
  const assetChange = selectedAsset.value.priceChangePercent ||
    selectedAsset.value.changePercentage;
  if (assetChange !== undefined) return assetChange;
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
    setTimeout(initChart, 100);
  } catch (error) {
    console.error('Failed to load TradeGame data:', error);
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
  if (chartInstance) {
    chartInstance.dispose();
  }

  chartInstance = echarts.init(chartContainer.value);
  const dates = chartData.value.map(item => item.date);
  const ohlcData = chartData.value.map(item => [
    item.open,
    item.close,
    item.low,
    item.high
  ]);

  // Темная тема для графика
  const option = {
    backgroundColor: '#1a1a2e',
    title: {
      text: chartTitle.value,
      left: 'center',
      textStyle: {
        color: '#e2e8f0',
        fontSize: 20,
        fontWeight: 'bold',
        fontFamily: 'Inter, sans-serif'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        lineStyle: {
          color: '#4f46e5',
          width: 1
        },
        crossStyle: {
          color: '#4f46e5'
        }
      },
      backgroundColor: 'rgba(26, 26, 46, 0.95)',
      borderColor: '#4f46e5',
      borderWidth: 1,
      textStyle: {
        color: '#e2e8f0',
        fontSize: 13
      },
      extraCssText: 'backdrop-filter: blur(10px); border-radius: 8px;',
      formatter: (params) => {
        const candle = chartData.value[params[0].dataIndex];
        const change = ((candle.close - candle.open) / candle.open * 100).toFixed(2);
        const color = change >= 0 ? '#10b981' : '#ef4444';

        return `
          <div style="padding: 16px; min-width: 240px;">
            <div style="font-weight: bold; margin-bottom: 12px; color: #e2e8f0; border-bottom: 1px solid #2d3748; padding-bottom: 8px;">
              📅 ${candle.date}
            </div>
            <div style="display: grid; grid-template-columns: auto auto; gap: 8px 20px;">
              <span style="color: #94a3b8">Открытие:</span>
              <span style="text-align: right; font-weight: 600; color: #e2e8f0">$${formatNumber(candle.open)}</span>

              <span style="color: #94a3b8">Закрытие:</span>
              <span style="text-align: right; font-weight: 600; color: ${color}">
                $${formatNumber(candle.close)}
              </span>

              <span style="color: #94a3b8">Максимум:</span>
              <span style="text-align: right; color: #10b981; font-weight: 600">$${formatNumber(candle.high)}</span>

              <span style="color: #94a3b8">Минимум:</span>
              <span style="text-align: right; color: #ef4444; font-weight: 600">$${formatNumber(candle.low)}</span>

              <span style="color: #94a3b8">Изменение:</span>
              <span style="text-align: right; color: ${color}; font-weight: 600">
                ${change >= 0 ? '+' : ''}${change}%
              </span>

              <span style="color: #94a3b8">Объем:</span>
              <span style="text-align: right; font-weight: 600; color: #60a5fa">${formatVolume(candle.volume)}</span>
            </div>
          </div>
        `;
      }
    },
    grid: {
      left: '3%',
      right: '3%',
      bottom: '12%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: {
        lineStyle: {
          color: '#475569'
        }
      },
      axisLabel: {
        color: '#94a3b8',
        fontSize: 12,
        fontFamily: 'Inter, sans-serif',
        formatter: (value) => {
          const date = new Date(value);
          return `${date.getDate().toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}`;
        }
      },
      splitLine: {
        show: true,
        lineStyle: {
          color: '#2d3748',
          type: 'dashed'
        }
      }
    },
    yAxis: {
      type: 'value',
      scale: true,
      axisLine: {
        lineStyle: {
          color: '#475569'
        }
      },
      axisLabel: {
        color: '#94a3b8',
        fontSize: 12,
        fontFamily: 'Inter, sans-serif',
        formatter: (value) => `$${formatNumber(value)}`
      },
      splitLine: {
        lineStyle: {
          color: '#2d3748',
          type: 'dashed'
        }
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(255,255,255,0.02)', 'rgba(0,0,0,0.05)']
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
        height: 24,
        start: 0,
        end: 100,
        backgroundColor: '#1e293b',
        borderColor: '#334155',
        fillerColor: 'rgba(79, 70, 229, 0.3)',
        handleStyle: {
          color: '#4f46e5',
          borderColor: '#4f46e5',
          borderWidth: 2
        },
        textStyle: {
          color: '#94a3b8',
          fontSize: 11
        },
        dataBackground: {
          lineStyle: {
            color: '#475569',
            width: 1
          },
          areaStyle: {
            color: '#334155'
          }
        }
      }
    ],
    series: [
      {
        name: 'Свечи',
        type: 'candlestick',
        data: ohlcData,
        itemStyle: {
          color: '#10b981',
          color0: '#ef4444',
          borderColor: '#10b981',
          borderColor0: '#ef4444',
          borderWidth: 1
        },
        emphasis: {
          itemStyle: {
            borderWidth: 2,
            borderColor: '#fbbf24',
            shadowBlur: 12,
            shadowColor: 'rgba(251, 191, 36, 0.4)'
          }
        }
      }
    ]
  };

  chartInstance.setOption(option);
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
  background: linear-gradient(135deg, #0f172a, #1e293b);
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3), 0 1px 3px rgba(255, 255, 255, 0.1);
  overflow: hidden;
  min-height: 950px;
  border: 1px solid #334155;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* Статус загрузки */
.status-message {
  padding: 80px 20px;
  text-align: center;
}

.status-message.loading {
  background: rgba(15, 23, 42, 0.8);
}

.status-message.error {
  background: rgba(220, 38, 38, 0.1);
  border-radius: 12px;
  margin: 30px;
  border: 1px solid rgba(220, 38, 38, 0.2);
}

.status-text {
  color: #e2e8f0;
  font-size: 16px;
  font-weight: 500;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(79, 70, 229, 0.2);
  border-top: 4px solid #4f46e5;
  border-radius: 50%;
  animation: spin 1.2s cubic-bezier(0.68, -0.55, 0.265, 1.55) infinite;
  margin: 0 auto 25px;
  box-shadow: 0 0 20px rgba(79, 70, 229, 0.3);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.retry-btn {
  margin-top: 25px;
  padding: 12px 30px;
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 15px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(79, 70, 229, 0.3);
}

.retry-btn:hover {
  background: linear-gradient(135deg, #4338ca, #6d28d9);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(79, 70, 229, 0.4);
}

.retry-btn:active {
  transform: translateY(0);
}

/* Панель выбора */
.selection-panel {
  padding: 24px;
  background: linear-gradient(180deg, rgba(30, 41, 59, 0.9), rgba(15, 23, 42, 0.9));
  border-bottom: 1px solid #334155;
  backdrop-filter: blur(10px);
}

.market-switch {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.market-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 14px 20px;
  background: rgba(30, 41, 59, 0.8);
  border: 1px solid #475569;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  font-size: 15px;
  color: #94a3b8;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.market-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: 0.5s;
}

.market-btn:hover::before {
  left: 100%;
}

.market-btn:hover {
  background: rgba(51, 65, 85, 0.8);
  border-color: #64748b;
  color: #e2e8f0;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.market-btn.active {
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  border-color: #8b5cf6;
  color: white;
  box-shadow: 0 8px 25px rgba(79, 70, 229, 0.3);
}

.market-btn.active:hover {
  background: linear-gradient(135deg, #4338ca, #6d28d9);
  transform: translateY(-2px);
}

.btn-icon {
  font-size: 18px;
  transition: transform 0.3s;
}

.market-btn:hover .btn-icon {
  transform: scale(1.2);
}

.asset-selection {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.select-wrapper {
  position: relative;
}

.asset-select {
  width: 100%;
  padding: 16px 20px;
  padding-right: 50px;
  background: rgba(15, 23, 42, 0.9);
  border: 1px solid #475569;
  border-radius: 10px;
  color: #e2e8f0;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  appearance: none;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.asset-select:hover {
  background: rgba(30, 41, 59, 0.9);
  border-color: #64748b;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.asset-select:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.2);
}

.select-arrow {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  color: #94a3b8;
  pointer-events: none;
  transition: transform 0.3s;
}

.select-wrapper:hover .select-arrow {
  color: #e2e8f0;
}

.asset-select option {
  background: #1e293b;
  color: #e2e8f0;
  padding: 12px;
  font-size: 14px;
}

.asset-select option:hover {
  background: #334155;
}

.period-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.period-btn {
  padding: 10px 20px;
  background: rgba(15, 23, 42, 0.8);
  border: 1px solid #475569;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  color: #94a3b8;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 90px;
  text-align: center;
}

.period-btn:hover {
  background: rgba(30, 41, 59, 0.9);
  border-color: #64748b;
  color: #e2e8f0;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
}

.period-btn.active {
  background: linear-gradient(135deg, #0ea5e9, #3b82f6);
  border-color: #60a5fa;
  color: white;
  box-shadow: 0 6px 20px rgba(14, 165, 233, 0.3);
}

.period-btn.active:hover {
  background: linear-gradient(135deg, #0284c7, #2563eb);
  transform: translateY(-2px);
}

/* Область графика */
.chart-area {
  padding: 24px;
}

.chart-header {
  margin-bottom: 24px;
}

.chart-header h3 {
  margin: 0 0 16px 0;
  color: #f1f5f9;
  font-size: 24px;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.chart-stats {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  color: #94a3b8;
  font-size: 14px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: rgba(30, 41, 59, 0.6);
  border-radius: 8px;
  border: 1px solid #334155;
}

.stat .label {
  font-weight: 500;
  color: #cbd5e1;
}

.stat .value {
  font-weight: 700;
  color: #f8fafc;
  font-size: 15px;
}

.price-stat .value {
  font-size: 17px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.price-stat.positive .value {
  color: #10b981;
  text-shadow: 0 0 10px rgba(16, 185, 129, 0.3);
}

.price-stat.negative .value {
  color: #ef4444;
  text-shadow: 0 0 10px rgba(239, 68, 68, 0.3);
}

.change {
  font-size: 13px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 4px;
  background: rgba(16, 185, 129, 0.1);
}

.price-stat.negative .change {
  background: rgba(239, 68, 68, 0.1);
}

.chart-container {
  width: 100%;
  height: 420px;
  margin: 24px 0;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #334155;
  background: #1a1a2e;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.chart-legend {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
  flex-wrap: wrap;
  padding: 16px 24px;
  background: rgba(30, 41, 59, 0.6);
  border-radius: 10px;
  margin-top: 24px;
  border: 1px solid #334155;
  backdrop-filter: blur(10px);
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.legend-color {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.legend-text {
  color: #cbd5e1;
  font-size: 14px;
  font-weight: 500;
}

.api-source {
  font-style: italic;
  color: #60a5fa;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.api-icon {
  font-size: 16px;
}

/* Информационная панель */
.info-panel {
  padding: 24px;
  background: linear-gradient(180deg, rgba(15, 23, 42, 0.9), rgba(30, 41, 59, 0.9));
  border-top: 1px solid #334155;
  backdrop-filter: blur(10px);
}

.panel-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #334155;
}

.panel-header h4 {
  margin: 0;
  color: #f1f5f9;
  font-size: 18px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.info-card {
  padding: 20px;
  background: rgba(30, 41, 59, 0.8);
  border-radius: 12px;
  border: 1px solid #334155;
  text-align: center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.info-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.05), transparent);
  transition: 0.5s;
}

.info-card:hover::before {
  left: 100%;
}

.info-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
  border-color: #475569;
  background: rgba(51, 65, 85, 0.9);
}

.card-icon {
  font-size: 24px;
  margin-bottom: 12px;
  opacity: 0.9;
}

.card-title {
  font-size: 12px;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 10px;
  font-weight: 600;
}

.card-value {
  font-size: 20px;
  font-weight: 700;
  color: #f8fafc;
}

.card-value.positive {
  color: #10b981;
  text-shadow: 0 0 8px rgba(16, 185, 129, 0.3);
}

.card-value.negative {
  color: #ef4444;
  text-shadow: 0 0 8px rgba(239, 68, 68, 0.3);
}

.card-value.volume {
  color: #60a5fa;
}

/* Адаптивность */
@media (max-width: 768px) {
  .crypto-chart {
    border-radius: 12px;
  }

  .market-switch {
    flex-direction: column;
  }

  .market-btn {
    width: 100%;
  }

  .period-buttons {
    justify-content: center;
  }

  .period-btn {
    min-width: 80px;
    padding: 8px 16px;
    font-size: 13px;
  }

  .chart-stats {
    flex-direction: column;
    gap: 12px;
  }

  .stat {
    width: 100%;
    justify-content: space-between;
  }

  .info-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .chart-legend {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .chart-container {
    height: 350px;
  }

  .chart-header h3 {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .info-cards {
    grid-template-columns: 1fr;
  }

  .chart-container {
    height: 300px;
  }

  .selection-panel {
    padding: 16px;
  }

  .chart-area {
    padding: 16px;
  }

  .info-panel {
    padding: 16px;
  }
}
</style>