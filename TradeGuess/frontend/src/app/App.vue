<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import {useRoute} from 'vue-router'
import {NavigationMenu} from '@/widgets/NavigationMenu'

const route = useRoute();
const showNavigation = ref(false)
const isTesting = ref(false)
const testResult = ref('')
const currentUser = ref<any>(null)

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

// ✅ Собираем реальные данные пользователя
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
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative p-4">

    <!-- ✅ КНОПКА с реальными данными пользователя -->
    <div class="fixed top-6 left-1/2 transform -translate-x-1/2 z-50 max-w-md w-full">
      <button
        @click="testAuth"
        :disabled="isTesting"
        class="w-full bg-emerald-600 hover:bg-emerald-700 disabled:bg-emerald-800 text-white font-bold py-3 px-6 rounded-xl shadow-lg transition-all duration-200 flex items-center justify-center gap-2 text-sm"
      >
        <span v-if="!isTesting">🚀 АВТОРИЗОВАТЬСЯ</span>
        <span v-else>⏳ Авторизуем...</span>
        <span v-if="currentUser" class="text-xs bg-white/20 px-2 py-1 rounded-full">
          {{ currentUser.first_name }}
        </span>
      </button>
    </div>

    <!-- ✅ РЕЗУЛЬТАТ -->
    <div v-if="testResult" class="fixed top-24 left-1/2 transform -translate-x-1/2 z-50 bg-zinc-800/95 backdrop-blur-md p-6 rounded-2xl border border-zinc-700 max-w-md w-full max-h-96 overflow-y-auto text-xs leading-relaxed font-mono">
      <div class="flex justify-between items-center mb-4 pb-2 border-b border-zinc-600">
        <h3 class="font-bold text-emerald-400">📊 Результат авторизации</h3>
        <button @click="testResult = ''" class="text-zinc-400 hover:text-zinc-200 text-lg">&times;</button>
      </div>
      <pre class="whitespace-pre-wrap text-zinc-200">{{ testResult }}</pre>
    </div>

    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>

<style>
body {
  background-color: #18181b;
}
</style>
