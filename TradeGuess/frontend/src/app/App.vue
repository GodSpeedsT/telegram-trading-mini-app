<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')
const currentUser = ref<any>(null)

watch(
  () => route.path,
  newPath => {
    showNavigation.value = newPath !== '/'
  },
  { immediate: true }
)

const collectRealUserData = () => {
  const tg = telegramWebApp;

  if (!tg || !tg.initDataUnsafe?.user) {
    console.warn('Telegram WebApp недоступен');
    return null;
  }

  const user = tg.initDataUnsafe.user
  currentUser.value = user;

  return {
    telegramId: user.id,
    username: user.username || '',
    firstName: user.first_name || '',
    isReal: true,
    fullData: user
  };
}

const authenticateUser = async () => {
  authError.value = ''

  // ✅ Собираем РЕАЛЬНЫЕ данные пользователя
  const realUserData = collectRealUserData();

  if (!realUserData) {
    authError.value = 'Данные пользователя не получены'
    console.warn('Telegram WebApp недоступен или данные пользователя отсутствуют')
    return false
  }

  console.log('👤 Данные пользователя:', realUserData.fullData)

  try {
    // ✅ Отправляем данные на сервер
    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        telegramId: realUserData.telegramId,
        username: realUserData.username,
        firstName: realUserData.firstName
      })
    });

    console.log('📡 Статус ответа:', response.status)

    const data = await response.json();
    console.log('📡 Ответ сервера:', data)

    if (response.ok && data.success) {
      if (data.data?.token) {
        localStorage.setItem('token', data.data.token)
        console.log('✅ Токен сохранён в localStorage:', data.data.token)
      }

      isAuthenticated.value = true

      // Показываем уведомление в Telegram
      if (telegramWebApp) {
        telegramWebApp.showAlert('✅ Авторизация успешна!')
      }

      // Скрываем уведомление через 3 секунды
      setTimeout(() => {
        isAuthenticated.value = false
      }, 3000)

      return true
    } else {
      const errorMsg = data.message || 'Ошибка сервера'
      authError.value = errorMsg
      throw new Error(errorMsg)
    }

  } catch (error: any) {
    console.error('❌ Ошибка авторизации:', error)
    authError.value = error.message || 'Неизвестная ошибка'

    // Показываем ошибку в Telegram
    if (telegramWebApp) {
      telegramWebApp.showAlert(`❌ ${authError.value}`)
    }

    return false
  }
}

// ✅ ДЕБАГ: Добавим кнопку для ручного тестирования
const testAuthManually = async () => {
  console.log('🛠 Ручной тест авторизации запущен')
  await authenticateUser()
}

// ✅ Добавляем логирование для отладки
onMounted(async () => {
  console.log('🚀 App.vue mounted')
  console.log('🔍 Telegram WebApp доступен:', !!telegramWebApp)
  console.log('🔍 Токен в localStorage:', localStorage.getItem('token'))

  if (telegramWebApp) {
    console.log('🔍 Telegram initData:', telegramWebApp.initData)
    console.log('🔍 Telegram user data:', telegramWebApp.initDataUnsafe?.user)
  }

  // ✅ Всегда пытаемся авторизоваться при загрузке
  // (можно раскомментировать для автоматической авторизации)
  // await authenticateUser()

  // ✅ Или проверяем токен, но всё равно пытаемся обновить данные
  if (!localStorage.getItem('token')) {
    console.log('🔍 Токен отсутствует, запускаем авторизацию')
    await authenticateUser()
  } else {
    console.log('🔍 Токен найден, но обновляем данные пользователя')
    // Даже если токен есть, отправляем данные пользователя для обновления БД
    await authenticateUser()
  }
})

// ✅ Экспортируем функции для отладки в консоли
defineExpose({
  authenticateUser,
  testAuthManually,
  currentUser,
  telegramWebApp
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <!-- Кнопка для ручного тестирования (только для разработки) -->
    <button
      v-if="process.env.NODE_ENV === 'development'"
      @click="testAuthManually"
      class="fixed top-20 left-4 z-50 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-bold"
    >
      🔧 Тест авторизации
    </button>

    <!-- Уведомление об успешной авторизации -->
    <div
      v-if="isAuthenticated"
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-emerald-400 shadow-2xl flex items-center gap-3 animate-fade-in"
    >
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✓</span>
      </div>
      <span class="font-bold text-white text-sm">Авторизация успешна</span>
    </div>

    <!-- Уведомление об ошибке -->
    <div
      v-if="authError"
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
</style>
