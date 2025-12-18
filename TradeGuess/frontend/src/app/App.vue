<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import { useRoute } from 'vue-router'
import {NavigationMenu} from '@/widgets/NavigationMenu'
import {telegramWebApp} from '@/app/main.ts'
import {authenticate} from '@/features/auth/model/auth.ts'

const route = useRoute()
const showNavigation = ref(false)

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

const tg = telegramWebApp;

onMounted(async () => {
  try {
    await authenticate(); // Отправка на сервер
  } catch (error) {
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
