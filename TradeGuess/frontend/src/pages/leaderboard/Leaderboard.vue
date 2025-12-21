<template>
  <div class="h-[100dvh] w-full flex flex-col items-center justify-center p-4 relative bg-[#09090b] overflow-hidden pb-[120px] md:pb-4">
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
    <div class="relative z-10 w-full max-w-[360px] sm:max-w-[500px] md:max-w-[800px] lg:max-w-[1000px] xl:max-w-[1200px] flex flex-col items-center bg-zinc-900/90 border border-zinc-800 backdrop-blur-2xl shadow-[0_20px_80px_-20px_rgba(0,0,0,0.8)] overflow-hidden transition-all duration-500 rounded-[48px] h-fit max-h-[85vh]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-emerald-500/40 to-transparent"></div>
      <div class="relative w-full flex items-center justify-center p-5 sm:p-6 md:p-8 shrink-0">
        <h1 class="text-xl sm:text-2xl md:text-3xl font-black text-white uppercase tracking-tighter leading-none text-center">РЕЙТИНГ</h1>
      </div>
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-4 sm:px-8 pb-4 flex flex-col gap-4">
        <div v-if="loading" class="flex flex-col flex-1 items-center justify-center gap-4 py-20">
          <div class="w-10 h-10 border-4 border-zinc-800 border-t-emerald-500 rounded-full animate-spin"></div>
          <span class="text-zinc-500 text-[10px] font-black uppercase tracking-widest leading-none">Синхронизация...</span>
        </div>
        <template v-else-if="leaderboard?.entries?.length">
          <div class="flex items-end justify-center gap-2 sm:gap-4 md:gap-8 pt-12 sm:pt-20 pb-4 relative shrink-0">
            <div class="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(16,185,129,0.05)_0%,transparent_70%)] pointer-events-none"></div>
            <div v-if="leaderboard.entries[1]" class="flex flex-col items-center gap-2 w-24 sm:w-32 md:w-44 animate-fade-in-up">
              <div class="relative z-10 transition-transform duration-500 hover:-translate-y-1">
                <div class="w-16 h-16 sm:w-20 lg:w-28 lg:h-28 aspect-square rounded-full bg-zinc-800 border-2 sm:border-4 border-zinc-400 flex items-center justify-center overflow-hidden shadow-[0_10px_40px_rgba(161,161,170,0.3)] shrink-0">
                  <span class="text-xl sm:text-2xl md:text-4xl font-black text-zinc-300 uppercase leading-none">{{ leaderboard.entries[1].user.firstName?.charAt(0) }}</span>
                </div>
                <div class="absolute -top-2.5 -right-1.5 w-8 h-8 sm:w-10 sm:h-10 bg-zinc-400 rounded-full flex items-center justify-center border-4 border-zinc-900 shadow-xl text-zinc-900 font-black text-xs sm:text-lg leading-none">🥈</div>
              </div>
              <div class="text-center flex flex-col gap-0.5 z-10 items-center">
                <p class="text-white font-bold text-[10px] sm:text-xs md:text-lg truncate w-20 sm:w-28 md:w-40 leading-none pb-0.5">{{ leaderboard.entries[1].user.firstName || leaderboard.entries[1].user.username }}</p>
                <p class="text-zinc-400 font-mono font-black text-xs sm:text-base md:text-xl">{{ leaderboard.entries[1].rating.toFixed(0) }}</p>
              </div>
              <div class="w-full h-24 bg-zinc-800/80 border border-zinc-700/50 backdrop-blur-md rounded-2xl shadow-2xl"></div>
            </div>
            <div v-if="leaderboard.entries[0]" class="flex flex-col items-center gap-2 sm:gap-3 w-28 sm:w-40 md:w-60 relative group animate-fade-in-up delay-100">
              <div class="relative z-20 transition-transform duration-500 hover:-translate-y-2 overflow-visible">
                <div class="absolute top-[-25%] sm:top-[-30%] left-1/2 -translate-x-1/2 text-5xl sm:text-7xl md:text-8xl animate-float drop-shadow-glow-yellow z-30 pointer-events-none">👑</div>
                <div class="w-20 h-20 sm:w-24 lg:w-36 lg:h-36 aspect-square rounded-full bg-zinc-800 border-2 sm:border-4 border-yellow-500 flex items-center justify-center overflow-hidden shadow-[0_0_60px_rgba(234,179,8,0.3)] relative shrink-0">
                  <span class="text-2xl sm:text-4xl md:text-6xl font-black text-yellow-500 uppercase leading-none">{{ leaderboard.entries[0].user.firstName?.charAt(0) }}</span>
                </div>
                <div class="absolute -top-2 -right-1 w-10 h-10 sm:w-12 sm:h-12 md:w-14 md:h-14 bg-yellow-500 rounded-full flex items-center justify-center border-4 border-zinc-900 shadow-2xl text-zinc-900 font-black text-sm sm:text-xl md:text-2xl leading-none">🥇</div>
              </div>
              <div class="text-center flex flex-col gap-0.5 z-20 items-center">
                <p class="text-white font-black text-[11px] sm:text-sm md:text-xl truncate w-24 sm:w-36 md:w-56 tracking-tight leading-none pb-0.5">{{ leaderboard.entries[0].user.firstName || leaderboard.entries[0].user.username }}</p>
                <p class="text-yellow-500 font-mono font-black text-base sm:text-2xl lg:text-5xl leading-tight">{{ leaderboard.entries[0].rating.toFixed(0) }}</p>
              </div>
              <div class="w-full h-32 bg-gradient-to-b from-yellow-500/20 to-zinc-800/80 border-2 border-yellow-500/40 backdrop-blur-md rounded-2xl shadow-[0_0_100px_rgba(0,0,0,0.6)]"></div>
            </div>
            <div v-if="leaderboard.entries[2]" class="flex flex-col items-center gap-2 w-24 sm:w-32 md:w-44 animate-fade-in-up delay-200">
              <div class="relative z-10 transition-transform duration-500 hover:-translate-y-1">
                <div class="w-16 h-16 sm:w-20 lg:w-28 lg:h-28 aspect-square rounded-full bg-zinc-800 border-2 sm:border-4 border-amber-700 flex items-center justify-center overflow-hidden shadow-[0_10px_40px_rgba(180,83,9,0.3)] shrink-0">
                  <span class="text-xl sm:text-2xl md:text-4xl font-black text-amber-700 uppercase leading-none">{{ leaderboard.entries[2].user.firstName?.charAt(0) }}</span>
                </div>
                <div class="absolute -top-3 -right-1 w-7 h-7 sm:w-9 lg:w-10 bg-amber-700 rounded-full flex items-center justify-center border-2 border-zinc-900 shadow-xl text-zinc-900 font-black text-xs sm:text-base leading-none">🥉</div>
              </div>
              <div class="text-center flex flex-col gap-0.5 z-10 items-center">
                <p class="text-white font-bold text-[10px] sm:text-xs md:text-lg truncate w-20 sm:w-28 md:w-40 leading-none pb-0.5">{{ leaderboard.entries[2].user.firstName || leaderboard.entries[2].user.username }}</p>
                <p class="text-zinc-400 font-mono font-black text-sm sm:text-base lg:text-xl leading-tight">{{ leaderboard.entries[2].rating.toFixed(0) }}</p>
              </div>
              <div class="w-full h-16 bg-zinc-800/80 border border-zinc-700/50 backdrop-blur-md rounded-2xl shadow-2xl"></div>
            </div>
          </div>
          <div class="flex flex-col gap-2 w-full max-w-[900px] mx-auto pb-4">
            <div v-for="(entry, idx) in leaderboard.entries" :key="entry.user.id" class="relative w-full border transition-all duration-300 flex items-center justify-between px-3 py-1.5 sm:px-4 sm:py-2.5 overflow-hidden shrink-0 rounded-full" :class="[
            idx < 3 ? 'hidden' : 'bg-zinc-800/40 border-zinc-800 hover:border-zinc-700',
            entry.isCurrentUser ? 'ring-2 ring-emerald-500/50 bg-emerald-500/5' : ''
          ]">
              <div class="flex items-center gap-3 sm:gap-4 flex-1 min-w-0 z-10">
                <div class="flex items-center justify-center font-black shrink-0 w-8 text-zinc-500 font-mono text-xs sm:text-sm">#{{ entry.rank }}</div>
                <div class="w-8 h-8 sm:w-10 lg:w-11 lg:h-11 rounded-full bg-gradient-to-br from-zinc-700 to-zinc-800 flex items-center justify-center text-zinc-200 font-bold border border-white/10 shrink-0 shadow-sm uppercase text-[10px] sm:text-xs">
                  {{ entry.user.firstName?.charAt(0) }}
                </div>
                <div class="flex flex-col min-w-0">
                  <div class="flex items-center gap-2 leading-tight">
                    <span class="text-white font-bold truncate text-xs sm:text-base md:text-lg leading-none">{{ entry.user.firstName || entry.user.username }}</span>
                    <span v-if="entry.isCurrentUser" class="bg-emerald-500 text-black px-1.5 py-0.5 rounded text-[7px] font-black tracking-tighter uppercase leading-none">ВЫ</span>
                  </div>
                  <span class="text-zinc-500 text-[9px] truncate uppercase tracking-widest font-bold opacity-60">@{{ entry.user.username }}</span>
                </div>
              </div>
              <div class="flex flex-col items-end shrink-0 z-10 pl-4">
                <span class="font-black text-sm sm:text-xl font-mono leading-none tracking-tighter text-emerald-400 leading-none">{{ entry.rating.toFixed(0) }}</span>
                <span class="text-[8px] font-black text-zinc-600 uppercase tracking-widest pt-0.5 opacity-60 text-right leading-none">{{ entry.accuracy }}% ACC</span>
              </div>
            </div>
          </div>
        </template>
      </div>
      <div class="w-full flex shrink-0 bg-zinc-950/60 border-t border-zinc-800/50 p-3 sm:p-5 items-center justify-center backdrop-blur-md">
        <div class="flex bg-zinc-900 p-0.5 sm:p-1 rounded-xl sm:rounded-2xl border border-zinc-800 shadow-inner w-full max-w-[280px] sm:max-w-[340px]">
          <button v-for="p in periods" :key="p.value" @click="currentPeriod = p.value; fetchLeaderboard(p.value)" class="flex-1 py-1.5 sm:py-2 rounded-lg sm:rounded-xl text-[9px] sm:text-[10px] font-black uppercase transition-all duration-300" :class="[currentPeriod === p.value ? 'bg-zinc-800 text-white shadow-lg border border-zinc-700' : 'text-zinc-500 hover:text-zinc-300']">
            {{ p.label }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

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
  }
  rating: number
  accuracy: number
  isCurrentUser: boolean
}

interface LeaderboardData {
  calculatedAt: string
  entries: LeaderboardEntry[]
}

const leaderboard = ref<LeaderboardData | null>(null)
const loading = ref(true)
const currentPeriod = ref<'weekly' | 'monthly' | 'all-time'>('weekly')

const periods = [
  { value: 'weekly' as const, label: '7Д' },
  { value: 'monthly' as const, label: '30Д' },
  { value: 'all-time' as const, label: 'ВСЁ' }
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

onMounted(() => {
  fetchLeaderboard('weekly')
})
</script>

<style>
.bg-puzzle-pattern {
  background-color: #09090b;
  background-image: url("data:image/svg+xml,%3Csvg width='200' height='200' viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cdefs%3E%3Cpath id='p' d='M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 0 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37' stroke-linecap='square' stroke-linejoin='round' fill='none'/%3E%3C/defs%3E%3Cuse href='%23p' stroke='%23000000' stroke-width='4' opacity='0.6' transform='translate(1,1)'/%3E%3Cuse href='%23p' stroke='%23ffffff' stroke-width='1.5' opacity='0.1'/%3E%3C/svg%3E");
  background-size: 150px 150px;
  background-repeat: repeat;
}
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

@keyframes fade-in-up {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up { animation: fade-in-up 0.8s cubic-bezier(0.16, 1, 0.3, 1) forwards; }

@keyframes float {
  0%, 100% { transform: translate(-50%, 0); }
  50% { transform: translate(-50%, -6px); }
}
.animate-float { animation: float 3s ease-in-out infinite; }

@keyframes pulse-slow {
  0%, 100% { transform: scale(1); opacity: 0.15; }
  50% { transform: scale(1.1); opacity: 0.25; }
}
.animate-pulse-slow { animation: pulse-slow 5s ease-in-out infinite; }

.drop-shadow-glow-yellow { filter: drop-shadow(0 0 20px rgba(234, 179, 8, 0.4)); }
</style>
