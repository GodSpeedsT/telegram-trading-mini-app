import { createRouter, createWebHistory } from 'vue-router'
import { MainMenu } from '@/pages/menu'
import {TradeGame} from '@/pages/chart'
import {RealTimeChart} from '@/pages/real-time'
import {Achievements} from '@/pages/achievements'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: MainMenu,
    },
    {
      path: '/trade-game',
      name: 'game-chart',
      component: TradeGame,
    },
    {
      path: '/real-time',
      name: 'real-time',
      component: RealTimeChart,
    },
     {
      path: '/achievements',
      name: 'achievements',
      component: Achievements,
    }
  ],
})

export default router
