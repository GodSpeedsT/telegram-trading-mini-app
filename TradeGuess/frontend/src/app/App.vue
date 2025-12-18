<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import {useRoute} from 'vue-router'
import {NavigationMenu} from '@/widgets/NavigationMenu'

const route = useRoute();
const showNavigation = ref(false)
const isAuthenticated = ref(false)
const authError = ref('')

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

// ✅ Чистая логика авторизации (без тестов)
const authenticateUser = async () => {
  const tg = telegramWebApp;

  if (!tg || !tg.initDataUnsafe?.user) {
    console.warn('Telegram WebApp недоступен');
    return false;
  }

  const user = tg.initDataUnsafe.user;

  try {
    const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        telegramId: user.id,
        username: user.username || '',
        firstName: user.first_name || ''
      })
    });

    const data = await response.json();

    if (response.ok && data.success && data.data?.token) {
      localStorage.setItem('token', data.data.token);
      isAuthenticated.value = true;
      tg.showAlert('✅ Авторизация успешна!');

      // ✅ Убираем галочку через 1 секунду
      setTimeout(() => {
        isAuthenticated.value = false;
      }, 1000);

      return true;
    } else {
      throw new Error(data.message || 'Ошибка сервера');
    }
  } catch (error: any) {
    console.error('Авторизация:', error);
    authError.value = error.message;
    tg?.showAlert(`❌ ${error.message}`);
    return false;
  }
}

// ✅ Автоматическая авторизация при загрузке
onMounted(async () => {
  // Проверяем, есть ли уже токен
  if (localStorage.getItem('token')) {
    return;
  }

  await authenticateUser();
});
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">

    <!-- ✅ Всплывающая галочка успеха (200 OK) — исчезает через 1 сек -->
    <div v-if="isAuthenticated"
         class="fixed top-6 left-1/2 transform -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm p-4 rounded-2xl border border-emerald-400 shadow-2xl animate-bounce">
      <div class="flex items-center gap-3">
        <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center animate-pulse">
          <span class="text-white font-bold text-lg">✓</span>
        </div>
        <span class="font-bold text-white text-sm">Авторизация успешна</span>
      </div>
    </div>

    <!-- ✅ Ошибка авторизации -->
    <div v-if="authError"
         class="fixed top-6 left-1/2 transform -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm p-4 rounded-2xl border border-red-400 shadow-2xl animate-pulse">
      <div class="flex items-center gap-3">
        <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
          <span class="text-white font-bold text-lg">✕</span>
        </div>
        <span class="font-bold text-white text-sm">Ошибка: {{ authError }}</span>
      </div>
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
