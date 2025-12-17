<template>
  <div class="min-h-screen bg-gradient-to-b from-zinc-900 via-zinc-800/50 to-zinc-900 flex flex-col justify-center items-center px-4 py-8">
    <!-- Анимированный фон -->
    <div class="absolute inset-0 overflow-hidden opacity-20">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-500/30 rounded-full blur-3xl"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-blue-500/30 rounded-full blur-3xl"></div>
      <div class="absolute top-1/2 left-1/2 w-60 h-60 bg-emerald-500/20 rounded-full blur-3xl"></div>
    </div>

    <!-- Заголовок и пользователь -->
    <div class="relative z-10 mb-8 sm:mb-10 md:mb-12 text-center">
      <h1 class="text-3xl sm:text-4xl md:text-5xl font-bold mb-3">
        <span class="bg-gradient-to-r from-emerald-400 to-blue-400 bg-clip-text text-transparent">
          TradeGuess
        </span>
      </h1>
      <div class="flex items-center justify-center gap-3">
        <div class="relative group">
          <div class="w-16 h-16 md:w-18 md:h-18 lg:w-20 lg:h-20 rounded-full bg-gradient-to-br from-zinc-800 to-zinc-900 border-2 border-zinc-700/50 flex items-center justify-center overflow-hidden shadow-2xl group-hover:scale-105 transition-transform duration-300">
            <div class="absolute inset-0 bg-gradient-to-tr from-emerald-500/10 to-blue-500/10"></div>
            <svg class="h-10 w-10 md:h-12 md:w-12 text-zinc-300/80" viewBox="0 0 24 24" fill="currentColor">
              <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="absolute -bottom-2 left-1/2 transform -translate-x-1/2 bg-emerald-500/90 backdrop-blur-sm px-3 py-1 rounded-full border border-emerald-400/30 shadow-lg">
            <span class="text-xs font-semibold text-white">Гость</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Кнопки меню -->
    <div class="relative z-10 w-full max-w-md space-y-4">
      <!-- Live Рынок -->
      <button
        @click="market"
        class="group relative w-full bg-gradient-to-r from-zinc-800/80 to-zinc-900/80 backdrop-blur-sm border border-emerald-500/30 hover:border-emerald-400/50 rounded-2xl shadow-xl flex items-center p-4 transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] hover:shadow-emerald-500/10"
      >
        <div class="absolute inset-0 bg-gradient-to-r from-emerald-500/5 to-transparent rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

        <div class="relative flex items-center justify-center w-12 h-12 md:w-14 md:h-14 bg-gradient-to-br from-emerald-500/20 to-emerald-600/10 rounded-xl border border-emerald-500/30 group-hover:border-emerald-400/50 transition-colors duration-300 mr-4">
          <div class="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-emerald-400/30 via-transparent to-transparent rounded-xl"></div>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 md:w-7 md:h-7 text-emerald-400 group-hover:text-emerald-300 transition-colors duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
          </svg>
        </div>

        <div class="text-left flex-1">
          <div class="flex items-center gap-2 mb-1">
            <span class="text-white font-bold text-lg md:text-xl">Live Рынок</span>
            <span class="px-2 py-0.5 bg-emerald-500/20 border border-emerald-500/30 rounded-full text-xs text-emerald-300 font-medium">
              Реальное время
            </span>
          </div>
          <p class="text-zinc-400 text-sm group-hover:text-zinc-300 transition-colors duration-300">
            Торгуйте в реальном времени с актуальными данными
          </p>
        </div>

        <div class="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-2 group-hover:translate-x-0">
          <svg class="w-5 h-5 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </div>
      </button>

      <!-- Тренажер -->
      <button
        @click="trainer"
        class="group relative w-full bg-gradient-to-r from-zinc-800/80 to-zinc-900/80 backdrop-blur-sm border border-purple-500/30 hover:border-purple-400/50 rounded-2xl shadow-xl flex items-center p-4 transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] hover:shadow-purple-500/10"
      >
        <div class="absolute inset-0 bg-gradient-to-r from-purple-500/5 to-transparent rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

        <div class="relative flex items-center justify-center w-12 h-12 md:w-14 md:h-14 bg-gradient-to-br from-purple-500/20 to-purple-600/10 rounded-xl border border-purple-500/30 group-hover:border-purple-400/50 transition-colors duration-300 mr-4">
          <div class="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-purple-400/30 via-transparent to-transparent rounded-xl"></div>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 md:w-7 md:h-7 text-purple-400 group-hover:text-purple-300 transition-colors duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>

        <div class="text-left flex-1">
          <div class="flex items-center gap-2 mb-1">
            <span class="text-white font-bold text-lg md:text-xl">Тренажер</span>
            <span class="px-2 py-0.5 bg-purple-500/20 border border-purple-500/30 rounded-full text-xs text-purple-300 font-medium">
              Исторические данные
            </span>
          </div>
          <p class="text-zinc-400 text-sm group-hover:text-zinc-300 transition-colors duration-300">
            Отрабатывайте стратегии на исторических данных
          </p>
        </div>

        <div class="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-2 group-hover:translate-x-0">
          <svg class="w-5 h-5 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </div>
      </button>

      <!-- Достижения -->
      <button
        @click="achievements"
        class="group relative w-full bg-gradient-to-r from-zinc-800/80 to-zinc-900/80 backdrop-blur-sm border border-yellow-500/30 hover:border-yellow-400/50 rounded-2xl shadow-xl flex items-center p-4 transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] hover:shadow-yellow-500/10"
      >
        <div class="absolute inset-0 bg-gradient-to-r from-yellow-500/5 to-transparent rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

        <div class="relative flex items-center justify-center w-12 h-12 md:w-14 md:h-14 bg-gradient-to-br from-yellow-500/20 to-yellow-600/10 rounded-xl border border-yellow-500/30 group-hover:border-yellow-400/50 transition-colors duration-300 mr-4">
          <div class="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-yellow-400/30 via-transparent to-transparent rounded-xl"></div>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 md:w-7 md:h-7 text-yellow-400 group-hover:text-yellow-300 transition-colors duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />
          </svg>
        </div>

        <div class="text-left flex-1">
          <div class="flex items-center gap-2 mb-1">
            <span class="text-white font-bold text-lg md:text-xl">Достижения</span>
            <span class="px-2 py-0.5 bg-yellow-500/20 border border-yellow-500/30 rounded-full text-xs text-yellow-300 font-medium">
              0/12 выполнено
            </span>
          </div>
          <p class="text-zinc-400 text-sm group-hover:text-zinc-300 transition-colors duration-300">
            Выполняйте задания и получайте награды
          </p>
        </div>

        <div class="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-2 group-hover:translate-x-0">
          <svg class="w-5 h-5 text-yellow-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </div>
      </button>

      <!-- Аккаунт -->
      <button
        @click="account"
        class="group relative w-full bg-gradient-to-r from-zinc-800/80 to-zinc-900/80 backdrop-blur-sm border border-blue-500/30 hover:border-blue-400/50 rounded-2xl shadow-xl flex items-center p-4 transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] hover:shadow-blue-500/10"
      >
        <div class="absolute inset-0 bg-gradient-to-r from-blue-500/5 to-transparent rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

        <div class="relative flex items-center justify-center w-12 h-12 md:w-14 md:h-14 bg-gradient-to-br from-blue-500/20 to-blue-600/10 rounded-xl border border-blue-500/30 group-hover:border-blue-400/50 transition-colors duration-300 mr-4">
          <div class="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-blue-400/30 via-transparent to-transparent rounded-xl"></div>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 md:w-7 md:h-7 text-blue-400 group-hover:text-blue-300 transition-colors duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
          </svg>
        </div>

        <div class="text-left flex-1">
          <div class="flex items-center gap-2 mb-1">
            <span class="text-white font-bold text-lg md:text-xl">Аккаунт</span>
            <span class="px-2 py-0.5 bg-blue-500/20 border border-blue-500/30 rounded-full text-xs text-blue-300 font-medium">
              Профиль
            </span>
          </div>
          <p class="text-zinc-400 text-sm group-hover:text-zinc-300 transition-colors duration-300">
            Ваша статистика и настройки профиля
          </p>
        </div>

        <div class="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-2 group-hover:translate-x-0">
          <svg class="w-5 h-5 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </div>
      </button>
    </div>

    <!-- Статистика внизу -->
    <div class="relative z-10 mt-8 sm:mt-10 w-full max-w-md bg-gradient-to-r from-zinc-800/30 to-zinc-900/30 backdrop-blur-sm border border-zinc-700/50 rounded-2xl p-4">
      <div class="grid grid-cols-3 gap-4 text-center">
        <div>
          <div class="text-2xl font-bold text-emerald-400">0</div>
          <div class="text-xs text-zinc-400">Очков</div>
        </div>
        <div>
          <div class="text-2xl font-bold text-purple-400">0</div>
          <div class="text-xs text-zinc-400">Торгов</div>
        </div>
        <div>
          <div class="text-2xl font-bold text-yellow-400">0%</div>
          <div class="text-xs text-zinc-400">Точность</div>
        </div>
      </div>
    </div>

    <!-- Анимация точек -->
    <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-1">
      <div class="w-2 h-2 bg-emerald-400/60 rounded-full animate-pulse" style="animation-delay: 0s"></div>
      <div class="w-2 h-2 bg-purple-400/60 rounded-full animate-pulse" style="animation-delay: 0.2s"></div>
      <div class="w-2 h-2 bg-blue-400/60 rounded-full animate-pulse" style="animation-delay: 0.4s"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import router from '@/app/router/index.js'

function market() {
  router.push({ path: '/test' });
}

function trainer() {
  router.push({ path: '/chart' });
}

function achievements() {
  router.push({ path: '/test' });
}

function account() {
  router.push({ path: '/test' });
}
</script>
