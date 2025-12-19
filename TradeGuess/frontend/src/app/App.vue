<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')
const authLoading = ref(false)

watch(
  () => route.path,
  newPath => {
    showNavigation.value = newPath !== '/'
  },
  { immediate: true }
)

// Функция проверки существующего токена
const checkExistingAuth = () => {
  const token = localStorage.getItem('token')
  return !!token
}

const authenticateUser = async (force = false) => {
  // Если уже авторизован и не принудительно - пропускаем
  if (!force && checkExistingAuth()) {
    console.log('Токен уже существует, пропускаем авторизацию')
    return true
  }

  const tg = telegramWebApp

  if (!tg) {
    console.warn('Telegram WebApp недоступен')
    authError.value = 'Telegram WebApp недоступен'
    return false
  }

  // Проверяем наличие данных пользователя
  if (!tg.initDataUnsafe?.user) {
    console.warn('Данные пользователя Telegram не получены')
    authError.value = 'Данные пользователя не получены'

    // Если есть токен, но нет данных пользователя - очищаем
    if (localStorage.getItem('token')) {
      localStorage.removeItem('token')
    }

    return false
  }

  const user = tg.initDataUnsafe.user

  // Проверяем минимально необходимые данные
  if (!user.id) {
    console.warn('Отсутствует telegramId пользователя')
    authError.value = 'Отсутствует ID пользователя'
    return false
  }

  authLoading.value = true
  authError.value = ''

  try {
    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        telegramId: user.id,
        username: user.username || '',
        firstName: user.first_name || '',
        lastName: user.last_name || '',
        languageCode: user.language_code || '',
        isPremium: user.is_premium || false
      })
    })

    const data = await response.json()

    if (response.ok && data.success) {
      // Сохраняем токен если он есть в ответе
      if (data.data?.token) {
        localStorage.setItem('token', data.data.token)
      }

      // Сохраняем данные пользователя в localStorage для быстрого доступа
      localStorage.setItem('userData', JSON.stringify({
        telegramId: user.id,
        username: user.username,
        firstName: user.first_name,
        lastName: user.last_name
      }))

      isAuthenticated.value = true

      // Показываем уведомление только если это новая авторизация
      if (!checkExistingAuth()) {
        tg.showAlert('✅ Авторизация успешна!')
      }

      // Скрываем статус через 3 секунды
      setTimeout(() => {
        isAuthenticated.value = false
      }, 3000)

      return true
    } else {
      const errorMessage = data.message || 'Ошибка сервера при авторизации'
      throw new Error(errorMessage)
    }
  } catch (error: any) {
    console.error('Ошибка авторизации:', error)

    // Более информативное сообщение об ошибке
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      authError.value = 'Ошибка подключения к серверу'
    } else {
      authError.value = error.message || 'Неизвестная ошибка'
    }

    telegramWebApp?.showAlert(`❌ Ошибка: ${authError.value}`)

    // Очищаем токен при ошибке
    localStorage.removeItem('token')
    localStorage.removeItem('userData')

    return false
  } finally {
    authLoading.value = false
  }
}

// Принудительная авторизация (например, по кнопке "Войти")
const forceAuthenticate = async () => {
  return await authenticateUser(true)
}

onMounted(async () => {
  const token = localStorage.getItem('token')

  if (token) {
    // Токен есть - проверяем его валидность
    console.log('Найден существующий токен, проверяем...')

    // Можно добавить дополнительную проверку токена на сервере
    // пока просто пытаемся обновить данные пользователя
    await authenticateUser(false)
  } else {
    // Токена нет - делаем полную авторизацию
    console.log('Токен не найден, выполняем авторизацию...')
    await authenticateUser(false)
  }
})

// Экспортируем функции для использования в других компонентах
defineExpose({
  forceAuthenticate,
  checkExistingAuth,
  authenticateUser
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <!-- Индикатор загрузки -->
    <div
      v-if="authLoading"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-blue-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-blue-400 shadow-2xl flex items-center gap-3"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center animate-spin">
        <div class="w-4 h-4 border-2 border-white border-t-transparent rounded-full"></div>
      </div>
      <span class="font-bold text-white text-sm">Авторизация...</span>
    </div>

    <!-- Успешная авторизация -->
    <div
      v-if="isAuthenticated"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-emerald-400 shadow-2xl flex items-center gap-3 animate-fade-in"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✓</span>
      </div>
      <span class="font-bold text-white text-sm">Авторизация успешна</span>
    </div>

    <!-- Ошибка авторизации -->
    <div
      v-if="authError && !authLoading"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-red-400 shadow-2xl flex items-center gap-3 animate-fade-in"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✕</span>
      </div>
      <span class="font-bold text-white text-sm">Ошибка: {{ authError }}</span>
    </div>

    <!-- Основной контент -->
    <router-view />

    <!-- Навигационное меню -->
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>

<style>
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translate(-50%, -10px);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

.animate-fade-in {
  animation: fade-in 0.3s ease-out;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
}
</style>
