<template>
  <div class="min-h-screen flex flex-col justify-center items-center">
    <div class="flex flex-col items-center gap-3 py-5">
      <div class="w-24 h-24 rounded-full bg-zinc-800 border-2 border-zinc-600 flex items-end justify-center overflow-hidden shadow-lg">
        <svg class="h-20 w-20 text-zinc-400" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
          <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
        </svg>
      </div>
      <span class="text-xl font-bold text-zinc-100">Гость</span>
    </div>
    <div class="w-full flex flex-col max-w-[300px] gap-5">
      <button @click="market" class=" h-[68px] bg-zinc-800 border border-zinc-700 rounded-2xl shadow-lg flex items-center justify-center gap-4 transition-transform active:scale-95">
        <div class="flex items-center justify-center text-green-400 bg-green-500/10 rounded-full w-10 h-10">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
          </svg>
        </div>
        <div class="text-center flex flex-col">
          <span class="text-white font-bold text-lg leading-tight">Live Рынок</span>
          <span class="text-xs text-zinc-400 font-medium">Реальное время</span>
        </div>
      </button>
      <button @click="trainer" class="h-[68px] bg-zinc-800 border border-zinc-700 rounded-2xl shadow-lg flex items-center justify-center gap-4 transition-transform active:scale-95">
        <div class="flex items-center justify-center text-purple-400 bg-purple-500/10 rounded-full w-10 h-10">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div class="text-center flex flex-col">
          <span class="text-white font-bold text-lg leading-tight">Тренажер</span>
          <span class="text-xs text-zinc-400 font-medium">Исторические данные</span>
        </div>
      </button>
      <button @click="achievements" class=" h-[68px] bg-zinc-800 border border-zinc-700 rounded-2xl shadow-lg flex items-center justify-center gap-4 transition-transform active:scale-95">
        <!-- Желтая иконка (Кубок) -->
        <div class="flex items-center justify-center text-yellow-400 bg-yellow-500/10 rounded-full w-10 h-10">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />
          </svg>
        </div>
        <!-- Текст -->
        <div class="text-center flex flex-col">
          <span class="text-white font-bold text-lg leading-tight">Достижения</span>
          <span class="text-xs text-zinc-400 font-medium">Задания и награды</span>
        </div>
      </button>
      <button @click="account" class=" h-[68px] bg-zinc-800 border border-zinc-700 rounded-2xl shadow-lg flex items-center justify-center gap-4 transition-transform active:scale-95">
        <!-- Синяя иконка (Пользователь/Настройки) -->
        <div class="flex items-center justify-center text-blue-400 bg-blue-500/10 rounded-full w-10 h-10">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
          </svg>
        </div>
        <!-- Текст -->
        <div class="text-center flex flex-col">
          <span class="text-white font-bold text-lg leading-tight">Аккаунт</span>
          <span class="text-xs text-zinc-400 font-medium">Профиль и статистика</span>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import router from '@/app/router/index.js'

function market() {
  router.push({ path: '/chart' });
}

function trainer() {
  router.push({ path: '/test' });
}

function achievements() {
  router.push({ path: '/test' });
}

function account() {
  router.push({ path: '/test' });
}
</script>

<!-- реализовать функции achievements и account +  сделать для них страницы -->
<!-- добавить оффект при наведдении на кнопку + адаптив для всего -->
