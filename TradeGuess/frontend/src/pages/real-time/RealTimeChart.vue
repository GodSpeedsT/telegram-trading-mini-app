<template>
  <div class="flex flex-col w-full h-screen bg-zinc-950 text-white font-sans select-none overflow-hidden pb-[90px]">
    <div v-if="status === 'loading'" class="flex flex-col flex-1 items-center justify-center gap-5 text-center px-4">
      <div class="border-4 border-zinc-800 border-t-emerald-500 rounded-full animate-spin w-10 h-10 md:w-12 md:h-12"></div>
      <p class="text-zinc-500 font-black text-[10px] md:text-xs uppercase tracking-widest">Синхронизация...</p>
    </div>
    <div v-else class="flex flex-col flex-1">
      <div class="flex flex-col bg-zinc-900/40 border-b border-zinc-800 backdrop-blur-xl shrink-0 p-2 sm:p-3 md:p-4 lg:p-5 gap-2 sm:gap-3 md:gap-4">
        <div class="flex items-center justify-between gap-2">
          <div class="flex bg-zinc-950 rounded-xl border border-zinc-800 p-0.5 sm:p-1">
            <button v-for="m in markets" :key="m.id" @click="switchMarket(m.id)" class="rounded-lg font-black uppercase transition-all duration-300 px-3 sm:px-4 md:px-5 py-1.5 sm:py-2 text-[10px] md:text-xs" :class="selectedMarket === m.id ? 'bg-zinc-800 text-white shadow-lg border border-zinc-700/50' : 'text-zinc-500 hover:text-zinc-400'">
              {{ m.label }}
            </button>
          </div>
          <div class="flex bg-zinc-950 rounded-xl border border-zinc-800 overflow-x-auto no-scrollbar p-0.5 sm:p-1">
            <button v-for="p in periods" :key="p.days" @click="selectPeriod(p.days)" class="rounded-lg font-black transition-all duration-300 whitespace-nowrap px-2.5 sm:px-3 md:px-4 py-1.5 sm:py-2 text-[9px] sm:text-[10px] md:text-xs" :class="selectedPeriod === p.days ? 'bg-emerald-500 text-black shadow-lg' : 'text-zinc-500 hover:text-zinc-400'">
              {{ p.label }}
            </button>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-2 sm:gap-3 md:gap-4">
          <div class="relative group">
            <div class="absolute top-1/2 -translate-y-1/2 font-black text-emerald-500 uppercase z-10 tracking-widest left-3 md:left-4 text-[8px] md:text-[9px]">Main</div>
            <select v-model="selectedAsset" @change="loadChartData" class="w-full bg-zinc-950 border border-zinc-800 rounded-xl text-white appearance-none outline-none focus:border-emerald-500 transition-all font-bold py-2 md:py-3 pl-11 md:pl-14 pr-8 text-[11px] md:text-sm cursor-pointer">
              <option v-for="a in assets" :key="a.symbol" :value="a">{{ a.symbol }}</option>
            </select>
            <div class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-zinc-600 text-[10px]">▼</div>
          </div>
          <div class="relative group">
            <div class="absolute top-1/2 -translate-y-1/2 font-black text-purple-400 uppercase z-10 tracking-widest left-3 md:left-4 text-[8px] md:text-[9px]">Corr</div>
            <select v-model="compareAsset" @change="loadChartData" class="w-full bg-zinc-950 border border-zinc-800 rounded-xl text-white appearance-none outline-none focus:border-purple-500 transition-all font-bold py-2 md:py-3 pl-11 md:pl-14 pr-8 text-[11px] md:text-sm cursor-pointer">
              <option :value="null">НЕТ</option>
              <option v-for="a in assets" :key="'comp-'+a.symbol" :value="a">{{ a.symbol }}</option>
            </select>
            <div class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-zinc-600 text-[10px]">▼</div>
          </div>
        </div>
      </div>
      <div class="flex-1 flex flex-col p-3 md:p-5 lg:p-6 gap-3 md:gap-4 overflow-hidden bg-zinc-950">
        <div class="flex items-end justify-between shrink-0">
          <div class="flex items-center gap-3 sm:gap-4">
            <div class="relative flex items-center justify-center bg-zinc-900 rounded-2xl border border-zinc-800 p-2 sm:p-3 aspect-square h-12 sm:h-16 md:h-20 lg:h-24 shadow-lg overflow-hidden">
              <div class="absolute inset-0 border-[3px] sm:border-[4px] opacity-20" :class="fearGreedColor"></div>
              <div class="flex flex-col items-center justify-center z-10 px-1 text-center">
                <span class="text-[8px] sm:text-[10px] font-black uppercase text-zinc-500 leading-none pb-1">Index</span>
                <span class="text-xs sm:text-lg md:text-xl font-black font-mono leading-none" :class="fearGreedTextColor">{{ fearGreedIndex }}</span>
              </div>
            </div>
            <div class="flex flex-col gap-0.5">
              <h2 class="font-black tracking-tighter text-xl md:text-3xl lg:text-4xl uppercase leading-none text-white">{{ chartTitle }}</h2>
              <div class="flex items-center gap-2">
                <span class="font-mono font-bold text-lg md:text-2xl" :class="priceChangeClass">${{ formatNumber(currentPrice) }}</span>
                <span class="px-1.5 py-0.5 rounded-md bg-zinc-900 text-[9px] md:text-[10px] font-black border border-zinc-800" :class="priceChangeClass">
                  {{ formatPercent(priceChangePercent) }}
                </span>
              </div>
            </div>
          </div>
          <div v-if="compareAsset" class="flex flex-col items-end gap-0.5 text-right">
            <span class="text-[8px] md:text-[9px] font-black text-purple-400 uppercase tracking-widest leading-none">Correlation</span>
            <span class="font-mono font-bold text-sm md:text-lg text-purple-300 leading-none">{{ compareAsset.symbol }}</span>
          </div>
        </div>
        <div class="flex-1 w-full bg-[#131722] border border-zinc-800 rounded-[24px] md:rounded-[40px] overflow-hidden shadow-2xl relative cursor-crosshair group">
          <div ref="chartRef" class="w-full h-full absolute inset-0"></div>
          <div class="absolute top-3 right-3 pointer-events-none bg-black/40 backdrop-blur-md px-2 py-1 rounded-lg border border-white/5 text-[7px] sm:text-[9px] font-bold text-zinc-400 uppercase tracking-widest opacity-0 group-hover:opacity-100 transition-opacity duration-300">Tap to Focus</div>
        </div>
        <div class="grid grid-cols-3 gap-2 sm:gap-4 shrink-0 font-mono">
          <div v-for="s in statsRows" :key="s.l" class="bg-zinc-900/40 rounded-xl border border-zinc-800/50 p-2 md:p-4 flex flex-col gap-0.5">
            <span class="font-black text-zinc-600 uppercase text-[7px] md:text-[9px] tracking-widest leading-none pb-0.5">{{ s.l }}</span>
            <span class="font-bold text-[10px] md:text-sm lg:text-base truncate leading-none" :class="s.c">
              {{ s.isVol ? formatVolume(s.v) : '$' + formatNumber(s.v) }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';
import * as echarts from 'echarts';
// @ts-expect-error: service
import { getMarketData, getHistoricalData } from '@/features/apiService';

interface Asset { symbol: string; name: string; price?: number; id?: string; }
interface Candle { date: string; open: number; high: number; low: number; close: number; volume: number; }

const status = ref<'loading' | 'ready' | 'error'>('loading');
const selectedMarket = ref('crypto');
const selectedAsset = ref<Asset | null>(null);
const compareAsset = ref<Asset | null>(null);
const selectedPeriod = ref('30');
const assets = ref<Asset[]>([]);
const mainData = ref<Candle[]>([]);
const compData = ref<Candle[]>([]);
const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const markets = [{ id: 'crypto', label: 'КРИПТО' }, { id: 'stocks', label: 'АКЦИИ' }];
const periods = [
  { days: '7', label: '7 ДНЕЙ' },
  { days: '30', label: '30 ДНЕЙ' },
  { days: '90', label: '90 ДНЕЙ' }
];

const fearGreedIndex = computed(() => {
  if (!mainData.value.length) return 50;
  const change = priceChangePercent.value;
  const val = 50 + (change * 5);
  return Math.min(Math.max(Math.round(val), 5), 95);
});

const fearGreedColor = computed(() => {
  const v = fearGreedIndex.value;
  if (v < 30) return 'border-rose-500';
  if (v < 45) return 'border-orange-500';
  if (v < 55) return 'border-zinc-500';
  if (v < 75) return 'border-emerald-400';
  return 'border-emerald-500';
});

const fearGreedTextColor = computed(() => {
  const v = fearGreedIndex.value;
  if (v < 30) return 'text-rose-500';
  if (v < 45) return 'text-orange-500';
  if (v < 55) return 'text-zinc-400';
  if (v < 75) return 'text-emerald-400';
  return 'text-emerald-500';
});

const chartTitle = computed(() => selectedAsset.value?.symbol || 'LIVE');
const currentPrice = computed(() => mainData.value.length ? mainData.value[mainData.value.length - 1].close : 0);
const lastCandle = computed(() => mainData.value.length ? mainData.value[mainData.value.length - 1] : { high: 0, low: 0, volume: 0 });

const priceChangePercent = computed(() => {
  if (!mainData.value.length) return 0;
  const l = mainData.value[mainData.value.length - 1];
  return ((l.close - l.open) / l.open) * 100;
});

const priceChangeClass = computed(() => priceChangePercent.value >= 0 ? 'text-emerald-500' : 'text-rose-500');

const statsRows = computed(() => [
  { l: 'Max 24h', v: lastCandle.value.high, c: 'text-emerald-400' },
  { l: 'Min 24h', v: lastCandle.value.low, c: 'text-rose-400' },
  { l: 'Volume', v: lastCandle.value.volume, c: 'text-blue-400', isVol: true }
]);

const switchMarket = async (m: string) => {
  selectedMarket.value = m;
  selectedAsset.value = null;
  compareAsset.value = null;
  await loadAssets();
};

const loadAssets = async () => {
  status.value = 'loading';
  try {
    const data = await getMarketData(selectedMarket.value);
    assets.value = data;
    if (data.length) selectedAsset.value = data[0];
    await loadChartData();
  } catch { status.value = 'error'; }
};

const loadChartData = async () => {
  if (!selectedAsset.value) return;
  status.value = 'loading';
  try {
    const mainPromise = getHistoricalData(selectedMarket.value, selectedAsset.value.id || selectedAsset.value.symbol, selectedAsset.value.symbol, selectedPeriod.value);
    if (compareAsset.value) {
      const [main, comp] = await Promise.all([
        mainPromise,
        getHistoricalData(selectedMarket.value, compareAsset.value.id || compareAsset.value.symbol, compareAsset.value.symbol, selectedPeriod.value)
      ]);
      mainData.value = main;
      compData.value = comp;
    } else {
      mainData.value = await mainPromise;
      compData.value = [];
    }
    status.value = 'ready';
    await nextTick();
    initChart();
  } catch { status.value = 'error'; }
};

const initChart = () => {
  if (!chartRef.value || !mainData.value.length) return;
  if (chartInstance) {
    chartInstance.off('click');
    chartInstance.dispose();
  }
  chartInstance = echarts.init(chartRef.value);

  const series: echarts.SeriesOption[] = [
    {
      type: 'candlestick',
      data: mainData.value.map(i => [i.open, i.close, i.low, i.high]),
      itemStyle: { color: '#10b981', color0: '#f43f5e', borderColor: '#10b981', borderColor0: '#f43f5e' },
      animationDelay: (idx: number) => idx * 4
    }
  ];

  if (compareAsset.value && compData.value.length) {
    series.push({
      type: 'line', yAxisIndex: 1, data: compData.value.map(i => i.close),
      smooth: true, showSymbol: false, lineStyle: { width: 2, color: '#a855f7', opacity: 0.8 },
      animationDelay: (idx: number) => idx * 4
    });
  }

  const option: echarts.EChartsOption = {
    backgroundColor: 'transparent',
    animation: true,
    animationDuration: 800,
    grid: { left: 10, right: 60, bottom: 25, top: 15, containLabel: false },
    dataZoom: [{ type: 'inside', xAxisIndex: 0, zoomOnMouseWheel: true, moveOnMouseMove: true }],
    tooltip: {
      trigger: 'axis', backgroundColor: '#18181b', borderColor: '#27272a', textStyle: { color: '#fff', fontSize: 10 },
      formatter: (params: unknown) => {
        const list = params as Array<{ seriesName: string, data: number | number[], dataIndex: number }>;
        if (!list.length) return '';
        const idx = list[0].dataIndex;
        const c = mainData.value[idx];
        let res = `<div style="font-weight:bold;padding-bottom:4px;border-bottom:1px solid #333">${c.date}</div>`;
        res += `<div style="padding-top:4px">O: ${c.open.toFixed(2)}</div>`;
        res += `<div>C: ${c.close.toFixed(2)}</div>`;
        if (compareAsset.value && list[1]) {
          res += `<div style="color:#a855f7;padding-top:4px;border-top:1px solid #333">${compareAsset.value.symbol}: ${(list[1].data as number).toFixed(2)}</div>`;
        }
        return res;
      }
    },
    xAxis: {
      type: 'category', data: mainData.value.map(i => i.date), axisLine: { show: false },
      axisLabel: { color: '#52525b', fontSize: 9, formatter: (v: string) => v.split('-').pop() || '' },
      splitLine: { show: true, lineStyle: { color: '#1e1e24' } }
    },
    yAxis: [
      { scale: true, position: 'right', axisLabel: { color: '#52525b', fontSize: 9 }, splitLine: { lineStyle: { color: '#1e1e24' } } },
      { scale: true, position: 'left', show: !!compareAsset.value, axisLabel: { color: '#a855f7', fontSize: 9 }, splitLine: { show: false } }
    ],
    series
  };

  chartInstance.setOption(option, true);

  chartInstance.on('click', (params) => {
    if (params.dataIndex !== undefined) {
      const idx = params.dataIndex;
      const total = mainData.value.length;
      const range = 15;
      const start = Math.max(0, ((idx - range/2) / total) * 100);
      const end = Math.min(100, ((idx + range/2) / total) * 100);
      chartInstance?.dispatchAction({ type: 'dataZoom', start, end });
    }
  });
};

const selectPeriod = (d: string) => { selectedPeriod.value = d; loadChartData(); };
const formatNumber = (n: number | string): string => {
  const v = typeof n === 'string' ? parseFloat(n) : n;
  return isNaN(v) ? '0.00' : v.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
};
const formatPercent = (p: number): string => `${p >= 0 ? '+' : ''}${p.toFixed(2)}%`;
const formatVolume = (v: number | string): string => {
  const val = typeof v === 'string' ? parseFloat(v) : v;
  if (val >= 1e9) return (val/1e9).toFixed(1)+'B';
  if (val >= 1e6) return (val/1e6).toFixed(1)+'M';
  return (val/1e3).toFixed(1)+'K';
};

// ИСПРАВЛЕНО: Вынос функции ресайза для корректного удаления слушателя
const handleResize = () => chartInstance?.resize();

onMounted(() => {
  loadAssets();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  if (chartInstance) {
    chartInstance.off('click');
    chartInstance.dispose();
  }
});
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
select option { background-color: #09090b; color: white; }
.cursor-crosshair { cursor: crosshair; }
</style>
