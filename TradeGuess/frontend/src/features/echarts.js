import { createApp } from 'vue';
import ECharts from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { CandlestickChart, LineChart, BarChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  LegendComponent,
  AxisPointerComponent,
  MarkLineComponent,
  VisualMapComponent
} from 'echarts/components';

// Регистрация необходимых компонентов ECharts
use([
  CanvasRenderer,
  CandlestickChart,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  LegendComponent,
  AxisPointerComponent,
  MarkLineComponent,
  VisualMapComponent
]);

export default {
  install(app) {
    // Регистрируем компонент глобально
    app.component('v-TradeGame', ECharts);
  }
};
