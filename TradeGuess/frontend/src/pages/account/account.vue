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

      <div class="flex flex-col items-center gap-3 sm:gap-3 md:gap-4 lg:gap-5 xl:gap-6 2xl:gap-7 pb-8 sm:pb-9 md:pb-10 lg:pb-12 xl:pb-14 2xl:pb-16">
        <div class="relative group cursor-pointer">
          <div class="w-28 h-28 sm:w-32 sm:h-32 md:w-36 md:h-36 lg:w-40 lg:h-40 xl:w-44 xl:h-44 2xl:w-48 2xl:h-48 rounded-full bg-gradient-to-br from-zinc-800 to-zinc-900 border-4 border-zinc-700 flex items-center justify-center overflow-hidden shadow-2xl transition-all duration-300 group-hover:border-zinc-500 group-hover:scale-105">
            <img v-if="userAvatar" :src="userAvatar" :alt="userName" class="w-full h-full object-cover rounded-full" />
            <svg v-else class="h-5/6 w-5/6 text-zinc-400" viewBox="0 0 24 24" fill="currentColor">
              <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>

        <div class="flex flex-col items-center gap-1">
          <span class="text-xl sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-4xl font-bold text-white uppercase tracking-tighter">
            {{ userName || 'Гость' }}
          </span>
          <div class="flex items-center gap-2 text-zinc-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
            </svg>
            <span class="text-sm sm:text-sm md:text-base lg:text-base xl:text-lg 2xl:text-lg font-medium font-mono">
              @{{ userAccountName || 'no_username' }}
            </span>
          </div>
        </div>

        <div class="flex items-center gap-2 px-4 py-2 bg-zinc-800/50 border border-zinc-700/50 rounded-full backdrop-blur-sm">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-zinc-500" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
          </svg>
          <span class="text-[10px] md:text-xs text-zinc-400 font-black uppercase tracking-widest leading-none pt-0.5">
            Updated: {{ lastLoginTime }}
          </span>
        </div>
      </div>

      <div class="w-full flex flex-col gap-4 px-6 max-w-[340px] sm:max-w-[350px] md:max-w-[400px] lg:max-w-[450px] xl:max-w-[500px] 2xl:max-w-[550px]">

        <button v-if="isAdmin && !isLoadingAdmin" @click="viewAdmin" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-emerald-500/30 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] group">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-emerald-400 bg-emerald-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 sm:w-6 sm:h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93.398.164.855.142 1.205-.108l.737-.527a1.125 1.125 0 011.45.132l.773.772c.3.3.398.743.249 1.14l-.324.873c-.15.404-.083.864.218 1.17l.617.616a1.125 1.125 0 010 1.59l-.617.617a1.125 1.125 0 01-1.17.217l-.873-.324a1.125 1.125 0 01-1.14.249l-.772.773a1.125 1.125 0 01-1.14.249l-.873-.324a1.125 1.125 0 01-1.17.217l-.617.617a1.125 1.125 0 01-1.59 0l-.617-.617a1.125 1.125 0 01-.217-1.17l.324-.873a1.125 1.125 0 01-.249-1.14l-.773-.772a1.125 1.125 0 01-.249-1.14l.324-.873a1.125 1.125 0 01-.217-1.17l-.617-.617a1.125 1.125 0 010-1.59l.617-.617a1.125 1.125 0 011.17-.217l.873.324a1.125 1.125 0 011.14-.249l.772-.773a1.125 1.125 0 011.14-.249l.873.324a1.125 1.125 0 011.17-.217l.617-.617a1.125 1.125 0 011.59 0l.617.617z" />
              <circle cx="12" cy="12" r="3" />
            </svg>
          </div>
          <div class="text-center flex flex-col items-center">
            <span class="text-white font-black uppercase tracking-widest text-sm sm:text-base lg:text-lg group-hover:text-emerald-400 transition-colors">Админ-панель</span>
            <span class="text-zinc-500 font-bold text-[9px] uppercase tracking-tighter">Управление системой</span>
          </div>
        </button>

        <button @click="viewStats" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] group">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-emerald-400 bg-emerald-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-8 lg:h-8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
            </svg>
          </div>
          <div class="text-center flex flex-col items-center">
            <span class="text-white font-bold text-sm sm:text-base md:text-lg">Статистика</span>
            <span class="text-zinc-400 font-medium text-[10px] sm:text-xs">Результаты и прогресс</span>
          </div>
        </button>

        <button @click="leaderboard" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-zinc-500 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[68px] sm:h-[72px] md:h-[76px] lg:h-[80px] group">
          <div class="absolute left-3 sm:left-4 md:left-5 lg:left-6 flex items-center justify-center text-yellow-400 bg-yellow-500/10 rounded-full w-10 h-10 sm:w-11 sm:h-11 md:w-12 md:h-12 lg:w-13 lg:h-13">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-8 lg:h-8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
            </svg>
          </div>
          <div class="text-center flex flex-col items-center">
            <span class="text-white font-bold text-sm sm:text-base md:text-lg">Лидерборд</span>
            <span class="text-zinc-400 font-medium text-[10px] sm:text-xs">Топ игроков</span>
          </div>
        </button>

        <button @click="goBack" class="relative w-full bg-zinc-800/50 hover:bg-zinc-700/50 border border-zinc-700/50 hover:border-zinc-500/50 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[56px] sm:h-[62px]">
          <div class="absolute left-4 flex items-center justify-center text-zinc-400 bg-zinc-500/5 rounded-full w-8 h-8 md:w-10 md:h-10">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" class="w-4 h-4 md:w-5 md:h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
          </div>
          <span class="text-zinc-400 font-black uppercase text-[10px] md:text-sm tracking-widest">Назад</span>
        </button>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { telegramWebApp } from '@/app/main.ts'

const router = useRouter()
const userAvatar = ref('')
const userName = ref('Гость')
const userAccountName = ref('no_username')
const lastLoginTime = ref('')
const adminUsers = ref<any[]>([])
const isAdminChecked = ref(false)
const isLoadingAdmin = ref(false)
const totalScore = ref(0)
const maxStreak = ref(0)

const checkAdminStatus = async () => {
  isLoadingAdmin.value = true
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      console.warn('Токен не найден')
      return
    }

    const response = await fetch('https://tradeguess-backend.onrender.com/api/admin/users', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`)
    }

    const data = await response.json()

    if (data.success && Array.isArray(data.data)) {
      adminUsers.value = data.data
      isAdminChecked.value = true
    } else {
      console.warn('Неверный формат ответа API')
    }
  } catch (error) {
    console.error('Ошибка проверки админа:', error)
  } finally {
    isLoadingAdmin.value = false
  }
}

const localIsAdmin = computed(() => {
  try {
    const userData = JSON.parse(localStorage.getItem('userData') || '{}')
    return userData.role === 'ROLE_SUPER_ADMIN' ||
      userData.role === 'ROLE_ADMIN' ||
      userData.telegramId === 934084397
  } catch {
    return false
  }
})

// Финальная проверка - API + local fallback
const isAdmin = computed(() => {
  return isAdminChecked.value ? adminUsers.value.length > 0 : localIsAdmin.value
})

onMounted(async () => {
  const tg = telegramWebApp
  await checkAdminStatus()
  if (tg?.initDataUnsafe?.user) {
    const user = tg.initDataUnsafe.user
    userName.value = user.first_name || 'Гость'
    userAccountName.value = user.username || 'no_username'

    if (user.photo_url) {
      userAvatar.value = user.photo_url
    } else {
      userAvatar.value = `https://ui-avatars.com/api/?name=${encodeURIComponent(userName.value)}&background=2b6cb0&color=fff&size=128&bold=true`
    }
  }
  lastLoginTime.value = new Date().toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })
})

const viewStats = () => router.push('/stats')
const leaderboard = () => router.push('/leaderboard')
const viewAdmin = () => router.push('/admin')
const goBack = () => router.back()
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>
