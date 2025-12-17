<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'

interface NavItem {
  id: number
  name: string
  icon: string
  activeIcon: string
  route: string
  gradient: string
  colorClass: string
}

const activeTab = ref(0)
const isTransitioning = ref(false)

const navItems: NavItem[] = [
  {
    id: 0,
    name: 'Рынок',
    icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z"/></svg>`,
    activeIcon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z"/></svg>`,
    route: '/market',
    gradient: 'from-emerald-500 via-green-500 to-emerald-400',
    colorClass: 'green'
  },
  {
    id: 1,
    name: 'Тренажер',
    icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
    activeIcon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
    route: '/trainer',
    gradient: 'from-violet-500 via-purple-500 to-fuchsia-400',
    colorClass: 'purple'
  },
  {
    id: 2,
    name: 'Достижения',
    icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125"/></svg>`,
    activeIcon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125"/></svg>`,
    route: '/achievements',
    gradient: 'from-amber-500 via-yellow-500 to-orange-400',
    colorClass: 'yellow'
  },
  {
    id: 3,
    name: 'Аккаунт',
    icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>`,
    activeIcon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>`,
    route: '/account',
    gradient: 'from-sky-500 via-blue-500 to-cyan-400',
    colorClass: 'blue'
  }
]

const itemWidth = 100 / navItems.length
const activePosition = computed(() => `${itemWidth * activeTab.value}%`)
const activeItem = computed(() => navItems[activeTab.value])

const switchTab = async (id: number) => {
  if (id === activeTab.value || isTransitioning.value) return

  isTransitioning.value = true
  activeTab.value = id

  // Ждем завершения рендера и запускаем CSS анимации
  await nextTick()
  isTransitioning.value = false
}
</script>

<template>
  <nav
    class="fixed bottom-0 left-0 right-0 z-50 bg-zinc-900/95 backdrop-blur-xl border-t border-zinc-800/50 shadow-[0_-20px_80px_-12px_rgba(0,0,0,0.9)]"
    style="transform: translateZ(0); will-change: transform;"
  >
    <!-- Анимированная подсветка снизу -->
    <div
      class="absolute bottom-0 h-[3px] rounded-t-full bg-gradient-to-r transition-all duration-500 ease-out"
      :style="{ left: activePosition, width: `${itemWidth}%` }"
      :class="activeItem.gradient"
    />

    <!-- Анимированная сфера -->
    <div
      class="absolute -top-8 h-16 w-16 rounded-full blur-xl opacity-30 transition-all duration-700 ease-out"
      :style="{ left: `calc(${activePosition} + 12.5% - 2rem)` }"
      :class="`bg-gradient-to-r ${activeItem.gradient}`"
    />

    <div class="relative flex h-20 items-center justify-around px-4 py-3">
      <!-- Жидкий фон активной вкладки -->
      <div
        class="absolute inset-y-2 h-14 rounded-2xl bg-gradient-to-r opacity-20 blur-sm transition-all duration-500 ease-out"
        :style="{ left: activePosition, width: `${itemWidth}%` }"
        :class="activeItem.gradient"
      />

      <button
        v-for="(item, index) in navItems"
        :key="item.id"
        class="group relative flex h-full flex-col items-center justify-center flex-1 gap-1 p-2 transition-all duration-300"
        :class="[
          isTransitioning && 'pointer-events-none',
          activeTab === index && 'nav-active'
        ]"
        @click="switchTab(item.id)"
      >
        <!-- Контейнер иконки -->
        <div class="icon-container relative flex h-12 w-12 items-center justify-center rounded-full border overflow-hidden transition-all duration-500 ease-out">
          <!-- Жидкий фон -->
          <div
            v-if="activeTab === index"
            class="absolute inset-0 rounded-full opacity-30"
            :class="`bg-gradient-to-r ${item.gradient} animate-spin-slow`"
            style="animation-duration: 4s; animation-play-state: running;"
          />

          <!-- Иконка -->
          <div
            class="icon relative z-10 transition-all duration-500 ease-out"
            :class="activeTab === index ? 'icon-active' : 'icon-inactive'"
            v-html="activeTab === index ? item.activeIcon : item.icon"
          />

          <!-- Пульсирующие кольца -->
          <div
            v-if="activeTab === index"
            class="absolute inset-0 rounded-full border-2 animate-ping"
            :class="`${item.gradient.split(' ')[1]}-400/30`"
            style="animation-duration: 2s;"
          />
        </div>

        <!-- Текст -->
        <span
          class="text-xs font-medium transition-all duration-500 ease-out truncate"
          :class="activeTab === index ? 'text-active' : 'text-inactive'"
        >
          {{ item.name }}
        </span>

        <!-- Подчеркивание -->
        <div
          v-if="activeTab === index"
          class="absolute -bottom-1 w-6 h-0.5 rounded-full animate-pulse bg-gradient-to-r"
          :class="item.gradient"
          style="animation-duration: 2s;"
        />
      </button>
    </div>

    <!-- Safe area для Telegram/iPhone -->
    <div class="h-[env(safe-area-inset-bottom)] sm:h-0 bg-transparent" />
  </nav>
</template>

<style scoped>
/* Touch оптимизации для Telegram Mini App */
button {
  -webkit-tap-highlight-color: transparent !important;
  -webkit-touch-callout: none;
  user-select: none;
  touch-action: manipulation;
}

/* Базовые оптимизации рендеринга */
.icon-container,
.icon,
.icon-container * {
  will-change: transform;
  backface-visibility: hidden;
  transform: translateZ(0);
}

/* Состояния иконки */
.icon-inactive {
  @apply text-zinc-400 scale-100 translate-y-0;
}

.icon-active {
  @apply scale-110 -translate-y-0.5 text-transparent bg-clip-text shadow-2xl;
  animation: iconSpring 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* Состояния текста */
.text-inactive {
  @apply text-zinc-400 group-hover:text-zinc-300;
}

.text-active {
  @apply font-bold text-transparent bg-clip-text -translate-y-0.5 shadow-sm;
}

/* Пружинная анимация иконки */
@keyframes iconSpring {
  0%, 60% { transform: scale(1) translateY(0); }
  30% { transform: scale(1.15) translateY(-1px); }
  100% { transform: scale(1.1) translateY(-0.5px); }
}

/* Медленное вращение */
@keyframes spin-slow {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Активная кнопка */
.nav-active .icon-container {
  @apply bg-gradient-to-r border-2 border-white/20 shadow-2xl;
}

/* Цветные тени */
.nav-active.green .icon-container {
  box-shadow:
    0 20px 40px -12px rgba(0, 0, 0, 0.5),
    0 0 25px rgba(16, 185, 129, 0.4);
}

.nav-active.purple .icon-container {
  box-shadow:
    0 20px 40px -12px rgba(0, 0, 0, 0.5),
    0 0 25px rgba(139, 92, 246, 0.4);
}

.nav-active.yellow .icon-container {
  box-shadow:
    0 20px 40px -12px rgba(0, 0, 0, 0.5),
    0 0 25px rgba(245, 158, 11, 0.4);
}

.nav-active.blue .icon-container {
  box-shadow:
    0 20px 40px -12px rgba(0, 0, 0, 0.5),
    0 0 25px rgba(14, 165, 233, 0.4);
}

/* Hover для неактивных */
.group:not(.nav-active) .icon-container {
  @apply bg-zinc-800/50 border-zinc-700/50 hover:bg-zinc-700/70 hover:border-zinc-500/50 hover:scale-105;
}

/* Reduced motion */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}

/* Telegram WebApp оптимизации */
@supports (padding: max(0px)) {
  nav {
    padding-bottom: max(env(safe-area-inset-bottom), 12px);
  }
}
</style>
