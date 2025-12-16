<template>
  <div class="historical-chart-container">
    <!-- Упрощенная панель управления -->
    <div class="control-panel">
      <div class="asset-selector">
        <select v-model="selectedCategory" @change="onCategoryChange">
          <option value="crypto">Криптовалюты</option>
          <option value="stocks">Акции</option>
          <option value="forex">Forex</option>
        </select>

        <select v-model="selectedAsset" @change="loadAssetData">
          <option v-for="asset in availableAssets"
                  :key="asset.assetKey"
                  :value="asset.assetKey">
            {{ asset.name }}
          </option>
        </select>

        <button @click="loadRandomAsset" class="random-btn">
          Случайный актив
        </button>
      </div>
    </div>

    <!-- График -->
    <div class="chart-wrapper" v-if="chartOption">
      <v-chart
        :option="chartOption"
        :autoresize="true"
        class="chart"
        theme="dark"
      />
    </div>

    <!-- Информация -->
    <div class="info-panel" v-if="currentAsset">
      <h3>{{ currentAsset.name }} ({{ currentAsset.symbol }})</h3>
      <p>Период: {{ currentAsset.period }}</p>
      <p>Тип: {{ categoryLabels[selectedCategory] }}</p>
      <p>Волатильность: {{ volatilityLabels[currentAsset.volatility] }}</p>
      <p>Свечей показано: {{ displayedCandles }} из {{ currentAsset.data.length }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import {
  GridComponent,
  TooltipComponent,
  TitleComponent,
  DataZoomComponent
} from 'echarts/components';
import { CandlestickChart } from 'echarts/charts';

import { marketModel } from '../../../entities/market-data/model/market-model';
import type {
  AssetData,
  AssetWithMeta,
  AssetInfo,
  HistoricalDatasets,
  VolatilityLevel
} from '../../../shared/types/market';

// Инициализация ECharts
use([
  CanvasRenderer,
  GridComponent,
  TooltipComponent,
  TitleComponent,
  DataZoomComponent,
  CandlestickChart
]);

// Реактивные данные
const selectedCategory = ref<keyof HistoricalDatasets>('crypto');
const selectedAsset = ref<string>('bitcoin');
const displayedCandles = ref<number>(100);
const chartOption = ref<any>(null);
const currentAsset = ref<AssetData | null>(null);

// Лейблы
const categoryLabels = {
  crypto: 'Криптовалюта',
  stocks: 'Акции',
  forex: 'Forex'
} as const;

const volatilityLabels: Record<VolatilityLevel, string> = {
  'low': 'Низкая',
  'medium': 'Средняя',
  'high': 'Высокая',
  'very high': 'Очень высокая'
};

// Доступные активы
const availableAssets = computed<AssetWithMeta[]>(() => {
  return marketModel.getAllAssets()
    .filter(asset => asset.category === selectedCategory.value);
});

// Загрузка данных актива
const loadAssetData = () => {
  const asset = marketModel.getAsset(selectedCategory.value, selectedAsset.value);
  if (asset) {
    currentAsset.value = asset;
    initializeChart();
  }
};

// Инициализация графика
const initializeChart = () => {
  if (!currentAsset.value?.data?.length) return;

  const chartData = marketModel.getChartData(
    selectedCategory.value,
    selectedAsset.value,
    displayedCandles.value
  );

  if (!chartData) return;

  const xAxisData = chartData.data.map(candle => candle.date);
  const seriesData = chartData.data.map(candle => [
    candle.open,
    candle.close,
    candle.low,
    candle.high
  ]);

  chartOption.value = {
    backgroundColor: '#1e1e1e',
    title: {
      text: currentAsset.value.name,
      left: 'center',
      textStyle: {
        color: '#fff',
        fontSize: 18
      },
      subtext: `Период: ${currentAsset.value.period}`,
      subtextStyle: {
        color: '#aaa',
        fontSize: 12
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      },
      formatter: (params: any[]) => {
        const candleIndex = params[0].dataIndex;
        const candle = chartData.data[candleIndex];
        const change = ((candle.close - candle.open) / candle.open * 100).toFixed(2);

        return `
          <div style="padding: 10px;">
            <strong>${candle.date}</strong><br/>
            Открытие: ${formatPrice(candle.open)}<br/>
            Закрытие: ${formatPrice(candle.close)} (${change}%)<br/>
            Максимум: ${formatPrice(candle.high)}<br/>
            Минимум: ${formatPrice(candle.low)}<br/>
            Объем: ${formatVolume(candle.volume)}
          </div>
        `;
      }
    },
    grid: {
      left: '10%',
      right: '8%',
      bottom: '15%',
      top: '15%'
    },
    xAxis: {
      type: 'category',
      data: xAxisData,
      axisLine: {
        lineStyle: { color: '#666' }
      },
      axisLabel: {
        color: '#aaa',
        formatter: (value: string) => {
          const date = new Date(value);
          return `${date.getDate()}.${date.getMonth() + 1}`;
        }
      },
      splitLine: {
        show: true,
        lineStyle: { color: '#444' }
      }
    },
    yAxis: {
      scale: true,
      axisLine: {
        lineStyle: { color: '#666' }
      },
      axisLabel: {
        color: '#aaa',
        formatter: (value: number) => formatPrice(value)
      },
      splitLine: {
        lineStyle: {
          color: '#444',
          type: 'dashed'
        }
      }
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100,
        minValueSpan: 20
      },
      {
        show: true,
        type: 'slider',
        top: '90%',
        start: 0,
        end: 100,
        backgroundColor: '#333',
        borderColor: '#555',
        fillerColor: 'rgba(67, 160, 71, 0.2)',
        handleStyle: {
          color: '#43a047'
        },
        textStyle: {
          color: '#aaa'
        }
      }
    ],
    series: [
      {
        name: 'Свечи',
        type: 'candlestick',
        data: seriesData,
        itemStyle: {
          color: '#26a69a',
          color0: '#ef5350',
          borderColor: '#26a69a',
          borderColor0: '#ef5350'
        }
      }
    ]
  };
};

// Форматирование
const formatPrice = (price: number): string => {
  if (!price) return 'N/A';
  return selectedCategory.value === 'forex' ?
    price.toFixed(4) : price.toFixed(2);
};

const formatVolume = (volume: number): string => {
  if (!volume) return 'N/A';
  if (volume >= 1e9) return `$${(volume / 1e9).toFixed(2)}B`;
  if (volume >= 1e6) return `$${(volume / 1e6).toFixed(2)}M`;
  return `$${(volume / 1e3).toFixed(2)}K`;
};

// Обработчики
const onCategoryChange = () => {
  const assets = availableAssets.value;
  if (assets.length > 0) {
    selectedAsset.value = assets[0].assetKey;
    loadAssetData();
  }
};

const loadRandomAsset = () => {
  const random = marketModel.getRandomAsset();
  selectedCategory.value = random.category;
  selectedAsset.value = random.assetKey;
  loadAssetData();
};

// Инициализация
onMounted(() => {
  loadAssetData();
});
</script>

<style scoped>
.historical-chart-container {
  width: 100%;
  height: 100%;
  background-color: #1e1e1e;
  color: white;
  border-radius: 8px;
  overflow: hidden;
  padding: 20px;
}

.control-panel {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #252525;
  border-radius: 6px;
}

.asset-selector {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

select {
  padding: 8px 16px;
  background-color: #333;
  color: white;
  border: 1px solid #555;
  border-radius: 4px;
  min-width: 150px;
}

button {
  padding: 8px 20px;
  background-color: #43a047;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #388e3c;
}

.chart-wrapper {
  width: 100%;
  height: 500px;
  margin: 20px 0;
}

.chart {
  width: 100%;
  height: 100%;
}

.info-panel {
  padding: 15px;
  background-color: #252525;
  border-radius: 6px;
  margin-top: 20px;
}

.info-panel h3 {
  margin: 0 0 10px 0;
  color: #fff;
}

.info-panel p {
  margin: 5px 0;
  color: #aaa;
}
</style>
