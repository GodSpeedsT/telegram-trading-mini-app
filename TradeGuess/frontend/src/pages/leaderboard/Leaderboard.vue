<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

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

<template>
  <div class="min-h-screen flex flex-col justify-center items-center p-4 relative bg-zinc-950 overflow-hidden">
    <div class="absolute inset-0 w-full h-full pointer-events-none"></div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none"></div>

    <div class="relative z-10 w-full flex flex-col items-center overflow-hidden bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[600px] xl:max-w-[700px] 2xl:max-w-[800px] pt-6 sm:pt-8 md:pt-10 lg:pt-12 xl:pt-14 2xl:pt-16 pb-8 sm:pb-10 md:pb-12 lg:pb-14 xl:pb-16 2xl:pb-20 rounded-[30px] sm:rounded-[40px] lg:rounded-[50px] 2xl:rounded-[60px]">

      <!-- Декоративная полоса сверху -->
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700 to-transparent opacity-50 animate-pulse"></div>

      <!-- Заголовок -->
      <div class="flex flex-col items-center gap-4 pb-8 sm:pb-9 md:pb-10 lg:pb-12 xl:pb-14 2xl:pb-16">
        <div class="w-20 h-20 sm:w-24 sm:h-24 md:w-28 md:h-28 lg:w-32 lg:h-32 xl:w-36 xl:h-36 2xl:w-40 2xl:h-40 rounded-2xl bg-gradient-to-br from-emerald-500/20 to-blue-500/20 border-4 border-emerald-400/30 flex items-center justify-center shadow-2xl">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 sm:w-12 sm:h-12 md:w-14 md:h-14 lg:w-16 lg:h-16 text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
        </div>
        <div class="text-center">
          <h1 class="text-2xl sm:text-3xl md:text-4xl lg:text-5xl xl:text-6xl 2xl:text-7xl font-bold bg-gradient-to-r from-white via-zinc-100 to-zinc-300 bg-clip-text text-transparent leading-tight">
            Лидерборд
          </h1>
          <div class="flex items-center justify-center gap-2 mt-2">
            <div class="flex bg-zinc-800/50 border border-zinc-700/50 rounded-full px-3 py-1.5">
              <button
                v-for="period in periods"
                :key="period.value"
                @click="currentPeriod = period.value; fetchLeaderboard(period.value)"
                class="px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-300 relative"
                :class="[
                  currentPeriod === period.value
                    ? 'bg-emerald-500 text-white shadow-lg shadow-emerald-500/25'
                    : 'text-zinc-400 hover:text-zinc-200 hover:bg-zinc-700/50'
                ]"
              >
                {{ period.label }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Список лидеров -->
      <div class="w-full px-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px] lg:max-w-[450px] xl:max-w-[500px] 2xl:max-w-[550px] space-y-3 mb-6">

        <!-- Загрузка -->
        <div v-if="loading" class="flex flex-col items-center gap-4 py-12">
          <div class="w-12 h-12 border-4 border-zinc-700/50 border-t-emerald-400 rounded-full animate-spin"></div>
          <span class="text-zinc-400 text-sm font-medium">Загрузка лидерборда...</span>
        </div>

        <!-- Пустой список -->
        <div v-else-if="!leaderboard?.entries?.length" class="flex flex-col items-center gap-4 py-12 text-center">
          <div class="w-20 h-20 bg-zinc-800 rounded-2xl flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-zinc-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 01.806 1.946c.08.373.12.784.12 1.21V18a2 2 0 01-2 2h-4.586a2 2 0 00-1.414.586l-2.828 2.828A2 2 0 016.586 20H4a2 2 0 01-2-2V8.853c0-.426.04-.837.12-1.21a3.42 3.42 0 01.806-1.946z" />
            </svg>
          </div>
          <div class="text-zinc-400">
            <p class="text-lg font-medium">Никто ещё не соревнуется</p>
            <p class="text-sm">Будьте первыми в рейтинге!</p>
          </div>
        </div>

        <!-- Топ-3 -->
        <div v-else class="space-y-3">
          <!-- 1-е место -->
          <div
            v-for="(entry, index) in leaderboard.entries.slice(0, 3)"
            :key="entry.user.id"
            class="group relative bg-gradient-to-r from-zinc-800/50 to-zinc-900/50 hover:from-zinc-700/70 hover:to-zinc-800/70 border border-zinc-700/50 hover:border-zinc-600/70 rounded-3xl p-6 transition-all duration-300 hover:shadow-2xl hover:shadow-zinc-900/50 hover:-translate-y-1"
            :class="{ 'border-emerald-500/50 bg-gradient-to-r from-emerald-500/10 via-zinc-800/50 to-zinc-900/50 shadow-emerald-500/20': entry.isCurrentUser }"
          >
            <div class="absolute -left-3 -top-3 w-12 h-12 bg-gradient-to-br from-yellow-400 via-yellow-300 to-yellow-400 rounded-2xl flex items-center justify-center text-2xl font-black text-zinc-900 shadow-2xl shadow-yellow-500/30 group-hover:scale-110 transition-all duration-300">
              {{ entry.rank }}
            </div>

            <div class="flex items-center gap-4">
              <div class="flex flex-col items-center flex-1 min-w-0">
                <div class="w-16 h-16 rounded-2xl bg-gradient-to-br from-zinc-700 to-zinc-800 border-3 border-zinc-600 flex items-center justify-center shadow-xl group-hover:border-emerald-400 transition-all duration-300">
                  <span class="text-2xl font-black text-zinc-200">{{ entry.rank === 1 ? '👑' : entry.rank === 2 ? '🥈' : '🥉' }}</span>
                </div>
                <div class="text-center mt-2">
                  <span class="text-zinc-300 font-bold text-lg truncate block">{{ entry.user.firstName || entry.user.username }}</span>
                  <span class="text-zinc-500 text-xs">@{{ entry.user.username }}</span>
                </div>
              </div>

              <div class="text-right flex-1">
                <div class="text-3xl font-black bg-gradient-to-r from-emerald-400 to-emerald-500 bg-clip-text text-transparent">
                  {{ entry.rating.toFixed(1) }}
                </div>
                <div class="text-zinc-400 text-sm font-medium">Рейтинг</div>
                <div class="flex items-center gap-1 text-sm mt-1">
                  <span class="text-emerald-400 font-bold">{{ entry.accuracy }}%</span>
                  <span class="text-zinc-500">точность</span>
                </div>
              </div>
            </div>

            <!-- Текущий пользователь индикатор -->
            <div v-if="entry.isCurrentUser" class="absolute top-4 right-4 w-6 h-6 bg-emerald-500 border-2 border-white rounded-full flex items-center justify-center text-xs font-bold text-white shadow-lg">
              Вы
            </div>
          </div>

          <!-- Остальные места -->
          <div
            v-for="(entry, index) in leaderboard.entries.slice(3)"
            :key="entry.user.id"
            class="group bg-zinc-800/30 hover:bg-zinc-700/40 border border-zinc-700/30 hover:border-zinc-600/50 rounded-2xl p-4 transition-all duration-300 hover:shadow-xl hover:shadow-zinc-900/30 hover:-translate-y-0.5 flex items-center gap-4"
            :class="{ 'border-emerald-400/50 bg-emerald-500/5': entry.isCurrentUser }"
          >
            <div class="w-10 h-10 bg-zinc-700/50 rounded-xl flex items-center justify-center text-zinc-400 font-bold text-lg flex-shrink-0">
              #{{ entry.rank }}
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-gradient-to-br from-zinc-700 to-zinc-800 flex items-center justify-center text-zinc-400 font-semibold text-sm flex-shrink-0">
                  {{ entry.user.firstName?.charAt(0) || entry.user.username.charAt(0) }}
                </div>
                <div>
                  <span class="text-white font-bold text-base truncate block">{{ entry.user.firstName || entry.user.username }}</span>
                  <span class="text-zinc-500 text-xs">@{{ entry.user.username }}</span>
                </div>
              </div>
            </div>
            <div class="text-right flex-shrink-0">
              <div class="text-xl font-bold text-zinc-100">{{ entry.rating.toFixed(1) }}</div>
              <div class="text-emerald-400 text-sm font-medium">{{ entry.accuracy }}%</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Кнопка назад -->
      <button @click="goBack" class="relative w-full bg-zinc-800/50 hover:bg-zinc-700/50 border border-zinc-700/50 hover:border-zinc-500/50 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[60px] sm:h-[64px] md:h-[68px] lg:h-[72px] xl:h-[76px] 2xl:h-[80px] mt-2">
        <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-zinc-400 bg-zinc-500/5 rounded-full w-9 h-9 sm:w-10 sm:h-10 md:w-11 md:h-11 lg:w-12 lg:h-12 xl:w-13 xl:h-13 2xl:w-14 2xl:h-14">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5 sm:w-5 sm:h-5 md:w-6 md:h-6 lg:w-6 lg:h-6 xl:w-7 xl:h-7 2xl:w-8 2xl:h-8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 15L3 9m0 0l6-6M3 9h12a6 6 0 010 12h-3" />
          </svg>
        </div>
        <div class="text-center flex flex-col">
          <span class="text-zinc-300 font-bold leading-tight text-lg sm:text-lg md:text-xl lg:text-xl xl:text-2xl 2xl:text-2xl">Назад</span>
        </div>
      </button>

      <!-- Дата обновления -->
      <div v-if="leaderboard" class="mt-6 text-center">
        <div class="flex items-center justify-center gap-2 px-4 py-2 bg-zinc-800/30 border border-zinc-700/30 rounded-full mx-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px]">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-zinc-500" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
          </svg>
          <span class="text-xs text-zinc-400 font-medium">Обновлено: {{ new Date(leaderboard.calculatedAt).toLocaleString('ru-RU') }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Анимации для плавности */
@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-4px); }
}

.group:hover .float-anim {
  animation: float 2s ease-in-out infinite;
}
</style>
