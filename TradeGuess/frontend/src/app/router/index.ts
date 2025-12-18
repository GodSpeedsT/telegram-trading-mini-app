import { createRouter, createWebHistory } from 'vue-router'
import { MainMenu } from '@/pages/menu'
import {TradeGame} from '@/pages/chart'
import RealTimeCandlestickChart from '@/pages/test/RealTimeCandlestickChart.vue'
import Achivments from '@/pages/achivments/achivments.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: MainMenu,
    },
    {
      path: '/TradeGame',
      name: 'chart',
      component: TradeGame,
    },
    {
      path: '/test',
      name: 'test',
      component: RealTimeCandlestickChart,
    },
     {
      path: '/achivments',
      name: 'achivments',
      component: Achivments,
    }
  ],
})

export default router
