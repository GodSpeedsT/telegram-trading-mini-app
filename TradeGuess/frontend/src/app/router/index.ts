import { createRouter, createWebHistory } from 'vue-router'
import { MainMenu } from '@/pages/menu'
import { TradeGame } from '@/pages/trade-game'
import { RealTimeChart } from '@/pages/real-time'
import { Achievements } from '@/pages/achievements'
import { Account } from '@/pages/account'
import { Leaderboard } from '@/pages/leaderboard'
import { AdminPanel } from '@/pages/admin'
import Stats from '@/pages/account/Stats.vue'

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
    },
    {
      path: '/account',
      name: 'account',
      component: Account,
    },
    {
      path: '/leaderboard',
      name: 'leaderboard',
      component: Leaderboard,
    },
    {
      path: '/stats',
      name: 'stats',
      component: Stats,
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminPanel,
    },
  ],
})

export default router
