<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import { useRoute } from 'vue-router'
import { NavigationMenu } from '@/widgets/NavigationMenu'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const authStore = useAuthStore()
const showNavigation = ref(false)

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })

onMounted(async () => {
  try {
    authStore.initWebApp()

    await authStore.authenticateUser()

  } catch (error: any) {
    console.error('[LIFECYCLE] onMounted error:', error)
    authStore.authError = 'Ошибка запуска: ' + (error.message || 'Неизвестно')
  }
})
</script>

<template>
  <div class="min-h-screen bg-zinc-900 text-zinc-300 font-sans antialiased relative">
    <div v-if="authStore.isAuthenticated" class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-emerald-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-emerald-400 shadow-2xl flex items-center gap-3">
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✓</span>
      </div>
      <span class="font-bold text-white text-sm">Авторизация успешна</span>
    </div>

    <div v-if="authStore.authError" class="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-500/90 backdrop-blur-sm px-5 py-3 rounded-2xl border border-red-400 shadow-2xl flex items-center gap-3">
      <div class="w-6 h-6 bg-white/20 rounded-full flex items-center justify-center">
        <span class="text-white font-bold text-lg">✕</span>
      </div>
      <span class="font-bold text-white text-sm">{{ authStore.authError }}</span>
    </div>

    <router-view />
    <NavigationMenu v-if="showNavigation" />
  </div>
</template>
