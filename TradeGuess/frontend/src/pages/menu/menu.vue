<template>
  <div class="flex flex-col justify-center items-center gap-3 p-2">
    <div class="w-12 h-12 bg-gray-300 rounded-full flex items-center justify-center text-2xl">
      👤
    </div>
    <span class="font-bold text-gray-700">Гость</span>
    <!-- показ регестриции-->

    <h1 @click="onClick" class="cursor-pointer hover:underline">График нажми на меня</h1>
    <h1 class="test cursor-pointer hover:underline" @click="onClickTest">График (ТЕСТ) нажми на меня</h1>
  </div>
</template>

<script setup lang="ts">
import router from '@/app/router/index.js'

function onClick() {
  router.push({ path: '/chart' });
}

function onClickTest() {
  router.push({ path: '/test' });
}
</script>

<style scoped>
.test {
  color: red;
}
</style>
