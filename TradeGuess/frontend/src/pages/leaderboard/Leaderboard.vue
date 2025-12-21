<template>
  <div class="h-screen w-full flex flex-col items-center justify-center p-3 sm:p-4 pb-[120px] sm:pb-[130px] pt-4 sm:pt-10 relative bg-[#09090b] overflow-hidden">
    <!-- Фоновый рисунок с пазлами -->
    <div class="absolute inset-0 w-full h-full pointer-events-none z-0">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="puzzlePattern" x="0" y="0" width="200" height="200" patternUnits="userSpaceOnUse">
            <path id="p" d="M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 0 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37" fill="none" stroke="white" stroke-width="1.5" opacity="0.08" />
            <use href="#p" stroke="black" stroke-width="4" opacity="0.6" transform="translate(1,1)" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#puzzlePattern)" />
      </svg>
    </div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none z-1"></div>
    
    <!-- Основной контейнер -->
    <div class="relative z-10 w-full max-w-[95%] xs:max-w-[450px] sm:max-w-[500px] md:max-w-[600px] lg:max-w-[700px] xl:max-w-[800px] flex flex-col items-center bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] overflow-hidden transition-all duration-500 rounded-2xl sm:rounded-[32px] md:rounded-[40px] max-h-[calc(100dvh-140px)] sm:max-h-[calc(100dvh-180px)]">
      
      <!-- Декоративная полоса сверху -->
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-0.5 sm:h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>
      
      <!-- Верхний блок с заголовком и переключателем -->
      <div class="flex flex-col items-center shrink-0 w-full px-4 xs:px-5 pt-4 sm:pt-6 md:pt-8 gap-3 sm:gap-4 md:gap-5">
        <!-- Иконка -->
        <div class="w-12 h-12 sm:w-14 sm:h-14 md:w-16 md:h-16 bg-zinc-800 rounded-full border-2 border-zinc-700 flex items-center justify-center shadow-xl relative overflow-visible shrink-0 transition-transform hover:scale-105">
          <div class="relative flex items-center justify-center w-full h-full">
            <svg viewBox="0 0 24 24" fill="currentColor" class="absolute w-10 h-10 sm:w-12 sm:h-12 text-yellow-500 blur-xl opacity-40 animate-pulse" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
            <svg viewBox="0 0 24 24" fill="currentColor" class="relative w-5 h-5 sm:w-6 sm:h-6 md:w-7 md:h-7 text-yellow-400 drop-shadow-[0_0_8px_rgba(234,179,8,0.7)]" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
          </div>
        </div>
        
        <!-- Заголовок и переключатель периода -->
        <div class="flex flex-col items-center gap-2 sm:gap-3 md:gap-4">
          <h1 class="text-xl xs:text-2xl sm:text-2xl md:text-3xl font-black text-white uppercase tracking-tight leading-none">
            Лидерборд
          </h1>
          <div class="flex bg-zinc-950 p-1 rounded-xl sm:rounded-2xl border border-zinc-800 gap-0.5 sm:gap-1 shadow-inner">
            <button 
              v-for="period in periods" 
              :key="period.value" 
              @click="currentPeriod = period.value; fetchLeaderboard(period.value)" 
              class="px-3 py-1.5 xs:px-4 xs:py-2 rounded-lg sm:rounded-xl text-[9px] xs:text-[10px] sm:text-xs font-bold uppercase transition-all duration-300 whitespace-nowrap"
              :class="[
                currentPeriod === period.value
                  ? 'bg-zinc-800 text-white shadow border border-zinc-700/50'
                  : 'text-zinc-500 hover:text-zinc-300'
              ]">
              {{ period.label }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- Список лидеров -->
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-3 sm:px-5 md:px-6 lg:px-8 py-4 sm:py-5 md:py-6 flex flex-col gap-2 sm:gap-3">
        <!-- Состояние загрузки -->
        <div v-if="loading" class="flex flex-col flex-1 items-center justify-center gap-3 sm:gap-4 py-8 sm:py-10">
          <div class="w-8 h-8 sm:w-10 sm:h-10 border-3 sm:border-4 border-zinc-800 border-t-emerald-500 rounded-full animate-spin"></div>
          <span class="text-zinc-500 text-[9px] sm:text-[10px] font-bold uppercase tracking-wider">Загрузка...</span>
        </div>
        
        <!-- Пустой список -->
        <div v-else-if="!leaderboard?.entries?.length" class="flex flex-col flex-1 items-center justify-center py-8 sm:py-10 text-center">
          <p class="text-zinc-500 font-bold text-lg sm:text-xl uppercase tracking-tight opacity-40">Пусто</p>
        </div>
        
        <!-- Список участников -->
        <div v-else class="flex flex-col gap-1.5 sm:gap-2 md:gap-3">
          <div 
            v-for="entry in leaderboard.entries" 
            :key="entry.user.id" 
            class="relative w-full border transition-all duration-300 rounded-xl sm:rounded-[16px] md:rounded-[20px] flex items-center justify-between px-3 xs:px-4 py-2 xs:py-2.5 sm:py-3 md:py-4 overflow-hidden shrink-0"
            :class="[
              entry.rank === 1 ? 'bg-gradient-to-r from-yellow-500/15 via-zinc-800/80 to-transparent border-yellow-500/50 shadow-lg sm:scale-[1.01] z-10' :
              entry.rank === 2 ? 'bg-gradient-to-r from-zinc-300/10 via-zinc-800/80 to-transparent border-zinc-300/40 shadow-md' :
              entry.rank === 3 ? 'bg-gradient-to-r from-amber-600/10 via-zinc-800/80 to-transparent border-amber-600/40 shadow' :
              entry.isCurrentUser ? 'bg-emerald-500/10 border-emerald-500/50 shadow-emerald-500/10' :
              'bg-zinc-800/50 border-zinc-800 hover:bg-zinc-800/70'
            ]">
            
            <!-- Левая часть: ранг, аватар, имя -->
            <div class="flex items-center gap-2 xs:gap-3 sm:gap-4 flex-1 min-w-0 z-10">
              <!-- Ранг -->
              <div class="flex items-center justify-center font-bold shrink-0 w-6 xs:w-7 sm:w-8">
                <span v-if="entry.rank === 1" class="text-xl sm:text-2xl md:text-2xl filter drop-shadow-md">🥇</span>
                <span v-else-if="entry.rank === 2" class="text-lg sm:text-xl md:text-2xl filter drop-shadow-md">🥈</span>
                <span v-else-if="entry.rank === 3" class="text-lg sm:text-xl md:text-2xl filter drop-shadow-md">🥉</span>
                <span v-else class="text-zinc-500 font-mono text-xs xs:text-xs sm:text-sm">#{{ entry.rank }}</span>
              </div>
              
              <!-- Аватар -->
              <div class="w-7 h-7 xs:w-8 xs:h-8 sm:w-10 sm:h-10 md:w-11 md:h-11 rounded-full bg-gradient-to-br from-zinc-700 to-zinc-800 flex items-center justify-center text-zinc-200 font-bold border border-white/10 shrink-0 shadow text-[9px] xs:text-[10px] sm:text-xs uppercase overflow-hidden">
                {{ entry.user.firstName?.charAt(0) || entry.user.username.charAt(0) }}
              </div>
              
              <!-- Имя и username -->
              <div class="flex flex-col min-w-0 flex-1">
                <div class="flex items-center gap-1 sm:gap-1.5 leading-tight">
                  <span class="text-white font-bold truncate text-xs xs:text-sm sm:text-base">
                    {{ entry.user.firstName || entry.user.username }}
                  </span>
                  <span 
                    v-if="entry.isCurrentUser" 
                    class="bg-emerald-500 text-black px-1 xs:px-1.5 py-0.5 rounded text-[6px] xs:text-[7px] sm:text-[8px] font-bold tracking-tighter shadow-sm shrink-0">
                    ВЫ
                  </span>
                </div>
                <span class="text-zinc-500 text-[8px] xs:text-[9px] sm:text-[10px] truncate font-semibold opacity-60 uppercase tracking-wider">
                  @{{ entry.user.username }}
                </span>
              </div>
            </div>
            
            <!-- Правая часть: рейтинг и точность -->
            <div class="flex flex-col items-end shrink-0 z-10 pl-2 xs:pl-3">
              <span 
                class="font-bold text-sm xs:text-base sm:text-lg font-mono leading-none tracking-tight"
                :class="[
                  entry.rank === 1 ? 'text-yellow-400' : 
                  entry.rank === 2 ? 'text-zinc-200' : 
                  entry.rank === 3 ? 'text-amber-500' : 
                  'text-emerald-400'
                ]">
                {{ entry.rating.toFixed(0) }}
              </span>
              <span class="text-[7px] xs:text-[8px] sm:text-[9px] font-bold text-zinc-600 uppercase tracking-wider opacity-60">
                {{ entry.accuracy }}% ACC
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Нижний блок с кнопкой назад -->
      <div class="w-full flex flex-col shrink-0 bg-zinc-900 border-t border-zinc-800 z-40 px-4 xs:px-5 sm:px-6 md:px-8 py-4 sm:py-5 gap-2 sm:gap-3">
        <!-- Кнопка Назад -->
        <button 
          @click="goBack" 
          class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-xl sm:rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[52px] sm:h-[56px] md:h-[60px]">
          <div class="absolute left-3 xs:left-4 flex items-center justify-center text-zinc-400 bg-zinc-500/10 rounded-full w-8 h-8 xs:w-9 xs:h-9 sm:w-10 sm:h-10">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-4 h-4 xs:w-5 xs:h-5 sm:w-5 sm:h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
            </svg>
          </div>
          <span class="text-zinc-300 font-bold uppercase tracking-[0.2em] xs:tracking-[0.25em] text-xs xs:text-sm">Назад</span>
        </button>
        
        <!-- Время обновления -->
        <div v-if="leaderboard" class="flex items-center justify-center">
          <div class="px-3 py-1 xs:px-4 xs:py-1.5 bg-zinc-950/60 border border-zinc-800 rounded-full flex items-center gap-2 backdrop-blur-sm shadow-inner">
            <div class="w-1.5 h-1.5 xs:w-2 xs:h-2 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_8px_rgba(16,185,129,0.6)]"></div>
            <span class="text-[8px] xs:text-[9px] text-zinc-600 font-bold uppercase tracking-wider whitespace-nowrap">
              Обновлено: {{ new Date(leaderboard.calculatedAt).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' }) }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

defineOptions({
  name: 'LeaderboardPage'
})

interface LeaderboardEntry {
  rank: number
  user: {
    id: number
    username: string
    firstName: string | null
    lastName: string | null
    isPremium: boolean
    createdAt: string
  }
  attempts: number
  correct: number
  accuracy: number
  rating: number
  change: number
  currentUser: boolean
  isCurrentUser: boolean
}

interface LeaderboardData {
  period: string
  calculatedAt: string
  entries: LeaderboardEntry[]
}

const router = useRouter()
const leaderboard = ref<LeaderboardData | null>(null)
const loading = ref(true)
const currentPeriod = ref<'weekly' | 'monthly' | 'all-time'>('weekly')

const periods = [
  { value: 'weekly' as const, label: 'Неделя' },
  { value: 'monthly' as const, label: 'Месяц' },
  { value: 'all-time' as const, label: 'Все время' }
]

const fetchLeaderboard = async (period: string) => {
  try {
    loading.value = true
    const response = await fetch(`https://tradeguess-backend.onrender.com/api/leaderboard?period=${period}`)
    const data = await response.json()
    if (data.success) {
      leaderboard.value = data.data
    }
  } catch (error) {
    console.error('Ошибка загрузки лидерборда:', error)
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.back()
}

onMounted(() => {
  fetchLeaderboard('weekly')
})
</script>

<style scoped>
/* Скрытие скроллбара */
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

</style>