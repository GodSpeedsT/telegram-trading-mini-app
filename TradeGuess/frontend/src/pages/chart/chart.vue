<script setup>
import { ref, onMounted, computed } from 'vue';
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { CandlestickChart, LineChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  VisualMapComponent,
  LegendComponent,
  AxisPointerComponent
} from 'echarts/components';

// Регистрация компонентов ECharts
use([
  CanvasRenderer,
  CandlestickChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  VisualMapComponent,
  LegendComponent,
  AxisPointerComponent
]);

// Тема графика
const chartTheme = ref('dark');

// Реактивные данные
const candles = ref([]);
const chartOption = ref(null);
const displayedCandles = ref(0);

// Компьютед свойства
const lastPrice = computed(() => {
  if (candles.value.length === 0) return 'N/A';
  const lastCandle = candles.value[candles.value.length - 1];
  return lastCandle.close.toFixed(2);
});

// Генерация случайных данных для свечей
const generateRandomCandles = (count = 150) => {
  const generatedCandles = [];
  let basePrice = 100 + Math.random() * 50; // Начальная цена от 100 до 150
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - count);

  for (let i = 0; i < count; i++) {
    const date = new Date(startDate);
    date.setDate(date.getDate() + i);

    // Волатильность (сильнее в понедельник)
    const dayOfWeek = date.getDay();
    const volatility = dayOfWeek === 1 ? 0.03 : 0.015; // Понедельник более волатильный

    // Генерация цен
    const open = i === 0 ? basePrice : generatedCandles[i-1].close;
    const change = (Math.random() - 0.5) * 2 * volatility * open;
    const close = open + change;
    const high = Math.max(open, close) + Math.random() * volatility * open;
    const low = Math.min(open, close) - Math.random() * volatility * open;
    const volume = Math.floor(Math.random() * 1000000) + 100000;

    generatedCandles.push({
      date: date.toISOString().split('T')[0],
      timestamp: date.getTime(),
      open: Number(open.toFixed(2)),
      close: Number(close.toFixed(2)),
      high: Number(high.toFixed(2)),
      low: Number(low.toFixed(2)),
      volume: volume,
      change: ((close - open) / open * 100).toFixed(2)
    });

    basePrice = close;
  }

  return generatedCandles;
};

// Форматирование даты
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('ru-RU');
};

// Инициализация графика с новыми данными
const initChartWithNewData = () => {
  // Генерируем от 100 до 150 свечей случайным образом
  const candleCount = Math.floor(Math.random() * 51) + 100; // 100-150 свечей
  candles.value = generateRandomCandles(candleCount);
  displayedCandles.value = Math.min(100, candleCount); // Показываем первые 100 или меньше, если свечей меньше

  // Подготовка данных для ECharts
  const xAxisData = candles.value.map(c => c.date);
  const seriesData = candles.value.map(c => [c.open, c.close, c.low, c.high]);

  // Конфигурация графика
  chartOption.value = {
    backgroundColor: '#1e1e1e',
    title: {
      text: `Свечной график (${candleCount} свечей)`,
      left: 'center',
      textStyle: {
        color: '#fff',
        fontSize: 18
      },
      subtext: `Сгенерировано: ${new Date().toLocaleString('ru-RU')}`,
      subtextStyle: {
        color: '#aaa',
        fontSize: 12
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
      formatter: function(params) {
        const candle = candles.value[params[0].dataIndex];
        return `
          <div style="padding: 5px;">
            <strong>${candle.date}</strong><br/>
            Открытие: <b>${candle.open}</b><br/>
            Закрытие: <b>${candle.close}</b> (${candle.change}%)<br/>
            Максимум: ${candle.high}<br/>
            Минимум: ${candle.low}<br/>
            Объем: ${candle.volume.toLocaleString()}
          </div>
        `;
      }
    },
    legend: {
      data: ['Свечи', 'Объем'],
      textStyle: {
        color: '#fff'
      },
      top: 40
    },
    grid: [
      {
        left: '10%',
        right: '8%',
        top: '18%',
        height: '60%'
      },
      {
        left: '10%',
        right: '8%',
        top: '80%',
        height: '15%'
      }
    ],
    xAxis: [
      {
        type: 'category',
        data: xAxisData,
        scale: true,
        boundaryGap: false,
        axisLine: {
          onZero: false,
          lineStyle: { color: '#666' }
        },
        axisTick: {
          show: false
        },
        axisLabel: {
          color: '#aaa',
          formatter: function(value) {
            const date = new Date(value);
            return `${date.getDate()}.${date.getMonth()+1}`;
          }
        },
        splitLine: { show: false },
        min: 0,
        max: displayedCandles.value - 1
      },
      {
        type: 'category',
        gridIndex: 1,
        data: xAxisData,
        scale: true,
        boundaryGap: false,
        axisLine: { onZero: false },
        axisTick: { show: false },
        axisLabel: { show: false },
        splitLine: { show: false },
        min: 0,
        max: displayedCandles.value - 1
      }
    ],
    yAxis: [
      {
        scale: true,
        splitArea: {
          show: true,
          areaStyle: {
            color: ['rgba(255,255,255,0.02)', 'rgba(255,255,255,0.05)']
          }
        },
        axisLine: {
          lineStyle: { color: '#666' }
        },
        axisLabel: {
          color: '#aaa',
          inside: true
        },
        splitLine: {
          lineStyle: {
            type: 'dashed',
            color: '#444'
          }
        }
      },
      {
        scale: true,
        gridIndex: 1,
        splitNumber: 2,
        axisLabel: { show: false },
        axisLine: { show: false },
        axisTick: { show: false },
        splitLine: { show: false }
      }
    ],
    dataZoom: [
      {
        type: 'inside',
        xAxisIndex: [0, 1],
        start: 0,
        end: (displayedCandles.value / candles.value.length) * 100,
        minValueSpan: 10, // Минимум 10 свечей видно
        maxValueSpan: displayedCandles.value
      },
      {
        show: true,
        type: 'slider',
        xAxisIndex: [0, 1],
        top: '95%',
        start: 0,
        end: (displayedCandles.value / candles.value.length) * 100,
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
    visualMap: {
      show: false,
      seriesIndex: 0,
      dimension: 2,
      pieces: [
        {
          value: 1,
          color: '#ef5350' // красный для падения
        },
        {
          value: -1,
          color: '#26a69a' // зеленый для роста
        }
      ]
    },
    series: [
      {
        name: 'Свечи',
        type: 'candlestick',
        data: seriesData,
        itemStyle: {
          color: '#26a69a', // цвет для роста
          color0: '#ef5350', // цвет для падения
          borderColor: '#26a69a',
          borderColor0: '#ef5350',
          borderWidth: 1
        },
        emphasis: {
          itemStyle: {
            borderWidth: 2,
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        }
      },
      {
        name: 'Объем',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: candles.value.map((c, index) => ({
          value: c.volume,
          itemStyle: {
            color: seriesData[index][1] >= seriesData[index][0] ? '#26a69a' : '#ef5350'
          }
        })),
        itemStyle: {
          opacity: 0.7
        }
      }
    ]
  };

  console.log(`Сгенерировано ${candleCount} свечей. Показано ${displayedCandles.value}`);
};

// Обновление графика
const refreshChart = () => {
  initChartWithNewData();
};

// Инициализация при монтировании
onMounted(() => {
  initChartWithNewData();

  // Можно также добавить автообновление по таймеру
  // setInterval(() => refreshChart(), 30000); // каждые 30 секунд
});
</script>

<template>
  <div class="candlestick-container">
    <div class="controls">
      <button @click="refreshChart">Обновить график</button>
      <span class="info">Показано свечей: {{ displayedCandles }} из {{ candles.length }}</span>
    </div>

    <div class="chart-wrapper">
      <VChart
        v-if="chartOption"
        :option="chartOption"
        :autoresize="true"
        class="chart"
        :theme="chartTheme"
      />
    </div>

    <div class="data-info">
      <p>Период: {{ formatDate(candles[0]?.date) }} - {{ formatDate(candles[displayedCandles-1]?.date) }}</p>
      <p>Последняя цена: {{ lastPrice }}</p>
    </div>
  </div>
</template>

<style scoped>
.candlestick-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #1e1e1e;
  color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.controls {
  padding: 15px 20px;
  background-color: #252525;
  border-bottom: 1px solid #333;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.controls button {
  padding: 8px 20px;
  background-color: #43a047;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.controls button:hover {
  background-color: #388e3c;
}

.controls .info {
  color: #aaa;
  font-size: 14px;
}

.chart-wrapper {
  flex: 1;
  padding: 10px;
}

.chart {
  width: 100%;
  height: 100%;
  min-height: 500px;
}

.data-info {
  padding: 15px 20px;
  background-color: #252525;
  border-top: 1px solid #333;
  font-size: 14px;
}

.data-info p {
  margin: 5px 0;
  color: #ddd;
}

/* Адаптивность */
@media (max-width: 768px) {
  .controls {
    flex-direction: column;
    gap: 10px;
  }

  .chart {
    min-height: 400px;
  }
}
</style>
