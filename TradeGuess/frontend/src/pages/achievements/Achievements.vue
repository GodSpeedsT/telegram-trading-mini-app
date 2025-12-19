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
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
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
                      Прогресс: {{ achievement.progress }}/{{ achievement.maxProgress }}
                    </span>
                    <span class="text-gray-400">
                      {{ Math.round((achievement.progress / achievement.maxProgress) * 100) }}%
                    </span>
                  </div>
                  <div class="w-full h-2 bg-gray-700 rounded-full overflow-hidden">
                    <div 
                      class="h-full bg-gradient-to-r from-blue-400 to-cyan-400 rounded-full transition-all duration-500"
                      :style="{ width: `${(achievement.progress / achievement.maxProgress) * 100}%` }"
                    ></div>
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
        
        <!-- Статистика -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6">
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-emerald-400">{{ stats.unlockedCount }}</div>
            <div class="text-sm text-gray-400">Разблокировано</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-cyan-400">{{ stats.totalCount }}</div>
            <div class="text-sm text-gray-400">Всего достижений</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-yellow-400">{{ stats.remainingCount }}</div>
            <div class="text-sm text-gray-400">Осталось разблокировать</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-purple-400">{{ stats.progressPercentage }}%</div>
            <div class="text-sm text-gray-400">Прогресс</div>
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
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { 
  loadAchievements, 
  resetAchievements as resetAchievementsFromStorage,
  getStats,
  type Achievement 
} from './utils/achievements'

// Загружаем достижения из localStorage
const achievements = ref<Achievement[]>(loadAchievements())

// Получаем статистику
const stats = computed(() => getStats())

// Сбрасываем достижения
const resetAchievements = () => {
  achievements.value = resetAchievementsFromStorage()
}

// Для совместимости с существующим кодом
const block = {
  title: 'Мои достижения',
  subtitle: 'Путь к успеху начинается здесь',
  backgroundColor: '#1e293b',
  textColor: '#ffffff'
}

// Обновляем достижения при загрузке страницы
onMounted(() => {
  console.log('Блок достижений загружен')
})
</script>