<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)
const isTesting = ref(false)
const testResult = ref('')
const currentUser = ref<any>(null)

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

const collectRealUserData = () => {
  const tg = telegramWebApp;

  if (!tg || !tg.initDataUnsafe?.user) {
    return {
      telegramId: 999999,
      username: 'testuser',
      firstName: 'Test User',
      isReal: false
    };
  }

  // ✅ РЕАЛЬНЫЕ данные из Telegram
  const user = tg.initDataUnsafe.user;
  currentUser.value = user;

  return {
    telegramId: user.id,
    username: user.username || '',
    firstName: user.first_name || '',
    isReal: true,
    fullData: user // Полные данные для показа
  };
}

const testAuth = async () => {
  isTesting.value = true
  testResult.value = ''

  try {
    // ✅ Собираем РЕАЛЬНЫЕ данные пользователя
    const realUserData = collectRealUserData();

    testResult.value = `👤 Пользователь найден:\n${JSON.stringify(realUserData.fullData, null, 2)}`

    // ✅ Отправляем ТОЛЬКО нужные данные на сервер
    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        telegramId: realUserData.telegramId,
        username: realUserData.username,
        firstName: realUserData.firstName
      })
    });

    const data = await response.json();

    testResult.value += `\n\n📡 Ответ сервера (${response.status}):\n${JSON.stringify(data, null, 2)}`

    if (data.success && data.data?.token) {
      localStorage.setItem('token', data.data.token)
      testResult.value += '\n\n✅ Токен сохранён в localStorage!'
    }

  } catch (error: any) {
    testResult.value = `❌ Ошибка: ${error.message}`
    console.error(error)
  } finally {
    isTesting.value = false
  }
}


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
