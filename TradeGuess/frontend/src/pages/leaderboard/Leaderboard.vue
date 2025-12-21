<template>
  <div class="h-screen w-full flex flex-col items-center justify-center p-4 pb-[130px] pt-10 relative bg-[#09090b] overflow-hidden">
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
    <div class="relative z-10 w-[94%] sm:w-full max-w-[450px] md:max-w-[550px] lg:max-w-[650px] flex flex-col items-center bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] overflow-hidden transition-all duration-500 rounded-[32px] sm:rounded-[48px] max-h-[calc(100dvh-220px)]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>
      <div class="flex flex-col items-center shrink-0 w-full px-5 pt-6 sm:pt-10 gap-4 sm:gap-6">
        <div class="w-14 h-14 sm:w-16 sm:h-16 bg-zinc-800 rounded-full border-2 border-zinc-700 flex items-center justify-center shadow-2xl relative overflow-visible shrink-0 transition-transform hover:scale-110">
          <div class="relative flex items-center justify-center w-full h-full">
            <svg viewBox="0 0 24 24" fill="currentColor" class="absolute w-12 h-12 text-yellow-500 blur-xl opacity-40 animate-pulse" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
            <svg viewBox="0 0 24 24" fill="currentColor" class="relative w-6 h-6 sm:w-8 sm:h-8 text-yellow-400 drop-shadow-[0_0_12px_rgba(234,179,8,0.7)]" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
          </div>
        </div>
        <div class="flex flex-col items-center gap-3 sm:gap-5">
          <h1 class="text-2xl sm:text-3xl font-black text-white uppercase tracking-tighter leading-none">
            Лидерборд
          </h1>
          <div class="flex bg-zinc-950 p-1 rounded-2xl border border-zinc-800 gap-1 shadow-inner">
            <button v-for="period in periods" :key="period.value" @click="currentPeriod = period.value; fetchLeaderboard(period.value)" class="px-4 py-2 rounded-xl text-[10px] sm:text-xs font-black uppercase transition-all duration-300"
                    :class="[
                currentPeriod === period.value
                  ? 'bg-zinc-800 text-white shadow-lg border border-zinc-700/50'
                  : 'text-zinc-500 hover:text-zinc-300'
                ]">
              {{ period.label }}
            </button>
          </div>
        </div>
      </div>
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-4 sm:px-10 py-6 flex flex-col gap-3">
        <div v-if="loading" class="flex flex-col flex-1 items-center justify-center gap-4 py-10">
          <div class="w-10 h-10 border-4 border-zinc-800 border-t-emerald-500 rounded-full animate-spin"></div>
          <span class="text-zinc-500 text-[10px] font-black uppercase tracking-widest">Загрузка...</span>
        </div>
        <div v-else-if="!leaderboard?.entries?.length" class="flex flex-col flex-1 items-center justify-center py-10 text-center">
          <p class="text-zinc-500 font-black text-xl uppercase tracking-tighter opacity-40">Пусто</p>
        </div>
        <div v-else class="flex flex-col gap-2 sm:gap-3">
          <div v-for="entry in leaderboard.entries" :key="entry.user.id" class="relative w-full border transition-all duration-300 rounded-[16px] sm:rounded-[24px] flex items-center justify-between px-4 py-2.5 sm:py-4 overflow-hidden shrink-0"
               :class="[
              entry.rank === 1 ? 'bg-gradient-to-r from-yellow-500/15 via-zinc-800/80 to-transparent border-yellow-500/50 shadow-lg scale-[1.01] z-10' :
              entry.rank === 2 ? 'bg-gradient-to-r from-zinc-300/10 via-zinc-800/80 to-transparent border-zinc-300/40' :
              entry.rank === 3 ? 'bg-gradient-to-r from-amber-600/10 via-zinc-800/80 to-transparent border-amber-600/40' :
              entry.isCurrentUser ? 'bg-emerald-500/10 border-emerald-500/50 shadow-emerald-500/10' :
              'bg-zinc-800/50 border-zinc-800'
            ]">
            <div class="flex items-center gap-3 sm:gap-5 flex-1 min-w-0 z-10">
              <div class="flex items-center justify-center font-black shrink-0 w-8">
                <span v-if="entry.rank === 1" class="text-2xl filter drop-shadow-md">🥇</span>
                <span v-else-if="entry.rank === 2" class="text-xl filter drop-shadow-md">🥈</span>
                <span v-else-if="entry.rank === 3" class="text-xl filter drop-shadow-md">🥉</span>
                <span v-else class="text-zinc-500 font-mono text-xs sm:text-sm">#{{ entry.rank }}</span>
              </div>
              <div class="w-8 h-8 sm:w-11 sm:h-11 rounded-full bg-gradient-to-br from-zinc-700 to-zinc-800 flex items-center justify-center text-zinc-200 font-black border border-white/10 shrink-0 shadow-lg text-[10px] sm:text-xs uppercase overflow-hidden">
                {{ entry.user.firstName?.charAt(0) || entry.user.username.charAt(0) }}
              </div>
              <div class="flex flex-col min-w-0">
                <div class="flex items-center gap-1.5 leading-tight">
                  <span class="text-white font-bold truncate text-xs sm:text-base">
                    {{ entry.user.firstName || entry.user.username }}
                  </span>
                  <span v-if="entry.isCurrentUser" class="bg-emerald-500 text-black px-1.5 py-0.5 rounded text-[7px] sm:text-[8px] font-black tracking-tighter shadow-sm pl-1.5 pr-1.5">ВЫ</span>
                </div>
                <span class="text-zinc-500 text-[9px] sm:text-[10px] truncate font-bold opacity-60 uppercase tracking-widest pl-0">@{{ entry.user.username }}</span>
              </div>
            </div>
            <div class="flex flex-col items-end shrink-0 z-10 pl-3">
              <span class="font-black text-sm sm:text-lg font-mono leading-none tracking-tighter" :class="entry.rank === 1 ? 'text-yellow-400' : entry.rank === 2 ? 'text-zinc-200' : entry.rank === 3 ? 'text-amber-500' : 'text-emerald-400'">
                {{ entry.rating.toFixed(0) }}
              </span>
              <span class="text-[8px] sm:text-[9px] font-black text-zinc-600 uppercase tracking-widest pb-1 opacity-60 text-right">{{ entry.accuracy }}% ACC</span>
            </div>
          </div>
        </div>
      </div>
      <div class="w-full flex flex-col shrink-0 bg-zinc-900 border-t border-zinc-800 z-40 px-6 py-5 sm:px-10 sm:py-7 gap-3 sm:gap-4">
        <button @click="goBack" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-xl flex items-center justify-center transition-all duration-300 active:scale-95 h-[56px] sm:h-[68px]">
          <div class="absolute left-4 flex items-center justify-center text-zinc-400 bg-zinc-500/10 rounded-full w-9 h-9 sm:w-11 sm:h-11">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" class="w-5 h-5 sm:w-6 sm:h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
            </svg>
          </div>
          <span class="text-zinc-300 font-black uppercase tracking-[0.3em] text-xs sm:text-sm">Назад</span>
        </button>
        <div v-if="leaderboard" class="flex items-center justify-center">
          <div class="px-4 py-1.5 bg-zinc-950/60 border border-zinc-800 rounded-full flex items-center gap-2.5 backdrop-blur-sm shadow-inner">
            <div class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_12px_rgba(16,185,129,0.8)]"></div>
            <span class="text-[9px] text-zinc-600 font-black uppercase tracking-widest whitespace-nowrap pl-0">Updated: {{ new Date(leaderboard.calculatedAt).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' }) }}</span>
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
    console.error(error)
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
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
