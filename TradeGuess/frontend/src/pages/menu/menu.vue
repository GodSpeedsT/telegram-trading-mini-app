<script setup>
import router from '@/app/router/index.js'

function onClick() {
  router.push({ path: '/chart' });
}
</script>

<template>
  <h1 @click="onClick">График нажми на меня</h1>
</template>

<style scoped>

</style>
