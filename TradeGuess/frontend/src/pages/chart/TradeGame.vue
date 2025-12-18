<template>
  <div class="w-full h-full min-h-screen bg-zinc-950 text-white flex flex-col font-sans select-none overflow-hidden relative pb-[90px]">
    <div class="absolute inset-0 w-full h-full bg-puzzle-pattern opacity-10 pointer-events-none"></div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-zinc-950/60 to-zinc-950 pointer-events-none"></div>
    <div class="pt-5 pb-2 px-4 z-20 shrink-0 flex flex-col gap-3">
      <div class="flex justify-between items-center">
        <div v-if="currentAsset" class="flex items-center gap-4">
          <div class="w-14 h-14 rounded-2xl bg-zinc-800 flex items-center justify-center border-2 border-zinc-700 shadow-xl">
            <span class="text-lg font-black text-zinc-300">{{ currentAsset.symbol[0] }}</span>
          </div>
          <div class="flex flex-col gap-1.5">
            <h1 class="text-xl font-black tracking-tight text-white leading-tight">{{ currentAsset.name }}</h1>
            <span class="text-xs font-bold text-zinc-400 bg-zinc-900/80 px-2.5 py-1 rounded-lg border border-zinc-800 w-fit">
              {{ currentAsset.symbol }}
            </span>
          </div>
        </div>
        <div class="flex flex-col items-end gap-2">
          <div class="flex items-center gap-3 bg-zinc-900/90 border-2 border-yellow-500/30 px-4 py-2.5 rounded-2xl shadow-xl shadow-yellow-900/20">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-6 h-6 text-yellow-400">
              <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z" clip-rule="evenodd" />
            </svg>
            <span class="font-mono text-2xl font-bold text-yellow-100">{{ score }}</span>
          </div>
          <div v-if="streak > 1" class="flex items-center gap-1.5 bg-gradient-to-r from-orange-500/20 to-red-500/20 px-3 py-1.5 rounded-full border border-orange-500/30 animate-pulse">
            <span class="text-lg">🔥</span>
            <span class="text-sm font-black text-orange-300">x{{ streak }}</span>
          </div>
        </div>
      </div>
      <div class="flex gap-2 bg-zinc-900/50 p-1.5 rounded-2xl border border-white/5 mt-1">
        <button @click="setGameMode('candle')" class="flex-1 flex items-center justify-center gap-3 py-3 rounded-xl text-sm font-black uppercase transition-all duration-300" :class="gameMode === 'candle' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'" :disabled="gameState !== 'playing'">
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M13 10V3L4 14h7v7l9-11h-7z" /></svg>
          1 Свеча
        </button>
        <button @click="setGameMode('trend')" class="flex-1 flex items-center justify-center gap-3 py-3 rounded-xl text-sm font-black uppercase transition-all duration-300" :class="gameMode === 'trend' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'" :disabled="gameState !== 'playing'">
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" /></svg>
          Тренд
        </button>
      </div>
    </div>
    <div class="w-full h-[55vh] px-4 z-10 relative shrink-0 flex pt-2">
      <div class="w-full h-full bg-zinc-800 rounded-[24px] border border-zinc-700 relative overflow-hidden shadow-2xl flex flex-col" :class="{ 'border-green-500/50 shadow-[0_0_30px_-5px_rgba(16,185,129,0.3)]': gameState === 'result' && gameResult === 'win', 'border-rose-500/50 shadow-[0_0_30px_-5px_rgba(244,63,94,0.3)]': gameState === 'result' && gameResult === 'lose' }">
        <div class="absolute inset-0 opacity-20 pointer-events-none" style="background-image: radial-gradient(#fff 1px, transparent 1px); background-size: 20px 20px;"></div>
        <div ref="chartRef" class="w-full h-full absolute inset-0 z-10"></div>
      </div>
    </div>
    <div class="px-4 pt-4 z-20 shrink-0 flex-grow flex items-end">
      <div v-if="gameState === 'playing'" class="grid grid-cols-2 gap-4 h-16 w-full">
        <button @click="makeGuess('long')" class="h-full bg-emerald-500/10 hover:bg-emerald-500/20 border-2 border-emerald-500/50 hover:border-emerald-500 text-emerald-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 group-hover:-translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 10l7-7m0 0l7 7m-7-7v18" /></svg>
          <span class="text-lg font-black tracking-wider group-hover:text-emerald-400">ВВЕРХ</span>
        </button>
        <button @click="makeGuess('short')" class="h-full bg-rose-500/10 hover:bg-rose-500/20 border-2 border-rose-500/50 hover:border-rose-500 text-rose-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 group-hover:translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M19 14l-7 7m0 0l-7-7m7 7V3" /></svg>
          <span class="text-lg font-black tracking-wider group-hover:text-rose-400">ВНИЗ</span>
        </button>
      </div>
      <div v-else-if="gameState === 'result' && !showResultModal" class="h-16 w-full">
        <button @click="loadNewRound" class="w-full h-full bg-white hover:bg-zinc-200 text-black rounded-2xl font-black text-xl shadow-[0_4px_0_#a1a1aa] active:scale-[0.97] transition-all flex items-center justify-center gap-3">
          Дальше
          <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M13 5l7 7-7 7M5 5l7 7-7 7" /></svg>
        </button>
      </div>
    </div>
    <transition name="result-modal" @after-leave="onResultModalLeave">
      <div v-if="showResultModal" class="fixed inset-0 z-50 flex items-center justify-center">
        <div class="absolute inset-0 bg-black/80 backdrop-blur-sm" @click="hideResultModal"></div>
        <div class="relative w-full h-full flex flex-col items-center justify-center">
          <div class="text-[140px] mb-6 animate-bounce">
            {{ gameResult === 'win' ? '🎉' : '💀' }}
          </div>
          <div class="text-center px-4">
            <div class="text-5xl font-black uppercase mb-3" :class="gameResult === 'win' ? 'text-green-400' : 'text-rose-400'">
              {{ gameResult === 'win' ? 'ВЕРНО!' : 'МИМО!' }}
            </div>
            <div class="text-xl font-bold text-white/80">
              {{ gameResult === 'win' ? '+10 очков' : 'Серия сброшена' }}
            </div>
            <div v-if="gameResult === 'win' && streak > 1" class="mt-6 flex items-center justify-center gap-2">
              <div class="text-2xl">🔥</div>
              <div class="text-2xl font-black text-orange-400">x{{ streak }}</div>
              <div class="text-lg font-bold text-orange-300">побед подряд!</div>
            </div>
          </div>
          <div class="absolute bottom-10 left-0 right-0 px-8">
            <div class="h-1.5 bg-white/20 rounded-full overflow-hidden">
              <div class="h-full bg-white transition-all duration-1000 ease-linear" :class="gameResult === 'win' ? 'bg-green-500' : 'bg-rose-500'" :style="{ width: `${progress}%` }"></div>
            </div>
          </div>
        </div>
      </div>
    </transition>
    
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
// @ts-expect-error: no types
import { historicalDatasets, getRandomAsset } from '@/features/chartDataService';

const $router = useRouter();

interface Candle {
  date: string;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
}

interface AssetData {
  name: string;
  symbol: string;
  period: string;
  volatility: string;
  data: Candle[];
}

const datasets = historicalDatasets as Record<string, Record<string, AssetData>>;

const VISIBLE_COUNT = 30;
const HIDDEN_CANDLE = 1;
const HIDDEN_TREND = 10;

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;
let resizeObserver: ResizeObserver | null = null;
let timer: ReturnType<typeof setTimeout> | null = null;
let progressInterval: ReturnType<typeof setInterval> | null = null;

const selectedCategory = ref<string>('crypto');
const currentAsset = ref<AssetData | null>(null);
const visibleData = ref<Candle[]>([]);
const hiddenData = ref<Candle[]>([]);
const gameState = ref<'playing' | 'result' | 'loading'>('playing');
const gameResult = ref<'win' | 'lose' | null>(null);
const gameMode = ref<'candle' | 'trend'>('trend');
const showResultModal = ref(false);
const progress = ref(100);

const score = ref(0);
const streak = ref(0);

const setGameMode = (mode: 'candle' | 'trend') => {
  if (gameState.value !== 'playing') return;
  gameMode.value = mode;
  loadNewRound();
};

const formatPrice = (price: number) => {
  if (!price) return '0';
  return price < 1 ? price.toFixed(4) : price.toFixed(2);
};

const initChart = (data: Candle[], showResultLine = false) => {
  if (!chartRef.value) return;

  if (chartInstance) chartInstance.dispose();
  chartInstance = echarts.init(chartRef.value);

  const dates = data.map(c => c.date);
  const values = data.map(c => [c.open, c.close, c.low, c.high]);
  const splitIndex = showResultLine ? visibleData.value.length : -1;

  const upColor = '#10b981';
  const downColor = '#f43f5e';

  const option: echarts.EChartsOption = {
    backgroundColor: 'transparent',
    grid: {
      left: 5, right: 45, top: 10, bottom: 25,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { show: false },
      splitLine: { show: false }
    },
    yAxis: {
      scale: true,
      position: 'right',
      splitLine: { lineStyle: { color: '#27272a', width: 1 } },
      axisLabel: {
        color: '#71717a',
        fontSize: 10,
        fontFamily: 'monospace',
        formatter: (val: number) => formatPrice(val)
      }
    },
    series: [
      {
        type: 'candlestick',
        data: values,
        barWidth: '60%',
        itemStyle: {
          color: upColor,
          color0: downColor,
          borderColor: upColor,
          borderColor0: downColor,
          borderWidth: 1.5
        },
        markLine: showResultLine ? {
          symbol: 'none',
          silent: true,
          data: [
            {
              xAxis: splitIndex - 1,
              lineStyle: { color: '#fff', type: 'dashed', width: 1, opacity: 0.5 },
              label: { show: false }
            }
          ]
        } : undefined,
        markArea: showResultLine ? {
          silent: true,
          itemStyle: { color: 'rgba(255, 255, 255, 0.05)' },
          data: [[{ xAxis: splitIndex - 1 }, { xAxis: data.length - 1 }]]
        } : undefined
      }
    ]
  };

  chartInstance.setOption(option);
  chartInstance.resize();
};

const startProgressBar = () => {
  progress.value = 100;
  if (progressInterval) clearInterval(progressInterval);

  progressInterval = setInterval(() => {
    progress.value -= 1;
    if (progress.value <= 0) {
      if (progressInterval) {
        clearInterval(progressInterval);
        progressInterval = null;
      }
    }
  }, 10);
};

const hideResultModal = () => {
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }

  if (progressInterval) {
    clearInterval(progressInterval);
    progressInterval = null;
  }

  showResultModal.value = false;
};

const onResultModalLeave = () => {
  progress.value = 100;
};

const loadNewRound = () => {
  gameState.value = 'loading';

  const random = getRandomAsset();
  if (!random) return;

  selectedCategory.value = random.category;
  const asset = datasets?.[random.category]?.[random.assetKey];
  const hiddenCount = gameMode.value === 'candle' ? HIDDEN_CANDLE : HIDDEN_TREND;

  if (!asset || !asset.data || asset.data.length < (VISIBLE_COUNT + hiddenCount)) {
    loadNewRound();
    return;
  }

  currentAsset.value = asset;
  gameState.value = 'playing';
  gameResult.value = null;

  const maxIndex = asset.data.length - (VISIBLE_COUNT + hiddenCount);
  const startIndex = Math.floor(Math.random() * maxIndex);

  visibleData.value = asset.data.slice(startIndex, startIndex + VISIBLE_COUNT);
  hiddenData.value = asset.data.slice(startIndex + VISIBLE_COUNT, startIndex + VISIBLE_COUNT + hiddenCount);

  nextTick(() => {
    initChart(visibleData.value);
  });
};

const makeGuess = (direction: 'long' | 'short') => {
  if (gameState.value !== 'playing') return;

  const lastVisible = visibleData.value[visibleData.value.length - 1];
  const lastHidden = hiddenData.value[hiddenData.value.length - 1];

  if (!lastVisible || !lastHidden) return;

  gameState.value = 'result';

  const startPrice = lastVisible.close;
  const endPrice = lastHidden.close;
  const isPriceUp = endPrice >= startPrice;
  const isWin = (direction === 'long' && isPriceUp) || (direction === 'short' && !isPriceUp);

  if (isWin) {
    gameResult.value = 'win';
    score.value += 10;
    streak.value++;
  } else {
    gameResult.value = 'lose';
    streak.value = 0;
  }

  const fullData = [...visibleData.value, ...hiddenData.value];
  initChart(fullData, true);

  showResultModal.value = true;
  startProgressBar();

  timer = setTimeout(() => {
    hideResultModal();
  }, 1000);
};

onMounted(() => {
  loadNewRound();
  if (chartRef.value) {
    resizeObserver = new ResizeObserver(() => {
      chartInstance?.resize();
    });
    resizeObserver.observe(chartRef.value);
  }
});

onUnmounted(() => {
  if (chartInstance) chartInstance.dispose();
  if (resizeObserver) resizeObserver.disconnect();
  if (timer) clearTimeout(timer);
  if (progressInterval) clearInterval(progressInterval);
});
</script>

<style scoped>
.result-modal-enter-active,
.result-modal-leave-active {
  transition: opacity 0.3s ease;
}

.result-modal-enter-from,
.result-modal-leave-to {
  opacity: 0;
}

.result-modal-enter-active .result-modal-content,
.result-modal-leave-active .result-modal-content {
  transition: all 0.3s ease;
}

.result-modal-enter-from .result-modal-content {
  opacity: 0;
  transform: scale(0.9);
}

.result-modal-leave-to .result-modal-content {
  opacity: 0;
  transform: scale(1.1);
}
</style>
