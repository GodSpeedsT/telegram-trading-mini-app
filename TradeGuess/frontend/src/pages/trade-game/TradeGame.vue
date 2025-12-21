<template>
  <div class="w-full h-full min-h-screen bg-zinc-950 text-white flex flex-col font-sans select-none overflow-hidden relative pb-[90px]">

    <!-- Гарантированный фон мозаика (как в меню) -->
    <div class="absolute inset-0 w-full h-full pointer-events-none z-0">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="gamePuzzle" x="0" y="0" width="200" height="200" patternUnits="userSpaceOnUse">
            <path d="M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 1 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37"
                  fill="none" stroke="white" stroke-width="1.5" opacity="0.05" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#gamePuzzle)" />
      </svg>
    </div>

    <!-- Notifications -->
    <div class="fixed top-4 right-4 z-[100] flex flex-col gap-2 pointer-events-none">
      <transition-group name="notification">
        <div v-for="notification in notifications" :key="notification.id"
             class="bg-zinc-800/90 border border-yellow-500/30 text-white p-3 rounded-2xl shadow-xl flex items-center gap-3 backdrop-blur-md max-w-[300px]">
          <div class="flex-shrink-0 w-10 h-10 bg-yellow-500/20 rounded-full flex items-center justify-center">
            <span class="text-xl">🏆</span>
          </div>
          <div class="flex flex-col">
            <h3 class="font-bold text-sm text-yellow-400">{{ notification.title }}</h3>
            <p class="text-xs text-zinc-300 leading-tight">{{ notification.description }}</p>
          </div>
        </div>
      </transition-group>
    </div>

    <!-- Header -->
    <div class="pt-5 pb-2 px-4 z-20 shrink-0 flex flex-col gap-3 bg-zinc-950/80 backdrop-blur-md border-b border-white/5">
      <div class="flex justify-between items-center">
        <div v-if="currentAsset" class="flex items-center gap-3">
          <div class="w-12 h-12 rounded-xl bg-zinc-800 flex items-center justify-center border-2 border-zinc-700 shadow-xl">
            <span class="text-base font-black text-zinc-300">{{ currentAsset.symbol[0] }}</span>
          </div>
          <div class="flex flex-col gap-0.5">
            <h1 class="text-lg font-black tracking-tight text-white leading-none">{{ currentAsset.name }}</h1>
            <span class="text-[10px] font-bold text-zinc-400 bg-zinc-900/80 px-2 py-0.5 rounded-lg border border-zinc-800 w-fit">{{ currentAsset.symbol }}</span>
          </div>
        </div>
        <div class="flex items-center gap-3">
          <!-- Отображение Звёзд ( reward ) -->
          <div class="flex items-center gap-1.5 bg-yellow-500/10 border border-yellow-500/20 px-2.5 py-1.5 rounded-xl">
            <span class="text-yellow-400 text-sm">⭐</span>
            <span class="font-mono text-sm font-bold text-yellow-100">{{ totalStars }}</span>
          </div>
          <!-- Очки игры ( score ) -->
          <div class="flex items-center gap-1.5 bg-zinc-900/90 border border-white/10 px-2.5 py-1.5 rounded-xl">
            <span class="text-emerald-400 text-xs font-black">PTS</span>
            <span class="font-mono text-sm font-bold text-white">{{ score }}</span>
          </div>
        </div>
      </div>
      <div class="flex gap-2 bg-zinc-900/50 p-1 rounded-2xl border border-zinc-800">
        <button @click="setGameMode('candle')"
                class="flex-1 py-2 rounded-xl text-xs font-black uppercase transition-all duration-300"
                :class="gameMode === 'candle' ? 'bg-zinc-800 text-white shadow-lg border border-zinc-700' : 'text-zinc-500'"
                :disabled="['loading', 'animating', 'result'].includes(gameState)">
          1 Свеча
        </button>
        <button @click="setGameMode('trend')"
                class="flex-1 py-2 rounded-xl text-xs font-black uppercase transition-all duration-300"
                :class="gameMode === 'trend' ? 'bg-zinc-800 text-white shadow-lg border border-zinc-700' : 'text-zinc-500'"
                :disabled="['loading', 'animating', 'result'].includes(gameState)">
          Тренд
        </button>
      </div>
    </div>

    <!-- Main Game Area -->
    <div class="flex-1 w-full z-10 relative flex flex-col min-h-0 bg-[#131722]">

      <!-- Модалка Лимита -->
      <div v-if="gameState === 'limitReached'" class="absolute inset-0 z-[60] flex flex-col items-center justify-center bg-black/90 backdrop-blur-xl p-8 text-center gap-6">
        <div class="text-6xl">⏰</div>
        <h2 class="text-2xl font-black text-yellow-400 uppercase tracking-tighter">{{ serverMessage }}</h2>
        <p class="text-zinc-500 text-sm max-w-xs">Дневной лимит попыток исчерпан. Возвращайтесь завтра!</p>
        <button @click="goHome" class="px-8 py-3 bg-white text-black font-black rounded-2xl uppercase text-sm active:scale-95 transition-transform">На главную</button>
      </div>

      <!-- Всплывающий результат -->
      <div v-if="showResultModal"
           class="absolute top-10 left-1/2 -translate-x-1/2 z-50 px-8 py-4 rounded-3xl border-4 shadow-2xl flex flex-col items-center gap-1 min-w-[200px] backdrop-blur-md"
           :class="gameResult === 'win' ? 'bg-emerald-500/10 border-emerald-500 text-emerald-400' : 'bg-rose-500/10 border-rose-500 text-rose-400'">
        <div class="text-4xl">{{ gameResult === 'win' ? '🎉' : '💀' }}</div>
        <div class="text-2xl font-black uppercase tracking-widest">{{ gameResult === 'win' ? 'ВЕРНО!' : 'МИМО!' }}</div>
      </div>

      <div ref="chartRef" class="w-full h-full absolute inset-0 z-10"></div>
    </div>

    <!-- Кнопки -->
    <div class="px-4 pt-4 z-20 shrink-0 h-28 bg-zinc-950 flex items-start">
      <div v-if="gameState === 'playing'" class="grid grid-cols-2 gap-4 h-16 w-full">
        <button @click="makeGuess('long')" class="h-full bg-emerald-500/10 border-2 border-emerald-500/50 text-emerald-500 rounded-2xl flex flex-col items-center justify-center font-black active:scale-95 transition-all uppercase tracking-widest">ВВЕРХ</button>
        <button @click="makeGuess('short')" class="h-full bg-rose-500/10 border-2 border-rose-500/50 text-rose-500 rounded-2xl flex flex-col items-center justify-center font-black active:scale-95 transition-all uppercase tracking-widest">ВНИЗ</button>
      </div>
      <div v-else class="w-full flex justify-center py-4">
        <div class="w-8 h-8 border-4 border-zinc-800 border-t-emerald-500 rounded-full animate-spin"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import { useAuthStore } from '@/stores/auth';
import { useGameStore } from '@/stores/game';
import { updateAllAchievements, loadAchievements } from '@/pages/achievements/utils/achievements';
import type { Candle, ChartResponse, GuessResponse, ServerCandle } from '@/entities/trade-game/types';
import type { Achievement } from '@/pages/achievements/utils/achievements';

const $router = useRouter();
const authStore = useAuthStore();
const gameStore = useGameStore();

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;
let resizeObserver: ResizeObserver | null = null;
let animationTimer: ReturnType<typeof setTimeout> | null = null;

const currentAsset = ref({ name: 'BTC/USDT', symbol: 'BTC' });
const allCandles = ref<Candle[]>([]);
const visibleCandlesCount = ref(0);
const gameState = ref<'loading' | 'playing' | 'animating' | 'result' | 'limitReached'>('loading');
const gameResult = ref<'win' | 'lose' | null>(null);
const gameMode = ref<'candle' | 'trend'>('trend');
const showResultModal = ref(false);
const segmentId = ref(0);
const serverMessage = ref('');
const notifications = ref<any[]>([]);

const score = computed(() => gameStore.score);
const streak = computed(() => gameStore.streak);

// Считаем звезды прямо из ачивок
const totalStars = computed(() => {
  const achs = loadAchievements();
  return achs.filter(a => a.unlocked).reduce((sum, a) => sum + (a.id % 2 === 0 ? 50 : 100), 0);
});

const notify = (achievement: Achievement) => {
  notifications.value.push({ id: Date.now() + Math.random(), title: achievement.title, description: achievement.description });
  setTimeout(() => { if (notifications.value.length > 0) notifications.value.shift(); }, 5000);
};

const goHome = () => $router.push('/');

const loadNewRound = async () => {
  gameState.value = 'loading';
  showResultModal.value = false;
  try {
    const userId = authStore.getUserId();
    const token = authStore.getToken();
    const response = await fetch(`https://tradeguess-backend.onrender.com/api/game/chart?userId=${userId}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const result: ChartResponse = await response.json();

    if (!result.success) {
      gameState.value = 'limitReached';
      serverMessage.value = result.message || 'Лимит исчерпан';
      return;
    }

    if (result.data) {
      allCandles.value = result.data.candles.map((c: ServerCandle) => ({
        date: new Date(c.t).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' }),
        open: Number(c.o), high: Number(c.h), low: Number(c.l), close: Number(c.c), volume: Number(c.v)
      }));
      segmentId.value = result.data.segmentId;
      const hiddenCount = gameMode.value === 'candle' ? 1 : 15;
      visibleCandlesCount.value = allCandles.value.length - hiddenCount;
      gameState.value = 'playing';
      await nextTick();
      initChart(allCandles.value.slice(0, visibleCandlesCount.value));
    }
  } catch {
    gameState.value = 'limitReached';
    serverMessage.value = 'Ошибка сети';
  }
};

const makeGuess = async (direction: 'long' | 'short') => {
  if (gameState.value !== 'playing') return;
  gameState.value = 'animating';
  try {
    const userId = authStore.getUserId();
    const token = authStore.getToken();
    const response = await fetch(`https://tradeguess-backend.onrender.com/api/game/guess?userId=${userId}`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
      body: JSON.stringify({ segmentId: segmentId.value, direction })
    });
    const result: GuessResponse = await response.json();
    if (!result.success) return;

    gameResult.value = result.data.isCorrect ? 'win' : 'lose';
    const resultCandles: Candle[] = result.data.resultCandles.map((c: ServerCandle) => ({
      date: new Date(c.t).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' }),
      open: Number(c.o), high: Number(c.h), low: Number(c.l), close: Number(c.c), volume: Number(c.v)
    }));

    animateResult(resultCandles, result.data.isCorrect);
  } catch { gameState.value = 'playing'; }
};

const animateResult = (resCandles: Candle[], isCorrect: boolean) => {
  let i = 0;
  const step = () => {
    if (i < resCandles.length) {
      const currentData = [...allCandles.value.slice(0, visibleCandlesCount.value), ...resCandles.slice(0, i + 1)];
      updateChart(currentData, true);
      i++;
      animationTimer = setTimeout(step, 150);
    } else {
      gameState.value = 'result';
      showResultModal.value = true;
      if (isCorrect) {
        gameStore.addScore(10);
        gameStore.incrementStreak();
      } else {
        gameStore.resetStreak();
      }

      const newlyUnlocked = updateAllAchievements({
        totalWins: 0, trendWins: 0, candleWins: 0,
        currentTrendStreak: 0, currentCandleStreak: 0,
        totalScore: gameStore.score, currentStreak: gameStore.streak
      });
      newlyUnlocked.forEach(notify);

      setTimeout(loadNewRound, 2000);
    }
  };
  step();
};

const initChart = (data: Candle[]) => {
  if (!chartRef.value) return;
  if (chartInstance) chartInstance.dispose();
  chartInstance = echarts.init(chartRef.value);
  updateChart(data, false);
};

const updateChart = (data: Candle[], showLine: boolean) => {
  if (!chartInstance) return;
  const option: echarts.EChartsOption = {
    backgroundColor: 'transparent',
    grid: { left: 10, right: 5, top: 40, bottom: 60, containLabel: false },
    xAxis: { type: 'category', data: data.map(c => c.date), axisLabel: { color: '#787b86', fontSize: 9 } },
    yAxis: { scale: true, position: 'right', axisLabel: { color: '#787b86', fontSize: 9 } },
    series: [{
      type: 'candlestick',
      data: data.map(c => [c.open, c.close, c.low, c.high]),
      itemStyle: { color: '#0ecb81', color0: '#f6465d', borderColor: '#0ecb81', borderColor0: '#f6465d' },
      markLine: showLine ? {
        symbol: ['none', 'none'],
        data: [{ xAxis: visibleCandlesCount.value - 1, lineStyle: { color: '#8b5cf6', type: 'dashed' } }]
      } : undefined
    }]
  };
  chartInstance.setOption(option, true);
};

const setGameMode = (mode: 'candle' | 'trend') => {
  if (['animating', 'loading'].includes(gameState.value)) return;
  gameMode.value = mode;
  loadNewRound();
};

onMounted(async () => {
  gameStore.loadGameData();
  await loadNewRound();
  if (chartRef.value) {
    resizeObserver = new ResizeObserver(() => chartInstance?.resize());
    resizeObserver.observe(chartRef.value);
  }
});

onUnmounted(() => {
  if (chartInstance) chartInstance.dispose();
  if (resizeObserver) resizeObserver.disconnect();
  if (animationTimer) clearTimeout(animationTimer);
});
</script>
