import { createRouter, createWebHistory } from 'vue-router'
import { menu } from '@/pages/menu'
import {chart} from '@/pages/chart'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: menu,
    },
    {
      path: '/chart',
      name: 'chart',
      component: chart,
    },
  ],
})

export default router
