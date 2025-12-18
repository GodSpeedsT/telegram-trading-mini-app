<script setup lang="ts">
import {ref, computed, onMounted, onUnmounted, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'

interface NavItem {
  id: number
  name: string
  icon: string
  activeIcon: string
  route: string
  colorClass: string
}

const activeTab = ref(0)
const isTransitioning = ref(false)
let pulseInterval: ReturnType<typeof setInterval> | null = null
const pulseAnimation = ref(0)
const router = useRouter()
const route = useRoute()

const navItems: NavItem[] = [
  {
    id: 0,
    name: 'Рынок',
    icon: 'icon-market',
    activeIcon: 'icon-market-active',
    route: '/real-time',
    colorClass: 'green'
  },
  {
    id: 1,
    name: 'Тренажер',
    icon: 'icon-trainer',
    activeIcon: 'icon-trainer-active',
    route: '/trade-game',
    colorClass: 'purple'
  },
  {
    id: 2,
    name: 'Достижения',
    icon: 'icon-achievements',
    activeIcon: 'icon-achievements-active',
    route: '/achievements',
    colorClass: 'yellow'
  },
  {
    id: 3,
    name: 'Аккаунт',
    icon: 'icon-account',
    activeIcon: 'icon-account-active',
    route: '/account',
    colorClass: 'blue'
  }
  
]

const itemWidth = computed(() => 100 / navItems.length)
const transitionStyle = computed(() => ({
  left: `${itemWidth.value * activeTab.value}%`,
  width: `${itemWidth.value}%`
}))
const glowPosition = computed(() => `calc(${itemWidth.value * activeTab.value}% + ${itemWidth.value / 2}% - 2rem)`)

const activeItem = computed(() => navItems[activeTab.value])
const activeColorClass = computed(() => {
  const map: Record<string, string> = {
    green: 'from-emerald-500 via-green-500 to-emerald-400',
    purple: 'from-violet-500 via-purple-500 to-fuchsia-400',
    yellow: 'from-amber-500 via-yellow-500 to-orange-400',
    blue: 'from-sky-500 via-blue-500 to-cyan-400'
  }
  return map[activeItem.value.colorClass]
})
const colorBgClass = computed(() => {
  const map: Record<string, string> = {
    green: 'text-green-400 bg-green-500/10',
    purple: 'text-purple-400 bg-purple-500/10',
    yellow: 'text-yellow-400 bg-yellow-500/10',
    blue: 'text-blue-400 bg-blue-500/10'
  }
  return map[activeItem.value.colorClass]
})

const switchTab = async (id: number) => {
  if (id === activeTab.value || isTransitioning.value) return
  isTransitioning.value = true
  const targetRoute = navItems[id].route
  await router.push(targetRoute)
  activeTab.value = id
  setTimeout(() => {
    isTransitioning.value = false
  }, 500)
}

onMounted(() => {
  const currentRoute = route.path
  const currentTab = navItems.findIndex(item => item.route === currentRoute)
  if (currentTab !== -1) {
    activeTab.value = currentTab
  }

  pulseInterval = setInterval(() => {
    pulseAnimation.value = (pulseAnimation.value + 1) % 3
  }, 1500)
})
onUnmounted(() => {
  if (pulseInterval) clearInterval(pulseInterval)
})

watch(() => route.path, (newPath) => {
  const currentTab = navItems.findIndex(item => item.route === newPath)
  if (currentTab !== -1 && currentTab !== activeTab.value) {
    activeTab.value = currentTab
  }
})

const getPulseStyle = (index: number) => {
  if (activeTab.value !== index) return {}
  const scales = [1, 1.3, 1]
  const opacities = [0.5, 0, 0.5]
  return {
    transform: `scale(${scales[pulseAnimation.value]})`,
    opacity: opacities[pulseAnimation.value]
  }
}
</script>

<template>
  <nav class="top-0 bottom-0 left-0 right-0 z-50">
    <div
      class=" inset-0 bg-zinc-900/95 backdrop-blur-xl border-2 border-zinc-800/50 shadow-[0_-20px_80px_-12px_rgba(0,0,0,0.9)]"/>
    <div class="absolute bottom-0 h-[3px] rounded-t-full transition-all duration-500"
         :style="transitionStyle" :class="`bg-gradient-to-r ${activeColorClass}`"/>
    <div
      class="absolute bottom-3 h-10 w-16 rounded-full blur-xl opacity-30 transition-all duration-700"
      :style="{ left: glowPosition }" :class="`bg-gradient-to-r ${activeColorClass}`"/>

    <div class="relative flex justify-around items-center py-3">
      <div
        class="absolute top-2 h-16 rounded-2xl transition-all duration-500 ease-[cubic-bezier(0.68,-0.55,0.265,1.55)]"
        :style="transitionStyle" :class="`bg-gradient-to-r ${activeColorClass} opacity-20`"/>

      <button v-for="item in navItems" :key="item.id"
              class="relative flex flex-col items-center justify-center w-full transition-all duration-300 z-10 group"
              @click="switchTab(item.id)" :disabled="isTransitioning">
        <div class="relative">
          <div v-if="activeTab === item.id"
               class="absolute -inset-2 rounded-full opacity-20 transition-all duration-500"
               :class="`bg-gradient-to-r ${activeColorClass}`"/>
          <div
            class="relative flex items-center justify-center rounded-full w-12 h-12 transition-all duration-300 overflow-hidden group-hover:scale-105"
            :class="[activeTab === item.id ? `${colorBgClass} border border-zinc-600/30 shadow-2xl` : 'bg-zinc-800/50 group-hover:bg-zinc-700/50 border border-zinc-700/30 group-hover:border-zinc-500/30']">
            <div v-if="activeTab === item.id"
                 class="absolute inset-0 rounded-full opacity-30 animate-spin-slow"
                 :class="`bg-gradient-to-r ${activeColorClass}`" style="animation-duration: 3s;"/>
            <div class="relative transition-all duration-300"
                 :style="{ transform: `scale(${activeTab === item.id ? 1.2 : 1}) translateY(${activeTab === item.id ? '-2px' : '0'})` }">
              <!-- Вставьте здесь <SvgIcon :name="activeTab === item.id ? item.activeIcon : item.icon" /> -->
              <div class="w-6 h-6 flex items-center justify-center">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  :stroke-width="activeTab === item.id ? '2' : '1.5'"
                  stroke="currentColor"
                  class="w-6 h-6 transition-all duration-300"
                >
                  <!-- Рынок (id: 0) -->
                  <path v-if="item.id === 0" stroke-linecap="round" stroke-linejoin="round"
                        d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />

                  <!-- Тренажер (id: 1) -->
                  <path v-else-if="item.id === 1" stroke-linecap="round" stroke-linejoin="round"
                        d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />

                  <!-- Достижения (id: 2) -->
                  <path v-else-if="item.id === 2" stroke-linecap="round" stroke-linejoin="round"
                        d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />

                  <!-- Аккаунт (id: 3) -->
                  <path v-else stroke-linecap="round" stroke-linejoin="round"
                        d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
                </svg>
              </div>
            </div>
            <template v-if="activeTab === item.id">
              <div class="absolute inset-0 rounded-full border" :class="`border-green-500/30`"
                   :style="getPulseStyle(item.id)"/>
              <div class="absolute inset-0 rounded-full border" :class="`border-green-500/20`"
                   :style="getPulseStyle(item.id)" style="animation-delay: 0.2s;"/>
            </template>
          </div>
        </div>
        <span class="mt-1 text-xs font-medium transition-all duration-300 truncate relative"
              :class="[activeTab === item.id ? `font-bold text-transparent bg-clip-text bg-gradient-to-r ${activeColorClass}` : 'text-zinc-400 group-hover:text-zinc-300']"
              :style="{ transform: `translateY(${activeTab === item.id ? '-2px' : '0'})` }">
          {{ item.name }}
        </span>
        <div v-if="activeTab === item.id"
             class="absolute -bottom-1 w-6 h-0.5 rounded-full animate-pulse"
             :class="`bg-gradient-to-r ${activeColorClass}`" style="animation-duration: 2s;"/>
      </button>
    </div>
    <div class="h-4 bg-transparent sm:h-0"/>
  </nav>
</template>

<style scoped>
@keyframes spin-slow {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.animate-spin-slow {
  animation: spin-slow linear infinite;
}

nav * {
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
}

button:active .relative > div:first-child {
  transform: scale(0.95);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.text-xs {
  font-size: 11px;
  letter-spacing: 0.01em;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

@media (max-width: 640px) {
  .text-xs {
    font-size: 10px;
  }
}

@media (min-width: 768px) {
  .text-xs {
    font-size: 12px;
  }
}

button {
  -webkit-tap-highlight-color: transparent;
  user-select: none;
}

@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}

.shadow-2xl {
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5), 0 0 20px rgba(16, 185, 129, 0.3);
}
</style>
