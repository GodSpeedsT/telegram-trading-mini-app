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
  (newPath, oldPath) => {
    console.log(`[ROUTE] Изменение пути: ${oldPath || 'N/A'} → ${newPath}`)
    showNavigation.value = newPath !== '/'
    console.log(`[ROUTE] showNavigation обновлен: ${showNavigation.value}`)
  },
  { immediate: true }
)

const authenticateUser = async () => {
  console.log('[AUTH] Начало процесса авторизации')

  try {
    const tg = telegramWebApp

    if (!tg) {
      console.error('[AUTH] Telegram WebApp недоступен')
      authError.value = 'Telegram WebApp недоступен'
      return false
    }

    if (!tg.initDataUnsafe?.user) {
      console.warn('[AUTH] initDataUnsafe.user отсутствует')
      authError.value = 'Данные пользователя недоступны'
      return false
    }

    const user = tg.initDataUnsafe.user
    console.log('[AUTH] Данные пользователя:', {
      id: user.id,
      username: user.username || 'N/A',
      firstName: user.first_name || 'N/A'
    })

    console.log('[AUTH] Отправка запроса на сервер...')
    const requestBody = {
      telegramId: user.id,
      username: user.username || '',
      firstName: user.first_name || ''
    }
    console.log('[AUTH] Тело запроса:', requestBody)

    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestBody)
    })

    console.log('[AUTH] Ответ сервера:', {
      status: response.status,
      statusText: response.statusText,
      ok: response.ok
    })

    // ✅ КРИТИЧНО: всегда проверяем response.ok ПЕРЕД .json()
    if (!response.ok) {
      const errorText = await response.text()
      console.error('[AUTH] HTTP ошибка:', response.status, errorText)
      throw new Error(`HTTP ${response.status}: ${errorText || 'Серверная ошибка'}`)
    }

    const data = await response.json()
    console.log('[AUTH] Данные ответа:', data)

    if (data.success && data.data?.token) {
      console.log('[AUTH] Авторизация успешна')
      localStorage.setItem('token', data.data.token)
      isAuthenticated.value = true
      tg.showAlert('✅ Авторизация успешна!')

      setTimeout(() => {
        isAuthenticated.value = false
      }, 5000)

      return true
    } else {
      const errorMsg = data.message || 'Ошибка сервера'
      console.error('[AUTH] Логическая ошибка:', errorMsg)
      throw new Error(errorMsg)
    }

  } catch (error: any) {
    console.error('[AUTH] Критическая ошибка:', error)
    authError.value = error.message || 'Неизвестная ошибка'
    telegramWebApp?.showAlert(`❌ ${error.message}`)
    return false
  }
}

// ✅ ИСПРАВЛЕННЫЙ onMounted с try-catch
onMounted(async () => {
  try {
    console.log('[LIFECYCLE] onMounted выполнен')

    const token = localStorage.getItem('token')
    console.log('[STORAGE] Токен в localStorage:', !!token)

    if (token) {
      console.log('[STORAGE] Токен найден, авторизация пропущена')
      return
    }

    console.log('[AUTH] Запуск авторизации')
    await authenticateUser()

  } catch (error: any) {
    console.error('[LIFECYCLE] Ошибка в onMounted:', error)
    authError.value = `Ошибка инициализации: ${error.message}`
  }
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
      <span class="font-bold text-white text-sm">{{ authError }}</span>
    </div>

    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>
