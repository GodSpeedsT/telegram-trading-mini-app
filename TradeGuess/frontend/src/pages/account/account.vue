<template>
  <div class="min-h-screen flex flex-col justify-center items-center p-4 relative bg-[#09090b] overflow-hidden">
    <!-- Фоновый рисунок с пазлами -->
    <div class="absolute inset-0 w-full h-full pointer-events-none z-0">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="puzzlePattern" x="0" y="0" width="200" height="200" patternUnits="userSpaceOnUse">
            <path id="p" d="M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 0 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37"
                  fill="none" stroke="white" stroke-width="1.5" opacity="0.08" />
            <use href="#p" stroke="black" stroke-width="4" opacity="0.6" transform="translate(1,1)" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#puzzlePattern)" />
      </svg>
    </div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none z-1"></div>

    <!-- Основной контейнер -->
    <div class="relative z-10 w-full flex flex-col items-center overflow-hidden bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[600px] xl:max-w-[700px] 2xl:max-w-[800px] pt-6 sm:pt-8 md:pt-10 lg:pt-12 xl:pt-14 2xl:pt-16 pb-8 sm:pb-10 md:pb-12 lg:pb-14 xl:pb-16 2xl:pb-20 rounded-[30px] sm:rounded-[40px] lg:rounded-[50px] 2xl:rounded-[60px]">

      <!-- Декоративная полоса сверху -->
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>

      <!-- Секция профиля -->
      <div class="flex flex-col items-center gap-3 sm:gap-3 md:gap-4 lg:gap-5 xl:gap-6 2xl:gap-7 pb-8 sm:pb-9 md:pb-10 lg:pb-12 xl:pb-14 2xl:pb-16">
        <!-- Аватарка -->
        <div class="relative group cursor-pointer">
          <div class="w-28 h-28 sm:w-32 sm:h-32 md:w-36 md:h-36 lg:w-40 lg:h-40 xl:w-44 xl:h-44 2xl:w-48 2xl:h-48 rounded-full bg-gradient-to-br from-zinc-800 to-zinc-900 border-4 border-zinc-700 flex items-center justify-center overflow-hidden shadow-2xl transition-all duration-300 group-hover:border-zinc-500 group-hover:scale-105">
            <img
              v-if="userAvatar"
              :src="userAvatar"
              :alt="userName"
              class="w-full h-full object-cover rounded-full"
            />
            <svg v-else class="h-5/6 w-5/6 text-zinc-400" viewBox="0 0 24 24" fill="currentColor">
              <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
            </svg>
          </div>
          <!-- Иконка редактирования -->
          <div class="absolute bottom-2 right-2 bg-zinc-800 border-2 border-zinc-700 rounded-full p-2 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-zinc-300" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
          </div>
        </div>

        <!-- Имя аккаунта -->
        <div class="flex flex-col items-center gap-1">
          <!-- ✅ Имя пользователя вместо "Гость" -->
          <span class="text-xl sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-4xl font-bold text-white">
            {{ userName || 'Гость' }}
          </span>
          <!-- Username -->
          <div class="flex items-center gap-2 text-zinc-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
            </svg>
            <span class="text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-lg font-medium">
              @{{ userAccountName || 'no_username' }}
            </span>
          </div>
        </div>

        <!-- Дата последнего захода -->
        <div class="flex items-center gap-2 px-4 py-2 bg-zinc-800/50 border border-zinc-700/50 rounded-full">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-zinc-500" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
          </svg>
          <span class="text-xs sm:text-xs md:text-sm lg:text-sm xl:text-base 2xl:text-base text-zinc-400 font-medium">
            Последний заход: {{ formatDate(user.lastLogin) }}
          </span>
        </div>
      </div>

      <!-- Кнопки действий -->
      <div class="w-full flex flex-col gap-5 px-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px] lg:max-w-[450px] xl:max-w-[500px] 2xl:max-w-[550px]">

        <!-- Статистика -->
        <button @click="viewStats" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-emerald-400 bg-emerald-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Статистика</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Результаты и прогресс</span>
          </div>
        </button>
        
        <!-- Лидерборд -->
        <button @click="leaderboard" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-pink-400 bg-yellow-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Лидерборд</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Топ игроков</span>
          </div>
        </button>

        <!-- Назад -->
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

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import router from '@/app/router/index.js'
import { useRouter } from 'vue-router'
import {telegramWebApp} from '@/app/main.ts'

const routerInstance = useRouter()
const userAvatar = ref('')
const userName = ref('Гость')
const userAccountName = ref()

// Данные пользователя
const user = ref({
  avatar: '', // URL аватарки
  fullName: 'Иван Петров',
  username: 'ivan_trader',
  lastLogin: new Date(), // Дата последнего захода
  stats: {
    totalTrades: 156,
    successRate: '72%',
    totalProfit: '+$12,450'
  }
})

// Форматирование даты
const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date)
}

// Загрузка данных пользователя
onMounted(() => {
  // Здесь можно загрузить реальные данные из API
  // Например:
  // user.value = await fetchUserData();
  const tg = telegramWebApp
  if (tg?.initDataUnsafe?.user) {
    const user = tg.initDataUnsafe.user
    userName.value = user.first_name || 'Гость'
    userAccountName.value = user.username || ''

    // ✅ Генерируем аватар по имени (Telegram не дает photo_url)
    if (user.photo_url) {
      userAvatar.value = user.photo_url
    } else if (user.first_name) {
      // fallback, если вдруг аватарки нет
      userAvatar.value =
        `https://ui-avatars.com/api/?name=${encodeURIComponent(user.first_name)}&background=2b6cb0&color=fff&size=128&bold=true&font-size=0.6`
    }
  }
  // Устанавливаем текущее время как время последнего захода
  user.value.lastLogin = new Date()
})

// Навигация
function viewStats() {
  router.push({ path: '/stats' })
}

function leaderboard() {
  router.push({ path: '/leaderboard' })
}

function goBack() {
  routerInstance.back()
}
</script>