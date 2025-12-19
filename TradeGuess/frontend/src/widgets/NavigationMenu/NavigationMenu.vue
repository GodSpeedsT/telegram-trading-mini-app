<template>
  <div class="fixed bottom-0 left-0 w-full bg-zinc-900/90 backdrop-blur-xl border-t border-white/5 z-50 [padding-bottom:env(safe-area-inset-bottom)] shadow-[0_-10px_40px_rgba(0,0,0,0.5)]">
    <div class="flex justify-around items-center h-[70px] px-2">
      <button v-for="item in navItems" :key="item.id" @click="switchTab(item.id)" class="flex flex-col items-center justify-center gap-1 w-16 h-full group relative [-webkit-tap-highlight-color:transparent] select-none">
        <div v-if="item.id === 2 && activeTab === 2" class="absolute top-2 w-10 h-10 bg-purple-500/20 rounded-full blur-lg"></div>
        <svg class="w-6 h-6 transition-colors relative z-10" :class="iconClass(item)" fill="none" viewBox="0 0 24 24" stroke="currentColor" :stroke-width="activeTab === item.id ? 2 : 1.5">
          <path stroke-linecap="round" stroke-linejoin="round" :d="activeTab === item.id ? item.activeIcon : item.icon"/>
        </svg>
        <span class="text-[10px] transition-colors relative z-10 truncate max-w-[64px]" :class="textClass(item)">
          {{ item.name }}
        </span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

interface NavItem {
  id: number
  name: string
  icon: string
  activeIcon: string
  route: string
  colorClass: string
}

const activeTab = ref(0)
const router = useRouter()
const route = useRoute()

const navItems: NavItem[] = [
  {
    id: 0,
    name: 'Главная',
    icon: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6',
    activeIcon: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6',
    route: '/',
    colorClass: 'white'
  },
  {
    id: 1,
    name: 'Live',
    icon: 'M13 7h8m0 0v8m0-8l-8 8-4-4-6 6',
    activeIcon: 'M13 7h8m0 0v8m0-8l-8 8-4-4-6 6',
    route: '/real-time',
    colorClass: 'green'
  },
  {
    id: 2,
    name: 'Игра',
    icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    activeIcon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    route: '/trade-game',
    colorClass: 'purple'
  },
  {
    id: 3,
    name: 'Награды',
    icon: 'M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7',
    activeIcon: 'M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7',
    route: '/achievements',
    colorClass: 'yellow'
  },
  {
    id: 4,
    name: 'Профиль',
    icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z',
    activeIcon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z',
    route: '/account',
    colorClass: 'white'
  }
]

const iconClass = (item: NavItem) => {
  if (activeTab.value === item.id) {
    return item.colorClass === 'white' ? 'text-purple-400' : `text-${item.colorClass}-400`
  }

  const map: Record<string, string> = {
    white: 'text-zinc-500 group-hover:text-white',
    green: 'text-zinc-500 group-hover:text-green-400',
    purple: 'text-zinc-500 group-hover:text-purple-400',
    yellow: 'text-zinc-500 group-hover:text-yellow-400'
  }
  return map[item.colorClass]
}

const textClass = (item: NavItem) => {
  if (activeTab.value === item.id) {
    return item.colorClass === 'white' ? 'text-purple-400 font-bold' : `text-${item.colorClass}-400 font-bold`
  }

  const map: Record<string, string> = {
    white: 'text-zinc-500 group-hover:text-zinc-300 font-medium',
    green: 'text-zinc-500 group-hover:text-green-400 font-medium',
    purple: 'text-zinc-500 group-hover:text-purple-400 font-medium',
    yellow: 'text-zinc-500 group-hover:text-yellow-400 font-medium'
  }
  return map[item.colorClass]
}

const switchTab = (id: number) => {
  if (id === activeTab.value) return
  const targetRoute = navItems[id]?.route
  if (targetRoute) {
    router.push(targetRoute)
    activeTab.value = id
  }
}

onMounted(() => {
  const currentRoute = route.path
  const currentTab = navItems.findIndex(item => item.route === currentRoute)
  if (currentTab !== -1) {
    activeTab.value = currentTab
  }
})

watch(() => route.path, (newPath) => {
  const currentTab = navItems.findIndex(item => item.route === newPath)
  if (currentTab !== -1 && currentTab !== activeTab.value) {
    activeTab.value = currentTab
  }
})
</script>
