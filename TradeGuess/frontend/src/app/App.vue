<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import { telegramWebApp } from '@/app/main.ts'
import {useRoute} from 'vue-router'
import {NavigationMenu} from '@/widgets/NavigationMenu'

const route = useRoute();
const showNavigation = ref(false)
watch(() => route.path,
  (newPath) => {
  showNavigation.value = newPath !== '/'
  }, { immediate: true }
)

onMounted(async () => {
  const tg = telegramWebApp;

  // 1️⃣ Проверяем наличие Telegram WebApp
  if (!tg) {
    alert('❌ Telegram WebApp НЕ найден');
    return;
  }

  alert('✅ Telegram WebApp найден');

  // 2️⃣ Проверяем initDataUnsafe
  if (!tg.initDataUnsafe) {
    alert('❌ initDataUnsafe пустой');
    return;
  }

  alert(
    '📤 initDataUnsafe отправляется на сервер:\n\n' +
    tg.initDataUnsafe
  );

  console.log('INIT DATA:', tg.initDataUnsafe);

  // 3️⃣ Отправка на сервер
  try {
    const response = await fetch(
      'https://tradeguess-backend.onrender.com/api/auth',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          initDataUnsafe: tg.initDataUnsafe
        })
      }
    );

    alert(`📡 HTTP статус ответа: ${response.status}`);

    const data = await response.json();

    // 4️⃣ Ответ сервера
    alert(
      '📥 Ответ сервера:\n\n' +
      JSON.stringify(data, null, 2)
    );

    console.log('SERVER RESPONSE:', data);

  } catch (error: any) {
    alert(
      '❌ Ошибка при запросе:\n\n' +
      error.message
    );
    console.error(error);
  }
});
</script>


<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>

<style>
body {
  background-color: #18181b;
}
</style>
