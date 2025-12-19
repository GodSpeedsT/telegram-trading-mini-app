<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')

watch(
  () => route.path,
  newPath => {
    showNavigation.value = newPath !== '/'
  },
  { immediate: true }
)

const authenticateUser = async () => {
  const tg = telegramWebApp

  if (!tg || !tg.initDataUnsafe?.user) {
    console.warn('Telegram WebApp недоступен')
    return false
  }

  const user = tg.initDataUnsafe.user

  console.log('Тело запроса:', {
    telegramId: user.id,
    username: user.username || '',
    firstName: user.first_name || '',
    initData: tg.initData
  })

  try {
    console.log('🚀 Отправляем запрос на бэкенд...')
    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        initData: tg.initData,
        telegramId: user.id,
        username: user.username || '',
        firstName: user.first_name || ''
      })
    })

    const data = await response.json()

    if (response.ok && data.success && data.data?.token) {
      localStorage.setItem('token', data.data.token)
      isAuthenticated.value = true
      tg.showAlert('✅ Авторизация успешна!')

      setTimeout(() => {
        isAuthenticated.value = false
      }, 5000)

      return true
    } else {
      throw new Error(data.message || 'Ошибка сервера')
    }
  } catch (error: any) {
    console.error('Авторизация:', error)
    authError.value = error.message
    telegramWebApp?.showAlert(`❌ ${error.message}`)
    return false
  }
}

onMounted(async () => {
  if (localStorage.getItem('token')) {
    return
  }
  await authenticateUser()
  console.log('⚡ authenticateUser вызван')
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">

    <div
      v-if="isAuthenticated"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-emerald-400 shadow-2xl flex items-center gap-3"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✓</span>
      </div>
      <span class="font-bold text-white text-sm">Авторизация успешна</span>
    </div>

    <div
      v-if="authError"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-red-400 shadow-2xl flex items-center gap-3"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✕</span>
      </div>
      <span class="font-bold text-white text-sm">Ошибка: {{ authError }}</span>
    </div>

    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>
