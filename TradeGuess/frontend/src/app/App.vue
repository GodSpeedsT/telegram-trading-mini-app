<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

let telegramWebApp: any = null

const route = useRoute()
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

const safeShowAlert = (message: string) => {
  try {
    if (telegramWebApp?.showPopup) {
      telegramWebApp.showPopup({
        message,
        buttons: [{ text: 'OK', type: 'default' }]
      })
    } else if (telegramWebApp?.showAlert) {
      telegramWebApp.showAlert(message)
    } else {
      alert(message)
    }
  } catch (e) {
    console.warn('[ALERT]', message)
  }
}

const collectUserData = () => {
  try {
    if (!telegramWebApp || !telegramWebApp.initDataUnsafe?.user) {
      return {
        telegramId: 999999,
        username: 'testuser',
        firstName: 'Test User',
        isReal: false
      }
    }
    const user = telegramWebApp.initDataUnsafe.user
    return {
      telegramId: user.id,
      username: user.username || '',
      firstName: user.first_name || '',
      isReal: true
    }
  } catch (error) {
    console.error('[AUTH] collectUserData error:', error)
    return {
      telegramId: 999999,
      username: 'testuser',
      firstName: 'Test User',
      isReal: false
    }
  }
}

const authenticateUser = async () => {
  try {
    const userData = collectUserData()
    console.log('[AUTH] Данные:', userData)

    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        telegramId: userData.telegramId,
        username: userData.username,
        firstName: userData.firstName
      })
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const data = await response.json()

    if (data.success && data.data?.token) {
      localStorage.setItem('token', data.data.token)
      isAuthenticated.value = true

      setTimeout(() => {
        isAuthenticated.value = false
      }, 5000)
      return true
    } else {
      throw new Error(data.message || 'Ошибка сервера')
    }
  } catch (error: any) {
    console.error('[AUTH] Ошибка:', error)
    authError.value = error.message || 'Ошибка авторизации'
    safeShowAlert(`❌ ${error.message}`)
    return false
  }
}

const initWebApp = () => {
  telegramWebApp = (window as any).Telegram?.WebApp || null

  if (!telegramWebApp) {
    console.warn('[TG] WebApp не найден')
    return
  }

  const version = parseFloat(telegramWebApp.version || '0')

  if (version < 6.0) {
    try {
      ;(telegramWebApp as any).setHeaderColor?.('#18181b')
      ;(telegramWebApp as any).setBackgroundColor?.('#18181b')
    } catch (e) {}
  }

  telegramWebApp.expand()
}

onMounted(async () => {
  try {
    initWebApp()

    if (localStorage.getItem('token')) {
      return
    }

    await authenticateUser()

  } catch (error: any) {
    console.error('[LIFECYCLE] onMounted error:', error)
    authError.value = 'Ошибка запуска: ' + (error.message || 'Неизвестно')
  }
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <div v-if="isAuthenticated" class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-emerald-400 shadow-2xl flex items-center gap-3">
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✓</span>
      </div>
      <span class="font-bold text-white text-sm">Авторизация успешна</span>
    </div>

    <div v-if="authError" class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-red-400 shadow-2xl flex items-center gap-3">
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✕</span>
      </div>
      <span class="font-bold text-white text-sm">{{ authError }}</span>
    </div>

    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>
