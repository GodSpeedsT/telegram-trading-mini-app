<template>
  <div class="fixed bottom-0 left-0 w-full bg-zinc-900/90 backdrop-blur-xl border-t border-white/5 z-50 [padding-bottom:env(safe-area-inset-bottom)] shadow-[0_-10px_40px_rgba(0,0,0,0.5)] flex justify-center">
    <div class="flex justify-around items-center px-2 w-full max-w-screen-md h-[70px] sm:h-[76px] md:h-[82px] lg:h-[88px] xl:h-[94px]">
      <button v-for="item in navItems" :key="item.id" @click="switchTab(item.id)"
              class="flex flex-col items-center justify-center gap-1 h-full group relative [-webkit-tap-highlight-color:transparent] select-none w-16 sm:w-20 md:w-24 lg:w-28">
        <div v-if="item.id === 2 && activeTab === 2"
             class="absolute top-2 bg-purple-500/20 rounded-full blur-lg w-10 h-10 sm:w-12 sm:h-12 md:w-14 md:h-14 lg:w-16 lg:h-16"></div>
        <svg class="transition-colors relative z-10 w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 lg:w-9 lg:h-9"
             :class="iconClass(item)" fill="none" viewBox="0 0 24 24" stroke="currentColor" :stroke-width="activeTab === item.id ? 2 : 1.5">
          <path stroke-linecap="round" stroke-linejoin="round" :d="activeTab === item.id ? item.activeIcon : item.icon"/>
        </svg>
        <span class="relative z-10 truncate max-w-[64px] sm:max-w-[80px] md:max-w-none transition-colors text-[10px] sm:text-[11px] md:text-xs lg:text-sm"
              :class="textClass(item)">
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
    name: 'Достижения',
    icon: 'M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8',
    activeIcon: 'M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8',
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
