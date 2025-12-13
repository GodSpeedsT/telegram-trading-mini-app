import { createRouter, createWebHistory } from 'vue-router'
import { menu } from '@/pages/menu'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: menu,
    },
  ],
})

export default router
