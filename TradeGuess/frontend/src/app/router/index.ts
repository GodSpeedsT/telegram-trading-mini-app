import { createRouter, createWebHistory } from 'vue-router'
import { MainMenu } from '@/pages/menu'
import {chart} from '@/pages/chart'
import RealTimeCandlestickChart from '@/pages/test/RealTimeCandlestickChart.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: MainMenu,
    },
    {
      path: '/chart',
      name: 'chart',
      component: chart,
    },
    {
      path: '/test',
      name: 'test',
      component: RealTimeCandlestickChart,
    }
  ],
})

export default router
