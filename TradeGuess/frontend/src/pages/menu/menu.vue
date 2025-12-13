<script setup>
import router from '@/app/router/index.js'

function onClick() {
  router.push({ path: '/chart' });
}

function onClickTest() {
  router.push({ path: '/test' });
}
</script>

<template>
  <h1 @click="onClick">График нажми на меня</h1>
  <h1 class="test" @click="onClickTest">График (ТЕСТ) нажми на меня</h1>
</template>

<style scoped>
.test {
  color: red;
}
</style>
