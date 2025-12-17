<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

interface NavItem {
  id: number
  name: string
  icon: string
  activeIcon: string
  route: string
  color: string
  gradient: string
  colorClass: string
}

const activeTab = ref(0)

// Конфигурация с градиентами для плавных переходов
const navItems: NavItem[] = [
  {
    id: 0,
    name: 'Рынок',
    icon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
      </svg>
    `,
    activeIcon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" />
      </svg>
    `,
    route: '/market',
    color: 'text-green-400 bg-green-500/10',
    gradient: 'from-emerald-500 via-green-500 to-emerald-400',
    colorClass: 'green'
  },
  {
    id: 1,
    name: 'Тренажер',
    icon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    `,
    activeIcon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    `,
    route: '/trainer',
    color: 'text-purple-400 bg-purple-500/10',
    gradient: 'from-violet-500 via-purple-500 to-fuchsia-400',
    colorClass: 'purple'
  },
  {
    id: 2,
    name: 'Достижения',
    icon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />
      </svg>
    `,
    activeIcon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0V5.25a2.25 2.25 0 00-2.25-2.25h-2.25a2.25 2.25 0 00-2.25 2.25v6.125m10.5 0a2.25 2.25 0 00-2.25-2.25h-2.25v6.125m-10.5 0a2.25 2.25 0 002.25-2.25h2.25v6.125" />
      </svg>
    `,
    route: '/achievements',
    color: 'text-yellow-400 bg-yellow-500/10',
    gradient: 'from-amber-500 via-yellow-500 to-orange-400',
    colorClass: 'yellow'
  },
  {
    id: 3,
    name: 'Аккаунт',
    icon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
      </svg>
    `,
    activeIcon: `
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
      </svg>
    `,
    route: '/account',
    color: 'text-blue-400 bg-blue-500/10',
    gradient: 'from-sky-500 via-blue-500 to-cyan-400',
    colorClass: 'blue'
  }
]

// Реактивная позиция активного индикатора
const activePosition = computed(() => {
  const itemWidth = 100 / navItems.length
  return `${itemWidth * activeTab.value}%`
})

// Анимация для плавного перехода
const isTransitioning = ref(false)
const transitionStyle = ref({
  left: '0%',
  width: '25%',
  transition: 'all 0.5s cubic-bezier(0.4, 0, 0.2, 1)'
})

// Анимационные состояния для каждой кнопки
const buttonStates = ref(
  navItems.map(() => ({
    scale: 1,
    rotate: 0,
    glow: 0
  }))
)

const switchTab = async (id: number) => {
  if (id === activeTab.value || isTransitioning.value) return

  isTransitioning.value = true

  // Анимация выхода
  buttonStates.value[activeTab.value] = {
    scale: 1,
    rotate: 0,
    glow: 0
  }

  // Обновляем активную вкладку
  const oldTab = activeTab.value
  activeTab.value = id

  // Анимация входа
  buttonStates.value[id] = {
    scale: 1.2,
    rotate: 5,
    glow: 1
  }

  // Плавное перемещение индикатора
  const itemWidth = 100 / navItems.length
  transitionStyle.value = {
    left: `${itemWidth * id}%`,
    width: `${itemWidth}%`,
    transition: 'all 0.5s cubic-bezier(0.4, 0, 0.2, 1)'
  }

  // Имитация "пружинной" анимации
  setTimeout(() => {
    buttonStates.value[id] = {
      scale: 1.1,
      rotate: 0,
      glow: 1
    }

    setTimeout(() => {
      buttonStates.value[id] = {
        scale: 1,
        rotate: 0,
        glow: 1
      }
      isTransitioning.value = false
    }, 100)
  }, 200)

  // Здесь можно добавить навигацию по роутам
  console.log(`Переключено на: ${navItems[id].name}`)
}

// Получаем текущий градиент
const getActiveGradient = computed(() => {
  return navItems[activeTab.value].gradient
})

// Получаем цвет класса для активной вкладки
const getActiveColorClass = computed(() => {
  return navItems[activeTab.value].colorClass
})

// Анимация пульсации для активного индикатора
const pulseAnimation = ref(0)
onMounted(() => {
  // Запускаем пульсацию для активной точки
  setInterval(() => {
    pulseAnimation.value = (pulseAnimation.value + 1) % 3
  }, 1500)
})

// Функция для получения стиля пульсации
const getPulseStyle = (index: number) => {
  if (activeTab.value !== index) return {}

  const scale = [1, 1.3, 1][pulseAnimation.value]
  const opacity = [0.5, 0, 0.5][pulseAnimation.value]

  return {
    transform: `scale(${scale})`,
    opacity: opacity
  }
}
</script>

<template>
  <nav class="fixed bottom-0 left-0 right-0 z-50">
    <!-- Фоновый размытый слой -->
    <div class="absolute inset-0 bg-zinc-900/95 backdrop-blur-xl border-t border-zinc-800/50 shadow-[0_-20px_80px_-12px_rgba(0,0,0,0.9)]" />

    <!-- Анимированная подсветка снизу -->
    <div
      class="absolute bottom-0 h-[3px] rounded-t-full transition-all duration-500"
      :style="transitionStyle"
      :class="`bg-gradient-to-r ${getActiveGradient}`"
    />

    <!-- Анимированная сфера для плавного перехода -->
    <div
      class="absolute -top-8 h-16 w-16 rounded-full blur-xl opacity-30 transition-all duration-700"
      :style="{ left: `calc(${activePosition} + 12.5% - 2rem)` }"
      :class="`bg-gradient-to-r ${getActiveGradient}`"
    />

    <div class="relative flex justify-around items-center py-3">
      <!-- Активный жидкий фон -->
      <div
        class="absolute top-2 h-14 rounded-2xl transition-all duration-500 ease-[cubic-bezier(0.68,-0.55,0.265,1.55)]"
        :style="transitionStyle"
        :class="`bg-gradient-to-r ${getActiveGradient} opacity-20`"
      />

      <button
        v-for="(item, index) in navItems"
        :key="item.id"
        class="relative flex flex-col items-center justify-center w-full transition-all duration-300 z-10 group"
        @click="switchTab(item.id)"
        :disabled="isTransitioning"
      >
        <!-- Контейнер иконки с жидкими эффектами -->
        <div class="relative">
          <!-- Внешнее свечение при активности -->
          <div
            v-if="activeTab === item.id"
            class="absolute -inset-2 rounded-full opacity-20 transition-all duration-500"
            :class="`bg-gradient-to-r ${item.gradient}`"
            :style="{
              transform: `scale(${buttonStates[index].glow})`,
              opacity: 0.2 * buttonStates[index].glow
            }"
          />

          <!-- Основной круг иконки -->
          <div
            class="relative flex items-center justify-center rounded-full w-12 h-12 transition-all duration-300 overflow-hidden group-hover:scale-105"
            :class="[
              activeTab === item.id
                ? `${item.color} border border-zinc-600/30 shadow-2xl`
                : 'bg-zinc-800/50 group-hover:bg-zinc-700/50 border border-zinc-700/30 group-hover:border-zinc-500/30'
            ]"
            :style="{
              transform: `scale(${buttonStates[index].scale}) rotate(${buttonStates[index].rotate}deg)`,
              transition: activeTab === item.id ? 'all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1)' : 'all 0.3s ease'
            }"
          >
            <!-- Жидкий фон внутри активной иконки -->
            <div
              v-if="activeTab === item.id"
              class="absolute inset-0 rounded-full opacity-30 animate-spin-slow"
              :class="`bg-gradient-to-r ${item.gradient}`"
              style="animation-duration: 3s;"
            />

            <!-- Иконка с плавным изменением -->
            <div
              class="relative transition-all duration-300"
              :style="{
                transform: `scale(${activeTab === item.id ? 1.2 : 1}) translateY(${activeTab === item.id ? '-2px' : '0'})`
              }"
              v-html="activeTab === item.id ? item.activeIcon : item.icon"
            />

            <!-- Анимированные круги вокруг активной иконки -->
            <template v-if="activeTab === item.id">
              <div
                class="absolute inset-0 rounded-full border"
                :class="`border-${item.gradient.split(' ')[1]}/30`"
                :style="getPulseStyle(index)"
              />
              <div
                class="absolute inset-0 rounded-full border"
                :class="`border-${item.gradient.split(' ')[1]}/20`"
                :style="getPulseStyle(index)"
                style="animation-delay: 0.2s;"
              />
            </template>
          </div>
        </div>

        <!-- Текст с плавной анимацией -->
        <span
          class="mt-1 text-xs font-medium transition-all duration-300 truncate relative"
          :class="[
            activeTab === item.id
              ? `font-bold text-transparent bg-clip-text bg-gradient-to-r ${item.gradient}`
              : 'text-zinc-400 group-hover:text-zinc-300'
          ]"
          :style="{
            transform: `translateY(${activeTab === item.id ? '-2px' : '0'})`
          }"
        >
          {{ item.name }}
        </span>

        <!-- Эффект пульсации под текстом активной вкладки -->
        <div
          v-if="activeTab === item.id"
          class="absolute -bottom-1 w-6 h-0.5 rounded-full animate-pulse"
          :class="`bg-gradient-to-r ${item.gradient}`"
          style="animation-duration: 2s;"
        />
      </button>
    </div>

    <!-- Безопасная зона для iPhone -->
    <div class="h-4 bg-transparent sm:h-0" />
  </nav>
</template>

<style scoped>
/* Кастомные CSS-переменные для цветов */
:root {
  --color-emerald-500-rgb: 16, 185, 129;
  --color-violet-500-rgb: 139, 92, 246;
  --color-amber-500-rgb: 245, 158, 11;
  --color-sky-500-rgb: 14, 165, 233;
}

/* Кастомные анимации */
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

/* Плавные переходы */
nav * {
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
}

/* Эффект нажатия с волной */
button:active .relative > div:first-child {
  transform: scale(0.95);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Улучшение читаемости текста */
.text-xs {
  font-size: 11px;
  letter-spacing: 0.01em;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* Адаптивность */
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

/* Запрет выделения для лучшей производительности */
button {
  -webkit-tap-highlight-color: transparent;
  user-select: none;
}

/* Оптимизация анимаций */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}

/* Эффект жидкости при наведении */
.group:hover .relative > div:first-child {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Градиентные тени */
.shadow-2xl {
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(var(--color-emerald-500-rgb), 0.3);
}

/* Специфичные тени для цветов */
.green .shadow-2xl {
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(16, 185, 129, 0.3);
}

.purple .shadow-2xl {
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(139, 92, 246, 0.3);
}

.yellow .shadow-2xl {
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(245, 158, 11, 0.3);
}

.blue .shadow-2xl {
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(14, 165, 233, 0.3);
}
</style>
