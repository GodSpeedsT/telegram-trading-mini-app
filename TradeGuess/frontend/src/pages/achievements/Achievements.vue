<template>
  <!-- Главный контейнер -->
  <div class="min-h-screen flex items-center justify-center p-4 bg-black-700">
    <!-- Блок достижений -->
    <div 
      class="achievement-block relative w-full max-w-4xl rounded-3xl overflow-hidden shadow-2xl transition-all duration-500 hover:shadow-3xl"
      :style="{
        backgroundColor: block.backgroundColor,
        backgroundImage: block.backgroundImage ? `url('${block.backgroundImage}')` : 'none',
        color: block.textColor || '#ffffff'
      }"
    >
      <!-- Градиентный фон для финансовой темы -->
      <div class="absolute inset-0 bg-gradient-to-br from-green-900/30 via-zinc-900/80 to-black-900/30"></div>
      
      <!-- Контент блока -->
      <div class="relative z-10 p-8">
        <!-- Заголовок блока -->
        <div class="text-center mb-10">
          <h1 class="text-4xl font-bold mb-3">
            {{ block.title }}
          </h1>
          <p class="text-lg opacity-90">
            {{ block.subtitle }}
          </p>
        </div>
        
        <!-- Список достижений -->
        <div class="space-y-5">
          <div 
            v-for="achievement in achievements" 
            :key="achievement.id"
            class="achievement-item group relative p-5 rounded-2xl transition-all duration-400"
            :class="[
              achievement.unlocked 
                ? 'bg-gradient-to-r from-green-500/20 to-emerald-500/20 border-green-400/50' 
                : 'bg-gradient-to-r from-gray-800/30 to-gray-900/30 border-gray-600/30',
              'border backdrop-blur-sm'
            ]"
          >
            <!-- Эффект свечения для разблокированных -->
            <div 
              v-if="achievement.unlocked"
              class="absolute inset-0 rounded-2xl bg-green-500/10 blur-md opacity-0 group-hover:opacity-100 transition-opacity duration-300"
            ></div>
            
            <div class="relative flex items-start gap-4">
              <!-- Иконка статуса -->
              <div class="flex-shrink-0">
                <div 
                  class="w-12 h-12 rounded-xl flex items-center justify-center transition-all duration-300 group-hover:scale-110"
                  :class="achievement.unlocked 
                    ? 'bg-gradient-to-br from-green-400 to-emerald-600 shadow-lg shadow-green-500/30' 
                    : 'bg-gradient-to-br from-gray-600 to-gray-800'"
                >
                  <!-- Иконка в зависимости от типа достижения -->
                  <svg 
                    v-if="achievement.unlocked"
                    class="w-6 h-6"
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <svg 
                    v-else
                    class="w-6 h-6 opacity-70"
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24"
                  >
                    <!-- Разные иконки для разных типов достижений -->
                    <template v-if="achievement.type?.includes('streak')">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                    </template>
                    <template v-else-if="achievement.type?.includes('score')">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </template>
                    <template v-else>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </template>
                  </svg>
                </div>
              </div>
              
              <!-- Контент достижения -->
              <div class="flex-1 min-w-0">
                <!-- Название достижения -->
                <div class="mb-2">
                  <div 
                    class="text-xl font-semibold"
                    :class="achievement.unlocked ? 'text-white' : 'text-gray-300'"
                  >
                    {{ achievement.title }}
                  </div>
                </div>
                
                <!-- Описание -->
                <div 
                  class="text-sm opacity-90 mb-3"
                >
                  {{ achievement.description }}
                </div>
                
                <!-- Прогресс бар (если нужно) -->
                <div v-if="achievement.type !== 'boolean'" class="mt-2">
                  <div class="flex justify-between text-xs mb-1">
                    <span class="text-gray-400">
                      {{ getProgressText(achievement) }}
                    </span>
                    <span class="text-gray-400">
                      {{ getProgressPercentage(achievement) }}%
                    </span>
                  </div>
                  <div class="w-full h-2 bg-gray-700 rounded-full overflow-hidden">
                    <div 
                      class="h-full rounded-full transition-all duration-500"
                      :class="getProgressBarClass(achievement)"
                      :style="{ width: `${getProgressPercentage(achievement)}%` }"
                    ></div>
                  </div>
                  
                  <!-- Отображение конкретного значения прогресса -->
                  <div v-if="showProgressValue(achievement)" class="text-xs text-center mt-1 text-gray-400">
                    {{ achievement.progress }} / {{ achievement.maxProgress }}
                  </div>
                </div>
                
                <!-- Мета информация -->
                <div class="flex flex-wrap gap-3 mt-3">
                  <!-- Категория -->
                  <div 
                    class="text-xs px-3 py-1 rounded-full bg-gradient-to-r from-blue-500/20 to-purple-500/20"
                  >
                    {{ achievement.category }}
                  </div>
                  
                  <!-- Тип достижения -->
                  <div 
                    class="text-xs px-3 py-1 rounded-full"
                    :class="getTypeBadgeClass(achievement.type)"
                  >
                    {{ getTypeText(achievement.type) }}
                  </div>
                  
                  <!-- Статус -->
                  <div 
                    class="text-xs px-3 py-1 rounded-full"
                    :class="achievement.unlocked ? 'bg-green-500/20 text-green-400' : 'bg-gray-700 text-gray-400'"
                  >
                    {{ achievement.unlocked ? 'Разблокировано' : 'Заблокировано' }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Прогресс блока -->
        <div class="mt-10 pt-6 border-t border-white/20">
          <div class="flex justify-between items-center mb-2">
            <span class="text-lg font-medium">Общий прогресс</span>
            <span class="text-xl font-bold">
              {{ stats.unlockedCount }}/{{ stats.totalCount }}
            </span>
          </div>
          <div class="w-full h-4 bg-gray-700/50 rounded-full overflow-hidden">
            <div 
              class="h-full bg-gradient-to-r from-green-400 to-emerald-500 rounded-full transition-all duration-700"
              :style="{ width: `${stats.progressPercentage}%` }"
            ></div>
          </div>
          <div class="text-center mt-3 text-sm opacity-80">
            {{ stats.progressPercentage }}% достижений разблокировано
          </div>
        </div>
        
        <!-- Статистика по категориям -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6">
          <div 
            v-for="(category, categoryName) in stats.byCategory" 
            :key="categoryName"
            class="bg-gray-800/30 rounded-xl p-4 text-center hover:bg-gray-800/50 transition-colors cursor-pointer"
            @click="scrollToCategory(categoryName)"
          >
            <div class="text-2xl font-bold" :class="getCategoryColor(categoryName)">
              {{ category.unlocked }}/{{ category.total }}
            </div>
            <div class="text-sm text-gray-400">{{ getCategoryName(categoryName) }}</div>
            <div class="mt-2 h-1 bg-gray-700 rounded-full overflow-hidden">
              <div 
                class="h-full"
                :class="getCategoryColor(categoryName, true)"
                :style="{ width: `${(category.unlocked / category.total) * 100}%` }"
              ></div>
            </div>
          </div>
        </div>
        
        <!-- Общая статистика -->
        <div class="grid grid-cols-2 gap-4 mt-6">
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-cyan-400">{{ stats.totalCount }}</div>
            <div class="text-sm text-gray-400">Всего достижений</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-yellow-400">{{ stats.remainingCount }}</div>
            <div class="text-sm text-gray-400">Осталось разблокировать</div>
          </div>
        </div>
        
        <!-- Кнопки управления -->
        <div class="mt-6 pt-6 border-t border-white/20">
          <div class="flex gap-3 justify-center">
            <button 
              @click="resetAchievements"
              class="px-4 py-2 bg-red-500/20 hover:bg-red-500/30 text-red-400 rounded-lg text-sm font-medium transition-colors border border-red-500/30"
            >
              Сбросить все достижения
            </button>
            <button 
              @click="refreshAchievements"
              class="px-4 py-2 bg-blue-500/20 hover:bg-blue-500/30 text-blue-400 rounded-lg text-sm font-medium transition-colors border border-blue-500/30"
            >
              Обновить
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { 
  loadAchievements as loadAchievementsFromStorage,
  resetAchievements as resetAchievementsFromStorage,
  getStats,
  type Achievement 
} from './utils/achievements'

// Загружаем достижения из localStorage
const achievements = ref<Achievement[]>(loadAchievementsFromStorage())

// Получаем статистику
const stats = computed(() => getStats())

// Интервал для обновления
let updateInterval: number | null = null

// Сбрасываем достижения
const resetAchievements = () => {
  if (confirm('Вы уверены, что хотите сбросить все достижения? Это действие нельзя отменить.')) {
    achievements.value = resetAchievementsFromStorage()
  }
}

// Обновляем достижения
const refreshAchievements = () => {
  achievements.value = loadAchievementsFromStorage()
}

// Для совместимости с существующим кодом
const block = {
  title: 'Мои достижения',
  subtitle: 'Путь к успеху начинается здесь',
  backgroundColor: '#1e293b',
  textColor: '#ffffff'
}

// Функции для отображения прогресса
const getProgressText = (achievement: Achievement) => {
  const type = achievement.type || 'counter'
  
  switch (type) {
    case 'streak':
      return `Лучшая серия:`
    case 'trend_streak':
      return `Серия в тренде:`
    case 'candle_streak':
      return `Серия в свечах:`
    case 'trend_counter':
      return `Побед в тренде:`
    case 'candle_counter':
      return `Побед в свечах:`
    case 'total_wins_counter':
      return `Всего побед:`
    case 'score_counter':
      return `Очков набрано:`
    default:
      return `Прогресс:`
  }
}

const getProgressPercentage = (achievement: Achievement) => {
  return Math.round((achievement.progress / achievement.maxProgress) * 100)
}

const getProgressBarClass = (achievement: Achievement) => {
  const type = achievement.type || 'counter'
  
  switch (type) {
    case 'streak':
    case 'trend_streak':
    case 'candle_streak':
      return 'bg-gradient-to-r from-orange-400 to-yellow-400'
    case 'score_counter':
      return 'bg-gradient-to-r from-yellow-400 to-yellow-300'
    case 'trend_counter':
      return 'bg-gradient-to-r from-blue-400 to-cyan-400'
    case 'candle_counter':
      return 'bg-gradient-to-r from-purple-400 to-pink-400'
    case 'total_wins_counter':
      return 'bg-gradient-to-r from-red-400 to-pink-400'
    default:
      return 'bg-gradient-to-r from-blue-400 to-cyan-400'
  }
}

const showProgressValue = (achievement: Achievement) => {
  return achievement.type !== 'boolean' && achievement.type !== 'counter'
}

const getTypeText = (type: string) => {
  switch (type) {
    case 'streak': return 'Серия'
    case 'trend_streak': return 'Тренд-серия'
    case 'candle_streak': return 'Свеча-серия'
    case 'score_counter': return 'Очки'
    case 'trend_counter': return 'Тренды'
    case 'candle_counter': return 'Свечи'
    case 'total_wins_counter': return 'Победы'
    case 'boolean': return 'Моментальное'
    default: return 'Счетчик'
  }
}

const getTypeBadgeClass = (type: string) => {
  switch (type) {
    case 'streak':
    case 'trend_streak':
    case 'candle_streak':
      return 'bg-orange-500/20 text-orange-400'
    case 'score_counter':
      return 'bg-yellow-500/20 text-yellow-400'
    case 'trend_counter':
      return 'bg-blue-500/20 text-blue-400'
    case 'candle_counter':
      return 'bg-purple-500/20 text-purple-400'
    case 'total_wins_counter':
      return 'bg-red-500/20 text-red-400'
    default:
      return 'bg-gray-700 text-gray-400'
  }
}

const getCategoryName = (categoryKey: string) => {
  const names: Record<string, string> = {
    'Старт': 'Старт',
    'Навыки': 'Навыки',
    'Мастерство': 'Мастерство',
    'Прогресс': 'Прогресс'
  }
  return names[categoryKey] || categoryKey
}

const getCategoryColor = (categoryName: string, forBar = false) => {
  const colors: Record<string, string> = {
    'Старт': forBar ? 'bg-green-500' : 'text-green-400',
    'Навыки': forBar ? 'bg-blue-500' : 'text-blue-400',
    'Мастерство': forBar ? 'bg-purple-500' : 'text-purple-400',
    'Прогресс': forBar ? 'bg-yellow-500' : 'text-yellow-400'
  }
  return colors[categoryName] || (forBar ? 'bg-gray-500' : 'text-gray-400')
}

const scrollToCategory = (categoryName: string) => {
  const element = document.querySelector(`[data-category="${categoryName}"]`)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

// Обновляем достижения каждую секунду для актуальности
onMounted(() => {
  console.log('Блок достижений загружен')
  
  updateInterval = window.setInterval(() => {
    achievements.value = loadAchievementsFromStorage()
  }, 1000)
})

// Очищаем интервал при размонтировании
onUnmounted(() => {
  if (updateInterval) {
    clearInterval(updateInterval)
  }
})
</script>

<style scoped>
.achievement-block {
  backdrop-filter: blur(10px);
}

.achievement-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px -10px rgba(0, 0, 0, 0.5);
}

/* Анимация для новых достижений */
@keyframes unlockPulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.achievement-item:has(.unlocked-new) {
  animation: unlockPulse 0.5s ease;
}
</style>