<template>
  <div class="min-h-screen flex flex-col justify-center items-center p-4 relative bg-zinc-950 overflow-hidden">
    <div class="absolute inset-0 w-full h-full bg-puzzle-pattern pointer-events-none"></div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none"></div>
    <div class="relative z-10 w-full flex flex-col items-center overflow-hidden bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[600px] xl:max-w-[700px] 2xl:max-w-[800px] pt-6 sm:pt-8 md:pt-10 lg:pt-12 xl:pt-14 2xl:pt-16 pb-8 sm:pb-10 md:pb-12 lg:pb-14 xl:pb-16 2xl:pb-20 rounded-[30px] sm:rounded-[40px] lg:rounded-[50px] 2xl:rounded-[60px]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700 to-transparent opacity-50 animate-pulse"></div>
      <div class="flex flex-col items-center gap-3 sm:gap-3 md:gap-4 lg:gap-5 xl:gap-6 2xl:gap-7 pb-6 sm:pb-7 md:pb-8 lg:pb-9 xl:pb-10 2xl:pb-12">
        <!-- ✅ Заменяем заглушку на аватар пользователя -->
        <div class="w-24 h-24 sm:w-28 sm:h-28 md:w-32 md:h-32 lg:w-36 lg:h-36 lg:border-4 xl:w-40 xl:h-40 2xl:w-44 2xl:h-44 rounded-full border-2 border-zinc-600 flex items-end justify-center overflow-hidden shadow-lg bg-gradient-to-br from-zinc-700 to-zinc-800">
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
        <!-- ✅ Имя пользователя вместо "Гость" -->
        <span class="text-xl sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-4xl font-bold text-white">
          {{ userName || 'Гость' }}
        </span>
      </div>
      <!-- Остальной код без изменений -->
      <div class="w-full flex flex-col gap-5 px-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px] lg:max-w-[450px] xl:max-w-[500px] 2xl:max-w-[550px]">
        <!-- Все кнопки без изменений -->
        <button @click="market" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-green-400 bg-green-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Live Рынок</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Реальное время</span>
          </div>
        </button>
        <button @click="trainer" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-purple-400 bg-purple-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Тренажер</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Исторические данные</span>
          </div>
        </button>
        <button @click="achievements" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-yellow-400 bg-yellow-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Достижения</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Задания и награды</span>
          </div>
        </button>
        <button @click="account" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-blue-400 bg-blue-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold leading-tight text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl">Аккаунт</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Профиль и статистика</span>
          </div>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import router from '@/app/router/index.js'
import {onMounted, ref} from 'vue'
import {telegramWebApp} from '@/app/main.ts'

function market() {
  router.push({ path: '/real-time' });
}

function trainer() {
  router.push({ path: '/trade-game' });
}

function achievements() {
  router.push({ path: '/achievements' });
}

function account() {
  router.push({ path: '/' });
}

// ✅ Данные пользователя из авторизации
const userName = ref('Гость')
const userAvatar = ref('')

// ✅ Получаем данные при монтировании
onMounted(() => {
  const tg = telegramWebApp
  if (tg?.initDataUnsafe?.user) {
    const user = tg.initDataUnsafe.user
    userName.value = user.first_name || 'Гость'

    // ✅ Генерируем аватар по имени (Telegram не дает photo_url)
    if (user.first_name) {
      userAvatar.value = `https://ui-avatars.com/api/?name=${encodeURIComponent(user.first_name)}&background=2b6cb0&color=fff&size=128&bold=true&font-size=0.6`
    }
  }
})
</script>

<style>
.bg-puzzle-pattern {
  background-color: #09090b;
  background-image: url("data:image/svg+xml,%3Csvg width='200' height='200' viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cdefs%3E%3Cpath id='p' d='M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 0 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37' stroke-linecap='square' stroke-linejoin='round' fill='none'/%3E%3C/defs%3E%3C!-- Shadow Layer (Black) --%3E%3Cuse href='%23p' stroke='%23000000' stroke-width='4' opacity='0.6' transform='translate(1,1)'/%3E%3C!-- Highlight Layer (White) --%3E%3Cuse href='%23p' stroke='%23ffffff' stroke-width='1.5' opacity='0.2'/%3E%3C/svg%3E");
  background-size: 150px 150px;
  background-repeat: repeat;
}
</style>
