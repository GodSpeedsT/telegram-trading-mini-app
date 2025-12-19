<template>
  <div class="w-full h-full min-h-screen bg-zinc-950 text-white flex flex-col font-sans select-none overflow-hidden relative pb-[90px]">
    <div class="fixed top-4 right-4 z-50 flex flex-col gap-2 pointer-events-none">
      <transition-group name="notification">
        <div v-for="notification in notifications" :key="notification.id"
             class="bg-zinc-800/90 border border-yellow-500/30 text-white p-3 rounded-2xl shadow-xl flex items-center gap-3 backdrop-blur-md max-w-[300px] sm:max-w-[350px] md:max-w-[400px]">
          <div class="flex-shrink-0 w-10 h-10 bg-yellow-500/20 rounded-full flex items-center justify-center"><span class="text-xl">🏆</span></div>
          <div class="flex flex-col"><h3 class="font-bold text-sm sm:text-base md:text-lg text-yellow-400">{{ notification.title }}</h3><p class="text-xs sm:text-sm text-zinc-300 leading-tight">{{ notification.description }}</p></div>
        </div>
      </transition-group>
    </div>
    <div class="pt-5 pb-2 px-4 z-20 shrink-0 flex flex-col gap-3 bg-zinc-950">
      <div class="flex justify-between items-center">
        <div v-if="currentAsset" class="flex items-center gap-3">
          <div class="w-12 h-12 sm:w-14 sm:h-14 md:w-16 md:h-16 rounded-xl bg-zinc-800 flex items-center justify-center border-2 border-zinc-700 shadow-xl">
            <span class="text-base sm:text-lg md:text-xl font-black text-zinc-300">{{ currentAsset.symbol[0] }}</span>
          </div>
          <div class="flex flex-col gap-0.5">
            <h1 class="text-lg sm:text-xl md:text-2xl font-black tracking-tight text-white leading-none">{{ currentAsset.name }}</h1>
            <span class="text-[10px] sm:text-xs font-bold text-zinc-400 bg-zinc-900/80 px-2 py-0.5 rounded-lg border border-zinc-800 w-fit">{{ currentAsset.symbol }}</span>
          </div>
        </div>
        <div class="flex flex-col items-end gap-1">
          <div class="flex items-center gap-2 bg-zinc-900/90 border-2 border-yellow-500/20 px-3 py-1.5 rounded-xl shadow-lg shadow-yellow-900/10">
            <svg viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4 sm:w-5 sm:h-5 md:w-6 md:h-6 text-yellow-400">
              <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z" clip-rule="evenodd" />
            </svg>
            <span class="font-mono text-xl sm:text-2xl md:text-3xl font-bold text-yellow-100">{{ score }}</span>
          </div>
          <div v-if="streak > 1" class="text-[10px] sm:text-xs font-bold text-orange-400 animate-pulse flex items-center gap-1">
            <span>🔥</span> x{{ streak }}
          </div>
        </div>
      </div>
      <div class="flex gap-2 bg-zinc-900/50 p-1.5 rounded-2xl border border-zinc-800">
        <button @click="setGameMode('candle')" class="flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-xs sm:text-sm font-black uppercase transition-all duration-300" :class="gameMode === 'candle' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'" :disabled="gameState !== 'playing'">1 Свеча</button>
        <button @click="setGameMode('trend')" class="flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-xs sm:text-sm font-black uppercase transition-all duration-300" :class="gameMode === 'trend' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'" :disabled="gameState !== 'playing'">Тренд</button>
      </div>
    </div>
    <div class="flex-1 w-full z-10 relative mt-2 flex flex-col min-h-0 bg-[#131722] border-y border-zinc-800">
      <div ref="chartRef" class="w-full h-full absolute inset-0 z-10"></div>
      <transition enter-active-class="transition duration-500 cubic-bezier(0.175, 0.885, 0.32, 1.275)" enter-from-class="opacity-0 scale-50" enter-to-class="opacity-100 scale-100" leave-active-class="transition duration-300 ease-in" leave-from-class="opacity-100 scale-100" leave-to-class="opacity-0 scale-90">
        <div v-if="showResultModal" class="absolute inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
          <div class="flex flex-col items-center justify-center p-4 sm:p-6 rounded-[30px] border-4 shadow-2xl bg-zinc-900 min-w-[200px] sm:min-w-[250px] md:min-w-[300px]"
               :class="gameResult === 'win' ? 'border-green-500 text-green-400' : 'border-rose-500 text-rose-400'">
            <div class="text-5xl sm:text-6xl md:text-7xl mb-2 filter drop-shadow-lg">{{ gameResult === 'win' ? '🎉' : '💀' }}</div>
            <div class="text-xl sm:text-2xl md:text-3xl font-black uppercase tracking-widest text-white">{{ gameResult === 'win' ? 'ВЕРНО!' : 'МИМО!' }}</div>
            <div class="text-[10px] sm:text-xs font-bold mt-1 px-3 py-1 rounded-full bg-white/10 text-white">{{ gameResult === 'win' ? '+10 очков' : 'Серия сброшена' }}</div>
          </div>
        </div>
      </transition>
      <div v-if="showResultModal" class="absolute bottom-0 left-0 right-0 h-1 bg-white/10 z-50">
        <div class="h-full transition-all duration-[10ms] ease-linear"
             :class="gameResult === 'win' ? 'bg-green-500' : 'bg-rose-500'"
             :style="{ width: `${progress}%` }"></div>
      </div>
    </div>
    <div class="px-4 pt-4 z-20 shrink-0 h-24 sm:h-28 md:h-32 bg-zinc-950 flex items-start">
      <div v-if="gameState === 'playing'" class="grid grid-cols-2 gap-4 h-16 sm:h-20 md:h-24 w-full">
        <button @click="makeGuess('long')"
                class="h-full bg-emerald-500/10 hover:bg-emerald-500/20 border-2 border-emerald-500/50 hover:border-emerald-500 text-emerald-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 sm:w-8 sm:h-8 mb-1 group-hover:-translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 10l7-7m0 0l7 7m-7-7v18" />
          </svg>
          <span class="text-lg sm:text-xl md:text-2xl font-black tracking-wider group-hover:text-emerald-400">ВВЕРХ</span>
        </button>
        <button @click="makeGuess('short')"
                class="h-full bg-rose-500/10 hover:bg-rose-500/20 border-2 border-rose-500/50 hover:border-rose-500 text-rose-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 sm:w-8 sm:h-8 mb-1 group-hover:translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
          </svg>
          <span class="text-lg sm:text-xl md:text-2xl font-black tracking-wider group-hover:text-rose-400">ВНИЗ</span>
        </button>
      </div>
      <div v-else-if="gameState === 'result' && !showResultModal" class="h-16 sm:h-20 md:h-24 w-full">
        <button @click="loadNewRound"
                class="w-full h-full bg-white hover:bg-zinc-200 text-black rounded-2xl font-black text-xl sm:text-2xl md:text-3xl shadow-[0_4px_0_#a1a1aa] active:scale-[0.97] transition-all flex items-center justify-center gap-3">
          <span>Дальше</span>
          <svg class="w-6 h-6 sm:w-8 sm:h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M13 5l7 7-7 7M5 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>

    <div class="fixed bottom-0 left-0 w-full bg-zinc-900/90 backdrop-blur-xl border-t border-white/5 z-50 pb-safe shadow-[0_-10px_40px_rgba(0,0,0,0.5)]">
      <div class="flex justify-around items-center h-[70px] sm:h-[80px] md:h-[90px] px-2">
        <button @click="$router.push('/')" class="flex flex-col items-center justify-center gap-1 w-16 sm:w-20 h-full group">
          <svg class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-zinc-500 group-hover:text-white transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          <span class="text-[10px] sm:text-xs font-medium text-zinc-500 group-hover:text-zinc-300">Главная</span>
        </button>
        <button @click="$router.push('/test')" class="flex flex-col items-center justify-center gap-1 w-16 sm:w-20 h-full group">
          <svg class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-zinc-500 group-hover:text-green-400 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
          </svg>
          <span class="text-[10px] sm:text-xs font-medium text-zinc-500 group-hover:text-green-400">Live</span>
        </button>
        <button class="flex flex-col items-center justify-center gap-1 w-16 sm:w-20 h-full relative">
          <div class="absolute top-2 w-10 h-10 sm:w-12 sm:h-12 bg-purple-500/20 rounded-full blur-lg"></div>
          <svg class="w-7 h-7 sm:w-8 sm:h-8 md:w-9 md:h-9 text-purple-400 relative z-10" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span class="text-[10px] sm:text-xs font-bold text-purple-400 relative z-10">Игра</span>
        </button>
        <button @click="$router.push('/test')" class="flex flex-col items-center justify-center gap-1 w-16 sm:w-20 h-full group">
          <svg class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-zinc-500 group-hover:text-yellow-400 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7" />
          </svg>
          <span class="text-[10px] sm:text-xs font-medium text-zinc-500 group-hover:text-yellow-400">Награды</span>
        </button>
        <button @click="$router.push('/test')" class="flex flex-col items-center justify-center gap-1 w-16 sm:w-20 h-full group">
          <svg class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-zinc-500 group-hover:text-white transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          <span class="text-[10px] sm:text-xs font-medium text-zinc-500 group-hover:text-zinc-300">Профиль</span>
        </button>
      </div>
    </div>
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

interface Achievement {
  id: number;
  title: string;
  description: string;
  unlocked: boolean;
  maxProgress: number;
  type: string;
  progress: number;
}

interface AchievementNotification {
  id: number;
  title: string;
  description: string;
}

const datasets = historicalDatasets as Record<string, Record<string, AssetData>>;
const VISIBLE_COUNT = 30;
const HIDDEN_CANDLE = 1;
const HIDDEN_TREND = 15;
const STORAGE_KEY = 'trade_game_achievements';
const SCORE_KEY = 'trade_game_score';
const STATS_KEY = 'trade_game_stats';

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
const notifications = ref<AchievementNotification[]>([]);

const totalWins = ref(0);
const trendWins = ref(0);
const candleWins = ref(0);
const currentTrendStreak = ref(0);
const currentCandleStreak = ref(0);

const initialAchievements: Achievement[] = [
  { id: 2, title: 'Первая прибыль', description: 'Закрыть сделку в плюс', unlocked: false, maxProgress: 1, type: 'counter', progress: 0 },
  { id: 3, title: 'Самодостаточный', description: '5 побед подряд', unlocked: false, maxProgress: 5, type: 'streak', progress: 0 },
  { id: 4, title: 'Миллионер', description: '15 побед подряд', unlocked: false, maxProgress: 15, type: 'streak', progress: 0 },
  { id: 5, title: 'Трендовый гений', description: '10 трендов подряд', unlocked: false, maxProgress: 10, type: 'streak', progress: 0 },
  { id: 6, title: 'Свечной эксперт', description: '5 свечей подряд', unlocked: false, maxProgress: 5, type: 'streak', progress: 0 },
  { id: 7, title: 'Стратег', description: '50 побед в тренде', unlocked: false, maxProgress: 50, type: 'counter', progress: 0 },
  { id: 8, title: 'Аналитик', description: '50 побед в свечах', unlocked: false, maxProgress: 50, type: 'counter', progress: 0 },
  { id: 9, title: 'Ученик', description: 'Набрать 100 очков', unlocked: false, maxProgress: 100, type: 'counter', progress: 0 },
  { id: 10, title: 'Мастер', description: 'Набрать 1000 очков', unlocked: false, maxProgress: 1000, type: 'counter', progress: 0 },
];

const loadAchievements = (): Achievement[] => {
  const saved = localStorage.getItem(STORAGE_KEY);
  return saved ? JSON.parse(saved) : initialAchievements;
};

const saveAchievements = (achievements: Achievement[]) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(achievements));
};

const updateAchievementProgress = (achievementId: number, increment = 1) => {
  const achievements = loadAchievements();
  const achievement = achievements.find((a) => a.id === achievementId);
  if (!achievement || achievement.unlocked) return;
  if (achievement.type === 'streak') {
    achievement.progress = increment > 0 ? achievement.progress + 1 : 0;
  } else {
    achievement.progress = Math.min(achievement.progress + increment, achievement.maxProgress);
  }
  if (achievement.progress >= achievement.maxProgress) {
    achievement.unlocked = true;
    notifications.value.push({ id: Date.now(), title: achievement.title, description: achievement.description });
    setTimeout(() => notifications.value.shift(), 5000);
  }
  saveAchievements(achievements);
};

const loadScore = () => parseInt(localStorage.getItem(SCORE_KEY) || '0');
const saveScore = (val: number) => localStorage.setItem(SCORE_KEY, val.toString());

const loadStats = () => {
  const saved = localStorage.getItem(STATS_KEY);
  if (saved) return JSON.parse(saved);
  return { totalWins: 0, trendWins: 0, candleWins: 0, currentTrendStreak: 0, currentCandleStreak: 0 };
};

const saveStats = () => {
  localStorage.setItem(STATS_KEY, JSON.stringify({
    totalWins: totalWins.value,
    trendWins: trendWins.value,
    candleWins: candleWins.value,
    currentTrendStreak: currentTrendStreak.value,
    currentCandleStreak: currentCandleStreak.value
  }));
};

const setGameMode = (mode: 'candle' | 'trend') => {
  if (gameState.value !== 'playing') return;
  gameMode.value = mode;
  if (mode === 'candle') currentTrendStreak.value = 0;
  else currentCandleStreak.value = 0;
  saveStats();
  loadNewRound();
};

interface EChartsTooltipParam {
  dataIndex: number;
  componentType: string;
  seriesIndex: number;
}

const initChart = (data: Candle[], showResultLine = false) => {
  if (!chartRef.value) return;
  if (chartInstance) chartInstance.dispose();
  chartInstance = echarts.init(chartRef.value);

  const dates = data.map(c => c.date);
  const values = data.map(c => [c.open, c.close, c.low, c.high]);
  const splitIndex = showResultLine ? visibleData.value.length : -1;

  const upColor = '#0ecb81';
  const downColor = '#f6465d';
  const bgColor = '#131722';

  const option: echarts.EChartsOption = {
    backgroundColor: bgColor,
    grid: {
      left: 10,
      right: 5,
      top: 40,
      bottom: 60,
      containLabel: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#2d3748',
          color: '#fff',
          fontSize: window.innerWidth < 640 ? 10 : 12
        },
        lineStyle: { color: '#71717a', type: 'dashed' }
      },
      backgroundColor: 'rgba(23, 27, 38, 0.9)',
      borderColor: '#3f3f46',
      borderWidth: 1,
      padding: 8,
      textStyle: {
        color: '#ccc',
        fontSize: window.innerWidth < 640 ? 10 : 12
      },
      formatter: (params: EChartsTooltipParam[] | unknown) => {
        if (!Array.isArray(params) || !params[0]) return '';
        const item = params[0] as EChartsTooltipParam;
        const idx = item.dataIndex;
        const candle = data[idx];
        if (!candle) return '';
        const change = ((candle.close - candle.open) / candle.open * 100);
        const color = change >= 0 ? upColor : downColor;
        const fontSize = window.innerWidth < 640 ? '10px' : '11px';
        return `
          <div style="font-family: monospace; font-size: ${fontSize}; line-height: 1.6;">
            <div style="color: #9ca3af; margin-bottom: 4px;">${candle.date}</div>
            <div style="display: flex; justify-content: space-between; gap: 15px;"><span>O:</span><span style="color:${color}">${candle.open.toFixed(2)}</span></div>
            <div style="display: flex; justify-content: space-between; gap: 15px;"><span>H:</span><span style="color:#10b981">${candle.high.toFixed(2)}</span></div>
            <div style="display: flex; justify-content: space-between; gap: 15px;"><span>L:</span><span style="color:#f43f5e">${candle.low.toFixed(2)}</span></div>
            <div style="display: flex; justify-content: space-between; gap: 15px;"><span>C:</span><span style="color:${color}">${candle.close.toFixed(2)}</span></div>
            <div style="display: flex; justify-content: space-between; gap: 15px; margin-top: 4px; border-top: 1px solid #333; padding-top: 4px;"><span>Vol:</span><span style="color:#60a5fa">${(candle.volume/1000).toFixed(1)}K</span></div>
          </div>
        `;
      }
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: '#787b86',
        fontSize: window.innerWidth < 640 ? 9 : 10,
        formatter: (val: string) => val.slice(5)
      },
      splitLine: { show: true, lineStyle: { color: '#2f3342', type: 'dashed', opacity: 0.5 } }
    },
    yAxis: {
      scale: true,
      position: 'right',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { show: true, lineStyle: { color: '#2f3342', type: 'dashed', opacity: 0.5 } },
      axisLabel: {
        color: '#787b86',
        fontSize: window.innerWidth < 640 ? 9 : 10,
        fontFamily: 'monospace'
      }
    },
    dataZoom: [
      { type: 'inside', xAxisIndex: 0, start: 0, end: 100 }
    ],
    series: [{
      type: 'candlestick',
      data: values,
      itemStyle: { color: upColor, color0: downColor, borderColor: upColor, borderColor0: downColor },
      markLine: showResultLine ? {
        symbol: 'none', silent: true,
        data: [{ xAxis: splitIndex - 1, lineStyle: { color: '#8b5cf6', type: 'dashed', width: 2 }, label: { show: false } }]
      } : undefined,
      markArea: showResultLine ? {
        silent: true,
        itemStyle: { color: 'rgba(0, 0, 0, 0.3)' },
        data: [[{ xAxis: splitIndex - 1 }, { xAxis: values.length - 1 }]]
      } : undefined
    }]
  };
  chartInstance.setOption(option);
  chartInstance.resize();
};

const startProgressBar = () => {
  progress.value = 100;
  if (progressInterval) clearInterval(progressInterval);
  progressInterval = setInterval(() => {
    progress.value -= 1;
    if (progress.value <= 0 && progressInterval) clearInterval(progressInterval);
  }, 10);
};

const hideResultModal = () => {
  if (timer) clearTimeout(timer);
  if (progressInterval) clearInterval(progressInterval);
  showResultModal.value = false;
};

const loadNewRound = () => {
  hideResultModal();
  gameState.value = 'loading';
  const random = getRandomAsset();
  if (!random) return;
  selectedCategory.value = random.category;
  const asset = datasets?.[random.category]?.[random.assetKey];
  const hiddenCount = gameMode.value === 'candle' ? HIDDEN_CANDLE : HIDDEN_TREND;
  if (!asset || !asset.data || asset.data.length < (VISIBLE_COUNT + hiddenCount)) { loadNewRound(); return; }
  currentAsset.value = asset;
  gameState.value = 'playing';
  gameResult.value = null;
  const maxIndex = asset.data.length - (VISIBLE_COUNT + hiddenCount);
  const startIndex = Math.floor(Math.random() * maxIndex);
  visibleData.value = asset.data.slice(startIndex, startIndex + VISIBLE_COUNT);
  hiddenData.value = asset.data.slice(startIndex + VISIBLE_COUNT, startIndex + VISIBLE_COUNT + hiddenCount);
  nextTick(() => initChart(visibleData.value));
};

const makeGuess = (direction: 'long' | 'short') => {
  if (gameState.value !== 'playing') return;
  const lastVisible = visibleData.value[visibleData.value.length - 1];
  const lastHidden = hiddenData.value[hiddenData.value.length - 1];
  if (!lastVisible || !lastHidden) return;
  gameState.value = 'result';
  const isPriceUp = lastHidden.close >= lastVisible.close;
  const isWin = (direction === 'long' && isPriceUp) || (direction === 'short' && !isPriceUp);
  if (isWin) {
    gameResult.value = 'win';
    score.value += 10;
    streak.value++;
    totalWins.value++;
    saveScore(score.value);
    updateAchievementProgress(2);
    if (streak.value >= 5) updateAchievementProgress(3);
    if (streak.value >= 15) updateAchievementProgress(4);
    if (score.value >= 100) updateAchievementProgress(9, 10);
    if (score.value >= 1000) updateAchievementProgress(10, 10);
    if (gameMode.value === 'trend') {
      trendWins.value++;
      currentTrendStreak.value++;
      currentCandleStreak.value = 0;
      updateAchievementProgress(7);
      if (currentTrendStreak.value >= 10) updateAchievementProgress(5);
    } else {
      candleWins.value++;
      currentCandleStreak.value++;
      currentTrendStreak.value = 0;
      updateAchievementProgress(8);
      if (currentCandleStreak.value >= 5) updateAchievementProgress(6);
    }
    saveStats();
  } else {
    gameResult.value = 'lose';
    streak.value = 0;
    currentTrendStreak.value = 0;
    currentCandleStreak.value = 0;
    saveStats();
  }
  const fullData = [...visibleData.value, ...hiddenData.value];
  initChart(fullData, true);
  showResultModal.value = true;
  startProgressBar();
  timer = setTimeout(() => { hideResultModal(); }, 1000);
};

onMounted(() => {
  score.value = loadScore();
  const stats = loadStats();
  totalWins.value = stats.totalWins;
  trendWins.value = stats.trendWins;
  candleWins.value = stats.candleWins;
  currentTrendStreak.value = stats.currentTrendStreak;
  currentCandleStreak.value = stats.currentCandleStreak;
  loadNewRound();
  if (chartRef.value) {
    resizeObserver = new ResizeObserver(() => {
      chartInstance?.resize();
      // Переинициализация графика при изменении размера для адаптивности
      if (gameState.value === 'result') {
        const fullData = [...visibleData.value, ...hiddenData.value];
        initChart(fullData, true);
      } else if (visibleData.value.length > 0) {
        initChart(visibleData.value, false);
      }
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
