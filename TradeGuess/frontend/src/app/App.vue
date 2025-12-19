<script setup lang="ts">
import { onMounted, ref, watch, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')
const isInitialized = ref(false)

watch(
  () => route.path,
  newPath => {
    showNavigation.value = newPath !== '/'
  },
  { immediate: true }
)

// Функция для получения Telegram WebApp с проверкой
const getTelegramWebApp = () => {
  // Проверяем разные варианты доступа к Telegram WebApp
  if (typeof window !== 'undefined') {
    // Вариант 1: Глобальный объект window
    if ((window as any).Telegram?.WebApp) {
      return (window as any).Telegram.WebApp
    }
    // Вариант 2: Через telegram-web-app скрипт
    if ((window as any).telegramWebApp) {
      return (window as any).telegramWebApp
    }
    // Вариант 3: Прямой доступ
    if ((window as any).TelegramWebApp) {
      return (window as any).TelegramWebApp
    }
  }
  return null
}

const authenticateUser = async () => {
  console.log('🔍 Начинаем авторизацию...')

  const tg = getTelegramWebApp()

  if (!tg) {
    console.warn('Telegram WebApp недоступен в глобальной области')
    authError.value = 'Telegram WebApp недоступен'

    // Проверяем доступность через несколько секунд
    setTimeout(() => {
      if (getTelegramWebApp()) {
        console.log('✅ Telegram WebApp теперь доступен, повторяем авторизацию')
        authenticateUser()
      }
    }, 1000)
    return false
  }

  console.log('✅ Telegram WebApp доступен:', tg)

  if (!tg.initDataUnsafe?.user) {
    console.warn('Данные пользователя не получены:', tg.initDataUnsafe)
    authError.value = 'Данные пользователя не получены'

    // Попробуем инициализировать WebApp если не инициализирован
    if (typeof tg.ready === 'function') {
      tg.ready()
    }

    // Проверяем снова через некоторое время
    setTimeout(() => {
      if (tg.initDataUnsafe?.user) {
        console.log('✅ Данные пользователя теперь доступны')
        authenticateUser()
      }
    }, 500)
    return false
  }

  const user = tg.initDataUnsafe.user
  console.log('👤 Получены данные пользователя:', user)

  // Проверяем минимальные данные
  if (!user.id) {
    console.error('Отсутствует telegramId пользователя')
    authError.value = 'Отсутствует ID пользователя'
    return false
  }

  try {
    console.log('📡 Отправляем запрос на сервер...')

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

    console.log('📡 Статус ответа:', response.status)
    const data = await response.json()
    console.log('📡 Ответ сервера:', data)

    if (response.ok && data.success) {
      if (data.data?.token) {
        localStorage.setItem('token', data.data.token)
        localStorage.setItem('userData', JSON.stringify({
          telegramId: user.id,
          username: user.username,
          firstName: user.first_name,
          lastName: user.last_name
        }))
        console.log('✅ Токен сохранён:', data.data.token)
      }

      isAuthenticated.value = true

      // Показываем уведомление в Telegram
      try {
        if (typeof tg.showAlert === 'function') {
          tg.showAlert('✅ Авторизация успешна!')
        }
      } catch (e) {
        console.log('Уведомление Telegram не сработало:', e)
      }

      // Скрываем статус через 3 секунды
      setTimeout(() => {
        isAuthenticated.value = false
      }, 3000)

      return true
    } else {
      const errorMsg = data.message || `Ошибка сервера (${response.status})`
      console.error('❌ Ошибка от сервера:', errorMsg)
      authError.value = errorMsg

      // Показываем ошибку в Telegram
      try {
        if (typeof tg.showAlert === 'function') {
          tg.showAlert(`❌ ${errorMsg}`)
        }
      } catch (e) {
        console.log('Уведомление об ошибке не сработало:', e)
      }

      return false
    }
  } catch (error: any) {
    console.error('❌ Ошибка при выполнении запроса:', error)
    authError.value = error.message || 'Ошибка подключения к серверу'

    // Показываем ошибку в Telegram
    try {
      const tg = getTelegramWebApp()
      if (tg && typeof tg.showAlert === 'function') {
        tg.showAlert(`❌ Ошибка: ${authError.value}`)
      }
    } catch (e) {
      console.log('Не удалось показать уведомление об ошибке')
    }

    return false
  }
}

// Функция для ручного тестирования (можно вызвать из консоли)
const testAuth = async () => {
  console.log('🛠 Ручной тест авторизации')
  await authenticateUser()
}

// Инициализация при монтировании
onMounted(async () => {
  console.log('🚀 App.vue mounted')
  isInitialized.value = true

  // Ждём следующего тика для полной инициализации
  await nextTick()

  // Даём время на загрузку Telegram WebApp
  setTimeout(async () => {
    const tg = getTelegramWebApp()
    console.log('⏳ Проверяем Telegram WebApp после загрузки:', tg ? 'доступен' : 'недоступен')

    if (tg) {
      console.log('📊 Telegram WebApp данные:', {
        initData: tg.initData,
        initDataUnsafe: tg.initDataUnsafe,
        version: tg.version,
        platform: tg.platform
      })
    }

    // Авторизуемся только если нет токена
    if (!localStorage.getItem('token')) {
      console.log('🔍 Токен не найден, запускаем авторизацию')
      await authenticateUser()
    } else {
      console.log('🔍 Токен уже существует:', localStorage.getItem('token'))
      // Но всё равно можем обновить данные пользователя
      // await authenticateUser()
    }
  }, 500)
})

// Экспортируем для отладки
defineExpose({
  authenticateUser,
  testAuth,
  getTelegramWebApp
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <!-- Индикатор инициализации (только для разработки) -->
    <div
      v-if="process.env.NODE_ENV === 'development' && !isInitialized"
      class="fixed top-4 left-4 z-50 bg-yellow-500 text-white px-3 py-1 rounded text-xs"
    >
      Инициализация...
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
      v-if="authError"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-red-400 shadow-2xl flex items-center gap-3 animate-fade-in"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✕</span>
      </div>
      <span class="font-bold text-white text-sm">Ошибка: {{ authError }}</span>
    </div>

    <!-- Кнопка для ручного тестирования (только для разработки) -->
    <button
      v-if="process.env.NODE_ENV === 'development'"
      @click="testAuth"
      class="fixed bottom-20 right-4 z-50 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-full shadow-lg font-bold text-sm"
    >
      🔧 Тест авторизации
    </button>

    <!-- Основной контент -->
    <router-view />

    <!-- Навигационное меню -->
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>

<style>
.animate-fade-in {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translate(-50%, -10px);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}
</style>
