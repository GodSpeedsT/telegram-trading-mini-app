<template>
  <div class="min-h-screen flex flex-col justify-center items-center p-4 relative bg-[#09090b] overflow-hidden">
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
    <div class="relative z-10 w-full flex flex-col items-center overflow-hidden bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[600px] xl:max-w-[700px] 2xl:max-w-[800px] pt-6 sm:pt-8 md:pt-10 lg:pt-12 xl:pt-14 2xl:pt-16 pb-8 sm:pb-10 md:pb-12 lg:pb-14 xl:pb-16 2xl:pb-20 rounded-[30px] sm:rounded-[40px] lg:rounded-[50px] 2xl:rounded-[60px]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>
      <div class="flex flex-col items-center gap-3 sm:gap-3 md:gap-4 lg:gap-5 xl:gap-6 2xl:gap-7 pb-6 sm:pb-7 md:pb-8 lg:pb-9 xl:pb-10 2xl:pb-12">
        <div class="w-24 h-24 sm:w-28 sm:h-28 md:w-32 md:h-32 lg:w-36 lg:h-36 lg:border-4 xl:w-40 xl:h-40 2xl:w-44 2xl:h-44 rounded-full border-2 border-zinc-600 flex items-end justify-center overflow-hidden shadow-lg bg-gradient-to-br from-zinc-700 to-zinc-800">
          <img v-if="userAvatar" :src="userAvatar" :alt="userName" class="w-full h-full object-cover rounded-full" />
          <svg v-else class="h-5/6 w-5/6 text-zinc-400" viewBox="0 0 24 24" fill="currentColor">
            <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
          </svg>
        </div>
        <span class="text-xl sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-4xl font-bold text-white tracking-tight">
          {{ userName || 'Гость' }}
        </span>
      </div>
      <div class="w-full flex flex-col gap-5 px-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px] lg:max-w-[450px] xl:max-w-[500px] 2xl:max-w-[550px]">
        <button @click="market" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-green-400 bg-green-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl leading-tight">Live Рынок</span>
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
            <span class="text-white font-bold text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl leading-tight">Тренажер</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Исторические данные</span>
          </div>
        </button>
        <button @click="achievements" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] xl:h-[84px] 2xl:h-[88px]">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-yellow-400 bg-yellow-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13 xl:w-14 xl:h-14 2xl:w-16 2xl:h-16">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-7 lg:h-7 xl:w-8 xl:h-8 2xl:w-9 2xl:h-9">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-white font-bold text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl leading-tight">Достижения</span>
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
            <span class="text-white font-bold text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl leading-tight">Аккаунт</span>
            <span class="text-zinc-400 font-medium text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-xl">Профиль и статистика</span>
          </div>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { telegramWebApp } from '@/app/main'

const router = useRouter()
const userName = ref('Гость')
const userAvatar = ref('')

const market = () => router.push('/real-time')
const trainer = () => router.push('/trade-game')
const achievements = () => router.push('/achievements')
const account = () => router.push('/account')

onMounted(() => {
  const tg = telegramWebApp
  if (tg?.initDataUnsafe?.user) {
    const user = tg.initDataUnsafe.user
    userName.value = user.first_name || 'Гость'
    if (user.photo_url) {
      userAvatar.value = user.photo_url
    } else {
      userAvatar.value = `https://ui-avatars.com/api/?name=${encodeURIComponent(userName.value)}&background=27272a&color=fff`
    }
  }
})
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>
