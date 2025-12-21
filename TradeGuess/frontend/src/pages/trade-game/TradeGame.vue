<template>
  <div class="w-full h-full min-h-screen bg-zinc-950 text-white flex flex-col font-sans select-none overflow-hidden relative pb-[90px]">
    <!-- Header -->
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
              <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z" clip-rule="evenodd"/>
            </svg>
            <span class="font-mono text-xl sm:text-2xl md:text-3xl font-bold text-yellow-100">{{ score }}</span>
          </div>
          <div v-if="streak > 1" class="text-[10px] sm:text-xs font-bold text-orange-400 animate-pulse flex items-center gap-1">
            <span>🔥</span> x{{ streak }}
          </div>
        </div>
      </div>
      <div class="flex gap-2 bg-zinc-900/50 p-1.5 rounded-2xl border border-zinc-800">
        <button @click="setGameMode('candle')"
                class="flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-xs sm:text-sm font-black uppercase transition-all duration-300"
                :class="gameMode === 'candle' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'"
                :disabled="gameState !== 'playing'">
          1 Свеча
        </button>
        <button @click="setGameMode('trend')"
                class="flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-xs sm:text-sm font-black uppercase transition-all duration-300"
                :class="gameMode === 'trend' ? 'bg-zinc-800 text-white shadow-lg border-2 border-zinc-700' : 'text-zinc-500 hover:text-zinc-300'"
                :disabled="gameState !== 'playing'">
          Тренд
        </button>
      </div>
    </div>

    <!-- Chart Container -->
    <div class="flex-1 w-full z-10 relative mt-2 flex flex-col min-h-0 bg-[#131722] border-y border-zinc-800">
      <!-- ✅ ИГРОВАЯ МОДАЛКА (результат) -->
      <transition name="slide-down">
        <div v-if="showResultModal && gameState === 'result'"
             class="absolute inset-0 z-50 flex flex-col items-center justify-center bg-black/70 backdrop-blur-sm p-8">
          <div class="flex flex-col items-center text-center max-w-md w-full px-4"
               :class="gameResult === 'win' ? 'text-green-400' : 'text-rose-400'">
            <div class="text-6xl mb-4 drop-shadow-2xl">{{ gameResult === 'win' ? '🎉' : '💀' }}</div>
            <div class="text-2xl md:text-3xl font-black uppercase tracking-widest mb-2 text-white">
              {{ gameResult === 'win' ? 'ВЕРНО!' : 'МИМО!' }}
            </div>
            <div class="text-sm md:text-base font-bold px-4 py-2 rounded-full bg-white/20 text-white mb-6">
              {{ gameResult === 'win' ? '+10 очков' : 'Серия сброшена' }}
            </div>
          </div>
        </div>
      </transition>

      <!-- ✅ МОДАЛКА ЛИМИТА (НА ВЕСЬ ЭКРАН) -->
      <transition name="fade">
        <div v-if="gameState === 'limitReached'"
             class="absolute inset-0 z-50 flex flex-col items-center justify-center bg-gradient-to-b from-zinc-900/95 to-black/90 backdrop-blur-md p-8">
          <div class="flex flex-col items-center text-center max-w-md w-full px-4">
            <div class="text-6xl mb-6 drop-shadow-2xl">⏰</div>
            <div class="text-2xl md:text-3xl font-black text-yellow-400 mb-4">{{ serverMessage }}</div>
            <div class="text-sm md:text-base text-zinc-300 mb-8 max-w-sm leading-relaxed">
              Дневной лимит попыток исчерпан. Вернитесь завтра за новыми раундами!
            </div>
            <button @click="$router.push('/')"
                    class="px-8 py-3 bg-gradient-to-r from-yellow-500 to-yellow-600 text-black font-black text-lg rounded-2xl shadow-2xl hover:shadow-yellow-500/25 transition-all duration-300 transform hover:scale-105">
              На главную
            </button>
          </div>
        </div>
      </transition>

      <!-- График -->
      <div ref="chartRef" class="w-full h-full absolute inset-0 z-10"></div>
    </div>

    <!-- Bottom Controls -->
    <div class="px-4 pt-4 z-20 shrink-0 h-24 sm:h-28 md:h-32 bg-zinc-950 flex items-start">
      <!-- КНОПКИ ВВЕРХ/ВНИЗ -->
      <div v-if="gameState === 'playing'" class="grid grid-cols-2 gap-4 h-16 sm:h-20 md:h-24 w-full">
        <button @click="makeGuess('long')"
                class="h-full bg-emerald-500/10 hover:bg-emerald-500/20 border-2 border-emerald-500/50 hover:border-emerald-500 text-emerald-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 sm:w-8 sm:h-8 mb-1 group-hover:-translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 10l7-7m0 0l7 7m-7-7v18"/>
          </svg>
          <span class="text-lg sm:text-xl md:text-2xl font-black tracking-wider group-hover:text-emerald-400">ВВЕРХ</span>
        </button>
        <button @click="makeGuess('short')"
                class="h-full bg-rose-500/10 hover:bg-rose-500/20 border-2 border-rose-500/50 hover:border-rose-500 text-rose-500 rounded-2xl flex flex-col items-center justify-center active:scale-[0.96] transition-all group backdrop-blur-sm gap-0.5">
          <svg class="w-6 h-6 sm:w-8 sm:h-8 mb-1 group-hover:translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M19 14l-7 7m0 0l-7-7m7 7V3"/>
          </svg>
          <span class="text-lg sm:text-xl md:text-2xl font-black tracking-wider group-hover:text-rose-400">ВНИЗ</span>
        </button>
      </div>

      <!-- АНИМАЦИЯ -->
      <div v-if="gameState === 'animating'" class="h-16 sm:h-20 md:h-24 w-full flex items-center justify-center">
        <div class="text-lg font-black text-purple-400 animate-pulse flex items-center gap-2">📈 График рисуется...</div>
      </div>

      <!-- ЛОАДЕР -->
      <div v-if="gameState === 'loading'" class="h-16 sm:h-20 md:h-24 w-full flex items-center justify-center">
        <div class="w-8 h-8 border-4 border-zinc-600 border-t-yellow-400 rounded-full animate-spin"></div>
      </div>
    </div>

    <!-- Bottom Navigation -->
    <div class="fixed bottom-0 left-0 w-full bg-zinc-900/90 backdrop-blur-xl border-t border-white/5 z-50 pb-safe shadow-[0_-10px_40px_rgba(0,0,0,0.5)]">
      <div class="flex justify-around items-center h-[70px] sm:h-[80px] md:h-[90px] px-2">
        <button @click="$router.push('/')">
          <svg class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-zinc-500 hover:text-white transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>
          </svg>
          <span class="text-[10px] sm:text-xs font-medium text-zinc-500 hover:text-zinc-300 block mt-1">Главная</span>
        </button>
        <!-- остальные кнопки -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import { useAuthStore } from '@/stores/auth';
import { useGameStore } from '@/stores/game';
import type { Candle, ChartResponse, GuessResponse, ServerCandle } from '@/entities/trade-game/types';

const $router = useRouter();
const authStore = useAuthStore();
const gameStore = useGameStore();

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;
let resizeObserver: ResizeObserver | null = null;
let animationTimer: ReturnType<typeof setTimeout> | null = null;
let resultTimer: ReturnType<typeof setTimeout> | null = null;

const currentAsset = ref<{ name: string; symbol: string }>({ name: 'BTC/USDT', symbol: 'BTC' });
const allCandles = ref<Candle[]>([]);
const visibleCandlesCount = ref(0);
const gameState = ref<'loading' | 'playing' | 'animating' | 'result' | 'limitReached'>('loading');
const gameResult = ref<'win' | 'lose' | null>(null);
const gameMode = ref<'candle' | 'trend'>('trend');
const showResultModal = ref(false);
const segmentId = ref(0);
const serverMessage = ref('');

const score = gameStore.score;
const streak = gameStore.streak;

const apiRequest = async (url: string, options: RequestInit = {}) => {
  const token = authStore.getToken();
  const config: RequestInit = {
    ...options,
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      ...options.headers
    }
  };

  const response = await fetch(url, config);
  if (response.status === 401 || response.status === 403) {
    await authStore.authenticateUser();
    const retryConfig = { ...config, headers: {
        'Authorization': `Bearer ${authStore.getToken()}`,
        'Content-Type': 'application/json'
      }};
    const retryResponse = await fetch(url, retryConfig);
    return await retryResponse.json();
  }

  if (!response.ok) throw new Error(`HTTP ${response.status}`);
  return await response.json();
};

// ✅ ИСПРАВЛЕННАЯ loadNewRound - БЕЗ БЕСКОНЕЧНЫХ ПОВТОРОВ!
const loadNewRound = async (isRetry = false) => {
  console.log('🔄 loadNewRound:', gameMode.value, 'retry:', isRetry);

  if (gameState.value === 'limitReached') return; // ✅ БЛОКИРУЕМ повторы

  gameState.value = 'loading';
  showResultModal.value = false;
  gameResult.value = null;

  try {
    const userId = authStore.getUserId();
    const result: ChartResponse = await apiRequest(
      `https://tradeguess-backend.onrender.com/api/game/chart?userId=${userId}`
    );

    console.log('📊 Сервер ответил:', result);

    // ✅ ПРЯМАЯ обработка лимита БЕЗ try/catch!
    if (!result.success) {
      console.log('❌ ЛИМИТ ИСЧЕРПАН:', result.message);
      gameState.value = 'limitReached';
      serverMessage.value = result.message || 'Дневной лимит исчерпан';
      authStore.safeShowAlert(serverMessage.value);
      return;
    }

    // ✅ Успех - обрабатываем данные
    allCandles.value = result.data!.candles.map((c: ServerCandle) => ({
      date: new Date(c.t).toLocaleString('ru-RU', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' }),
      open: Number(c.o.toFixed(2)),
      high: Number(c.h.toFixed(2)),
      low: Number(c.l.toFixed(2)),
      close: Number(c.c.toFixed(2)),
      volume: Number(c.v.toFixed(2))
    }));

    const hiddenCount = gameMode.value === 'candle' ? 1 : 15;
    visibleCandlesCount.value = allCandles.value.length - hiddenCount;
    segmentId.value = result.data!.segmentId;

    gameState.value = 'playing';
    await nextTick();
    initChart(allCandles.value.slice(0, visibleCandlesCount.value));

  } catch (error: any) {
    console.error('❌ loadNewRound ОШИБКА:', error);

    // ✅ ТОЛЬКО 1 попытка ретрая!
    if (!isRetry) {
      authStore.safeShowAlert('Ошибка сети. Повтор...');
      setTimeout(() => loadNewRound(true), 2000);
    } else {
      gameState.value = 'limitReached';
      serverMessage.value = 'Ошибка соединения. Попробуйте позже.';
    }
  }
};

const makeGuess = async (direction: 'long' | 'short') => {
  if (gameState.value !== 'playing') return;

  gameState.value = 'animating';

  try {
    const userId = authStore.getUserId();
    const result: GuessResponse = await apiRequest(
      `https://tradeguess-backend.onrender.com/api/game/guess?userId=${userId}`,
      {
        method: 'POST',
        body: JSON.stringify({ segmentId: segmentId.value, direction })
      }
    );

    if (!result.success) {
      gameState.value = 'playing';
      authStore.safeShowAlert(result.data.message || 'Ошибка сервера');
      return;
    }

    gameResult.value = result.data.isCorrect ? 'win' : 'lose';
    const resultCandles: Candle[] = result.data.resultCandles.map((c: ServerCandle) => ({
      date: new Date(c.t).toLocaleString('ru-RU', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' }),
      open: Number(c.o.toFixed(2)),
      high: Number(c.h.toFixed(2)),
      low: Number(c.l.toFixed(2)),
      close: Number(c.c.toFixed(2)),
      volume: Number(c.v.toFixed(2))
    }));

    animateResultCandles(resultCandles, result.data.isCorrect);

  } catch (error: any) {
    console.error('❌ makeGuess:', error);
    gameState.value = 'playing';
    authStore.safeShowAlert('Ошибка сети');
  }
};

const animateResultCandles = (resultCandles: Candle[], isCorrect: boolean) => {
  let currentIndex = 0;

  const animateStep = () => {
    if (currentIndex < resultCandles.length) {
      const visibleData = allCandles.value.slice(0, visibleCandlesCount.value);
      const animatedData = [...visibleData, ...resultCandles.slice(0, currentIndex + 1)];
      updateChartData(animatedData, true);
      currentIndex++;
      animationTimer = setTimeout(animateStep, 150);
    } else {
      gameState.value = 'result';
      showResultModal.value = true;

      if (isCorrect) {
        gameStore.addScore(10);
        gameStore.incrementStreak();
      } else {
        gameStore.resetStreak();
      }

      resultTimer = setTimeout(() => {
        showResultModal.value = false;
        loadNewRound();
      }, 2000);
    }
  };

  animateStep();
};

const setGameMode = (mode: 'candle' | 'trend') => {
  if (gameState.value !== 'playing' && gameState.value !== 'loading') return;
  gameMode.value = mode;
  loadNewRound();
};

const initChart = (data: Candle[]) => {
  if (!chartRef.value || !data.length || chartInstance) return;

  chartInstance = echarts.init(chartRef.value);
  updateChartData(data, false);
};

const updateChartData = (data: Candle[], showResultLine = false) => {
  if (!chartInstance || !data.length) return;

  const dates = data.map(c => c.date);
  const values = data.map(c => [c.open, c.close, c.low, c.high]);
  const splitIndex = showResultLine ? Math.max(0, visibleCandlesCount.value - 1) : -1;

  const option: echarts.EChartsOption = {
    backgroundColor: '#131722',
    grid: { left: 10, right: 5, top: 40, bottom: 60, containLabel: false },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const p = Array.isArray(params) ? params[0] : params;
        const candle = data[p.dataIndex];
        if (!candle) return '';
        return `<div style="font-size:11px;">${candle.date}<br/>O: ${candle.open}<br/>C: ${candle.close}</div>`;
      }
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { color: '#787b86', fontSize: 9 },
      splitLine: { show: true, lineStyle: { color: '#2f3342', opacity: 0.5 } }
    },
    yAxis: {
      scale: true,
      position: 'right',
      axisLabel: { color: '#787b86', fontSize: 9 },
      splitLine: { show: true, lineStyle: { color: '#2f3342', opacity: 0.5 } }
    },
    series: [{
      type: 'candlestick',
      data: values,
      itemStyle: {
        color: '#0ecb81',
        color0: '#f6465d',
        borderColor: '#0ecb81',
        borderColor0: '#f6465d'
      },
      markLine: showResultLine && splitIndex >= 0 ? {
        symbol: ['none', 'none'],
        data: [{ xAxis: splitIndex, lineStyle: { color: '#8b5cf6', type: 'dashed' } }]
      } : undefined
    }]
  };

  chartInstance.setOption(option, { notMerge: false, lazyUpdate: true });
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
  gameStore.saveGameData();
  if (animationTimer) clearTimeout(animationTimer);
  if (resultTimer) clearTimeout(resultTimer);
  if (chartInstance) {
    chartInstance.dispose();
    chartInstance = null;
  }
  if (resizeObserver) resizeObserver.disconnect();
});
</script>

<style scoped>
/* ✅ ИГРОВАЯ МОДАЛКА */
.slide-down-enter-active {
  animation: slideDown 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.slide-down-leave-active {
  animation: slideDown 0.3s ease-in reverse;
}

/* ✅ МОДАЛКА ЛИМИТА */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

@keyframes slideDown {
  0% { opacity: 0; transform: scale(0.9); }
  100% { opacity: 1; transform: scale(1); }
}
</style>
