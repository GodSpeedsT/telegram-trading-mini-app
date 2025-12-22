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

    <!-- Основной контейнер с translate-y-[-5vh] -->
    <div class="relative z-10 w-full max-w-[98%] sm:max-w-[550px] md:max-w-[650px] lg:max-w-[700px] xl:max-w-[750px] flex flex-col items-center bg-zinc-900/90 border border-zinc-800 backdrop-blur-2xl shadow-[0_20px_80px_-20px_rgba(0,0,0,0.8)] overflow-hidden transition-all duration-500 rounded-[32px] md:rounded-[40px] h-fit max-h-[90vh] md:translate-y-[2vh] lg:translate-y-[-5vh]">

      <!-- Декоративная полоска сверху -->
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-emerald-500/40 to-transparent"></div>

      <!-- Аватар профиля сверху -->
      <div class="flex flex-col items-center shrink-0 w-full px-4 sm:px-6 md:px-8 py-6 md:py-8 bg-zinc-900/50 border-b border-zinc-800/50 gap-4">
        <div class="w-28 h-28 sm:w-32 sm:h-32 md:w-36 md:h-36 rounded-full bg-gradient-to-br from-zinc-800 to-zinc-900 border-3 border-zinc-700 flex items-center justify-center overflow-hidden shadow-xl">
          <img v-if="userAvatar" :src="userAvatar" :alt="userName" class="w-full h-full object-cover rounded-full" />
          <svg v-else class="h-4/5 w-4/5 text-zinc-400" viewBox="0 0 24 24" fill="currentColor">
            <path fill-rule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z" clip-rule="evenodd" />
          </svg>
        </div>
        <div class="flex flex-col items-center text-center gap-1">
          <h1 class="text-xl sm:text-2xl md:text-3xl font-black text-white uppercase tracking-tight">
            {{ userName || 'Гость' }}
          </h1>
          <div class="flex items-center gap-1.5 px-3 py-1.5 bg-zinc-800/50 border border-zinc-700/50 rounded-full">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 text-zinc-500" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
            </svg>
            <span class="text-xs sm:text-sm md:text-base text-zinc-400 font-medium font-mono">
              @{{ userAccountName || 'no_username' }}
            </span>
          </div>
        </div>
      </div>

      <!-- Содержимое профиля -->
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-4 sm:px-6 md:px-8 py-6 md:py-8">
        <div class="flex flex-col items-center gap-4 md:gap-5">

          <!-- Кнопки действий друг под другом -->
          <div class="w-full flex flex-col gap-3 md:gap-4">

            <!-- Статистика -->
            <button @click="viewStats"
                    class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-emerald-500 rounded-2xl shadow-lg flex items-center transition-all duration-300 active:scale-95 h-20 md:h-22 group p-5">
              <div class="flex items-center justify-center text-emerald-400 bg-emerald-500/10 rounded-full w-12 h-12 md:w-13 md:h-13">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-6 h-6 md:w-7 md:h-7">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
                </svg>
              </div>
              <div class="text-left flex flex-col ml-5">
                <span class="text-white font-bold text-lg md:text-xl lg:text-2xl">Статистика</span>
                <span class="text-zinc-400 font-medium text-sm md:text-base">Результаты и прогресс</span>
              </div>
            </button>

            <!-- Рейтинг -->
            <button @click="leaderboard"
                    class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-yellow-500 rounded-2xl shadow-lg flex items-center transition-all duration-300 active:scale-95 h-20 md:h-22 group p-5">
              <div class="flex items-center justify-center text-yellow-400 bg-yellow-500/10 rounded-full w-12 h-12 md:w-13 md:h-13">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-6 h-6 md:w-7 md:h-7">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0a7.454 7.454 0 01-.982-3.172M9.497 14.25a7.454 7.454 0 00.981-3.172M5.25 4.236c-.982.143-1.954.317-2.916.52A6.003 6.003 0 007.73 9.728M5.25 4.236V4.5c0 2.108.966 3.99 2.48 5.228M5.25 4.236V2.721C7.456 2.41 9.71 2.25 12 2.25c2.291 0 4.545.16 6.75.47v1.516M7.73 9.728a6.726 6.726 0 002.748 1.35m8.272-6.842V4.5c0 2.108-.966 3.99-2.48 5.228m2.48-5.492a46.32 46.32 0 012.916.52 6.003 6.003 0 01-5.395 4.972m0 0a6.726 6.726 0 01-2.749 1.35m0 0a6.772 6.772 0 01-3.044 0" />
                </svg>
              </div>
              <div class="text-left flex flex-col ml-5">
                <span class="text-white font-bold text-lg md:text-xl lg:text-2xl">Рейтинг</span>
                <span class="text-zinc-400 font-medium text-sm md:text-base">Топ игроков</span>
              </div>
            </button>

            <!-- Админ-панель (если доступна) -->
            <button v-if="isAdmin && !isLoadingAdmin" @click="viewAdmin"
                    class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-emerald-500/30 hover:border-emerald-500 rounded-2xl shadow-lg flex items-center transition-all duration-300 active:scale-95 h-20 md:h-22 group p-5">
              <div class="flex items-center justify-center text-emerald-400 bg-emerald-500/10 rounded-full w-12 h-12 md:w-13 md:h-13">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-6 h-6 md:w-7 md:h-7">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93.398.164.855.142 1.205-.108l.737-.527a1.125 1.125 0 011.45.132l.773.772c.3.3.398.743.249 1.14l-.324.873c-.15.404-.083.864.218 1.17l.617.616a1.125 1.125 0 010 1.59l-.617.617a1.125 1.125 0 01-1.17.217l-.873-.324a1.125 1.125 0 01-1.14.249l-.772.773a1.125 1.125 0 01-1.14.249l-.873-.324a1.125 1.125 0 01-1.17.217l-.617.617a1.125 1.125 0 01-1.59 0l-.617-.617a1.125 1.125 0 01-.217-1.17l.324-.873a1.125 1.125 0 01-.249-1.14l-.773-.772a1.125 1.125 0 01-.249-1.14l.324-.873a1.125 1.125 0 01-.217-1.17l-.617-.617a1.125 1.125 0 010-1.59l.617-.617a1.125 1.125 0 011.17-.217l.873.324a1.125 1.125 0 011.14-.249l.772-.773a1.125 1.125 0 011.14-.249l.873.324a1.125 1.125 0 011.17-.217l.617-.617a1.125 1.125 0 011.59 0l.617.617z" />
                  <circle cx="12" cy="12" r="3" />
                </svg>
              </div>
              <div class="text-left flex flex-col ml-5">
                <span class="text-white font-bold text-lg md:text-xl lg:text-2xl">Админ-панель</span>
                <span class="text-zinc-400 font-medium text-sm md:text-base">Управление системой</span>
              </div>
            </button>

          </div>

          <!-- Кнопка Назад -->
          <div class="w-full pt-4 md:pt-6">
            <button @click="goBack"
                    class="w-full bg-zinc-800/50 hover:bg-zinc-700/50 border border-zinc-700/50 hover:border-zinc-500/50 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-14 md:h-16 p-4">
              <div class="flex items-center justify-center text-zinc-400 bg-zinc-500/5 rounded-full w-10 h-10">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
                </svg>
              </div>
              <span class="text-zinc-400 font-black uppercase text-sm md:text-base tracking-widest ml-3">Назад</span>
            </button>
          </div>

        </div>
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
const adminUsers = ref<any[]>([])
const isAdminChecked = ref(false)
const isLoadingAdmin = ref(false)

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
