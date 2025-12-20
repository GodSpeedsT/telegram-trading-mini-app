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
        <!-- кнопки ВВЕРХ/ВНИЗ -->
      </div>
      <div v-else-if="gameState === 'limitReached'" class="h-16 sm:h-20 md:h-24 w-full flex flex-col items-center justify-center text-center p-4">
        <div class="text-xl font-black text-yellow-400 mb-2">⏰</div>
        <div class="text-sm font-bold text-zinc-300 mb-1">{{ serverMessage }}</div>
        <div class="text-xs text-zinc-500">Попробуйте завтра!</div>
      </div>
      <div v-else-if="gameState === 'result' && !showResultModal" class="h-16 sm:h-20 md:h-24 w-full">
        <!-- кнопка Дальше -->
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

const $router = useRouter();

// Получаем userId и token
const getUserId = () => {
  try {
    const userData = JSON.parse(localStorage.getItem('userData') || '{}');
    return userData.telegramId?.toString() || '999999';
  } catch {
    return '999999';
  }
};

const getToken = () => {
  return localStorage.getItem('token') || '';
};

// Интерфейсы
interface ServerCandle { t: number; o: number; h: number; l: number; c: number; v: number; }
interface Candle { date: string; open: number; high: number; low: number; close: number; volume: number; }
interface ChartResponse {
  success: boolean;
  data?: {
    segmentId: number;
    candles: ServerCandle[];
    attemptsLeft: number;
  };
  message?: string;
}
interface GuessResponse {
  success: boolean;
  data: {
    isCorrect: boolean;
    resultCandles: ServerCandle[];
    message: string;
    priceChangePercent: number;
  };
}

// Refs
const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;
let resizeObserver: ResizeObserver | null = null;
let timer: ReturnType<typeof setTimeout> | null = null;
let progressInterval: ReturnType<typeof setInterval> | null = null;

const currentAsset = ref<{ name: string; symbol: string }>({ name: 'BTC/USDT', symbol: 'BTC' });
const allCandles = ref<Candle[]>([]);
const visibleCandlesCount = ref(0);
const gameState = ref<'playing' | 'result' | 'loading' | 'limitReached'>('loading');
const gameResult = ref<'win' | 'lose' | null>(null);
const gameMode = ref<'candle' | 'trend'>('trend');
const showResultModal = ref(false);
const progress = ref(100);
const attemptsLeft = ref(10);
const segmentId = ref(0);
const serverMessage = ref('');
const limitReached = ref(false);

const score = ref(0);
const streak = ref(0);
const notifications = ref<any[]>([]);

const safeShowAlert = (message: string) => {
  console.warn('🚨', message);
  // TODO: Telegram alert
};

// Загрузка нового раунда с сервера
const loadNewRound = async () => {
  console.log('🔄 Загрузка нового раунда...');
  gameState.value = 'loading';
  showResultModal.value = false;
  limitReached.value = false;

  try {
    const userId = getUserId();
    const token = getToken();

    const response = await fetch(`https://tradeguess-backend.onrender.com/api/game/chart?userId=5`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    const result: ChartResponse = await response.json();

    // ✅ НОВАЯ ЛОГИКА: проверяем success И message
    if (!result.success) {
      console.error('❌ Лимит исчерпан:', result.message);
      gameState.value = 'limitReached';
      serverMessage.value = result.message || 'Дневной лимит исчерпан';
      limitReached.value = true;
      safeShowAlert(serverMessage.value);
      return;
    }

    if (!result.data?.candles?.length) {
      throw new Error('Нет данных свечей');
    }

    // Конвертируем серверные данные
    allCandles.value = result.data.candles.map((c: ServerCandle) => ({
      date: new Date(c.t).toLocaleString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      }),
      open: Number(c.o.toFixed(2)),
      high: Number(c.h.toFixed(2)),
      low: Number(c.l.toFixed(2)),
      close: Number(c.c.toFixed(2)),
      volume: Number(c.v.toFixed(2))
    }));

    const hiddenCount = gameMode.value === 'candle' ? 1 : 15;
    visibleCandlesCount.value = allCandles.value.length - hiddenCount;

    segmentId.value = result.data.segmentId;
    attemptsLeft.value = result.data.attemptsLeft;

    gameState.value = 'playing';
    gameResult.value = null;

    await nextTick();
    initChart(allCandles.value.slice(0, visibleCandlesCount.value));

  } catch (error) {
    console.error('❌ Ошибка загрузки:', error);
    safeShowAlert('Ошибка сети. Повтор через 2 сек...');
    setTimeout(loadNewRound, 2000);
  }
};

// Отправка угадывания на сервер
const sendGuess = async (direction: 'long' | 'short'): Promise<GuessResponse | null> => {
  try {
    const userId = getUserId();
    const token = getToken();

    const response = await fetch(`https://tradeguess-backend.onrender.com/api/game/guess?userId=5`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        segmentId: segmentId.value,
        direction
      })
    });

    const result: GuessResponse = await response.json();
    return result;
  } catch (error) {
    console.error('❌ Ошибка отправки:', error);
    return null;
  }
};

const initChart = (data: Candle[], showResultLine = false) => {
  if (!chartRef.value) return;

  if (chartInstance) chartInstance.dispose();
  chartInstance = echarts.init(chartRef.value);

  const dates = data.map(c => c.date);
  const values = data.map(c => [c.open, c.close, c.low, c.high]);
  const splitIndex = showResultLine ? visibleCandlesCount.value - 1 : -1;

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
      markLine: showResultLine ? {
        symbol: ['none', 'none'],
        data: [{ xAxis: splitIndex, lineStyle: { color: '#8b5cf6', type: 'dashed' } }]
      } : undefined
    }]
  };

  chartInstance.setOption(option);
};

// ✅ ДОБАВЬТЕ ЭТО В TEMPLATE в секцию кнопок:
const makeGuess = async (direction: 'long' | 'short') => {
  if (gameState.value !== 'playing') return;

  console.log('🎯 Отправляем на сервер:', direction, segmentId.value);

  gameState.value = 'result';

  const serverResponse = await sendGuess(direction);

  if (!serverResponse?.success) {
    console.error('❌ Ошибка сервера');
    gameState.value = 'playing';
    safeShowAlert('Ошибка сервера');
    return;
  }

  gameResult.value = serverResponse.data.isCorrect ? 'win' : 'lose';
  serverMessage.value = serverResponse.data.message;

  const resultCandles: Candle[] = serverResponse.data.resultCandles.map((c: ServerCandle) => ({
    date: new Date(c.t).toLocaleString('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }),
    open: Number(c.o.toFixed(2)),
    high: Number(c.h.toFixed(2)),
    low: Number(c.l.toFixed(2)),
    close: Number(c.c.toFixed(2)),
    volume: Number(c.v.toFixed(2))
  }));

  const fullData = [...allCandles.value.slice(0, visibleCandlesCount.value), ...resultCandles];
  initChart(fullData, true);

  showResultModal.value = true;
  progress.value = 100;

  if (progressInterval) clearInterval(progressInterval);
  progressInterval = setInterval(() => {
    progress.value -= 1;
  }, 10);

  timer = setTimeout(async () => {
    showResultModal.value = false;
    if (progressInterval) clearInterval(progressInterval);

    if (serverResponse.data.isCorrect) {
      score.value += 10;
      streak.value++;
    } else {
      streak.value = 0;
    }

    await loadNewRound();
  }, 2000);
};

const setGameMode = (mode: 'candle' | 'trend') => {
  if (gameState.value !== 'playing') return;
  gameMode.value = mode;
  loadNewRound();
};

onMounted(async () => {
  await loadNewRound();

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

