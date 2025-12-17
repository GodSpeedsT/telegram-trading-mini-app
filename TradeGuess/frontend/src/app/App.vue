<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {NavigationMenu} from '@/widgets/NavigationMenu'

const route = useRoute()
const showNavigation = ref(false)

watch(() => route.path, (newPath) => {
  showNavigation.value = newPath !== '/'
}, { immediate: true })
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
