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
            v-for="achievement in block.achievements" 
            :key="achievement.id"
            class="achievement-item group relative p-5 rounded-2xl transition-all duration-400 cursor-pointer"
            :class="[
              achievement.unlocked 
                ? 'bg-gradient-to-r from-green-500/20 to-emerald-500/20 border-green-400/50' 
                : 'bg-gradient-to-r from-gray-800/30 to-gray-900/30 border-gray-600/30',
              'border backdrop-blur-sm hover:scale-[1.02] hover:shadow-xl'
            ]"
            @click="toggleAchievement(achievement.id)"
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
                    v-if="achievement.title.trim()"
                    class="text-xl font-semibold"
                    :class="achievement.unlocked ? 'text-white' : 'text-gray-300'"
                  >
                    {{ achievement.title }}
                  </div>
                </div>
                
                <!-- Описание -->
                <div 
                  v-if="achievement.description"
                  class="text-sm opacity-90 mb-3"
                >
                  {{ achievement.description }}
                </div>
                
                <!-- Мета информация -->
                <div class="flex flex-wrap gap-3 mt-3">
                  <!-- Категория -->
                  <div 
                    v-if="achievement.category"
                    class="text-xs px-3 py-1 rounded-full bg-gradient-to-r from-blue-500/20 to-purple-500/20"
                  >
                    {{ achievement.category }}
                  </div>
                </div>
              </div>
              
              <!-- Бейдж прогресса -->
              <div 
                v-if="achievement.progress !== undefined"
                class="flex-shrink-0 w-24"
              >
                <div class="text-right text-sm mb-1">
                  {{ achievement.progress }}%
                </div>
                <div class="w-full h-2 bg-gray-700 rounded-full overflow-hidden">
                  <div 
                    class="h-full bg-gradient-to-r from-blue-400 to-cyan-400 rounded-full transition-all duration-500"
                    :style="{ width: `${achievement.progress}%` }"
                  ></div>
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
              {{ unlockedCount }}/{{ block.achievements.length }}
            </span>
          </div>
          <div class="w-full h-4 bg-gray-700/50 rounded-full overflow-hidden">
            <div 
              class="h-full bg-gradient-to-r from-green-400 to-emerald-500 rounded-full transition-all duration-700"
              :style="{ width: `${progressPercentage}%` }"
            ></div>
          </div>
          <div class="text-center mt-3 text-sm opacity-80">
            {{ progressPercentage }}% достижений разблокировано
          </div>
        </div>
        
        <!-- Статистика -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6">
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-emerald-400">{{ unlockedCount }}</div>
            <div class="text-sm text-gray-400">Разблокировано</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-cyan-400">{{ block.achievements.length }}</div>
            <div class="text-sm text-gray-400">Всего достижений</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-yellow-400">{{ remainingCount }}</div>
            <div class="text-sm text-gray-400">Осталось разблокировать</div>
          </div>
          <div class="bg-gray-800/30 rounded-xl p-4 text-center">
            <div class="text-2xl font-bold text-purple-400">{{ progressPercentage }}%</div>
            <div class="text-sm text-gray-400">Прогресс</div>
          </div>
        </div>
      </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'


// ========== ТИПЫ ==========
interface Achievement {
  id: number
  title: string
  description?: string
  unlocked: boolean
  category?: string
  progress?: number
}

interface AchievementBlock {
  title: string
  subtitle: string
  achievements: Achievement[]
  backgroundColor?: string
  backgroundImage?: string
  textColor?: string
}

// ========== ДАННЫЕ ==========
const block = ref<AchievementBlock>({
  title: 'Мои достижения',
  subtitle: 'Путь к успеху начинается здесь',
  backgroundColor: '#1e293b',
  textColor: '#ffffff',
  
  achievements: [
    {
      id: 1,
      title: 'Первое достижение',
      description: 'Зайти в игру',
      unlocked: true,
      category: 'Старт',
      progress: 100
    },
    {
      id: 2,
      title: 'Первая прибыль',
      description: 'Закрыть сделку с положительным результатом',
      unlocked: true,
      category: 'Старт',
      progress: 100
    },
    {
      id: 3,
      title: 'Самодостаточный',
      description: 'Закрыть 5 сделок подряд с положительным результатом',
      unlocked: true,
      category: 'Старт',
      progress: 100
    },
    {
      id: 4,
      title: 'Миллионер',
      description: 'Закрыть 15 сделок подряд с положительным результатом',
      unlocked: true,
      category: 'Старт',
      progress: 100
    },
    {
      id: 99,
      title: 'Умнейший',
      description: 'Выполните все достижения',
      unlocked: true,
      category: 'Старт',
      progress: 100
    }
  ]
})

// ========== ВЫЧИСЛЯЕМЫЕ СВОЙСТВА ==========
const unlockedCount = computed(() => {
  return block.value.achievements.filter(a => a.unlocked).length
})

const remainingCount = computed(() => {
  return block.value.achievements.length - unlockedCount.value
})

const progressPercentage = computed(() => {
  return Math.round((unlockedCount.value / block.value.achievements.length) * 100)
})

// ========== МЕТОДЫ ==========
const toggleAchievement = (id: number) => {
  const achievement = block.value.achievements.find(a => a.id === id)
  if (achievement) {
    achievement.unlocked = !achievement.unlocked
    // Если разблокируем, устанавливаем прогресс 100%
    if (achievement.unlocked && achievement.progress !== undefined) {
      achievement.progress = 100
    }
  }
}

// ========== ХУКИ ==========
onMounted(() => {
  console.log('Блок достижений загружен')
})
</script>

<style scoped>
.achievement-block {
  background-size: cover;
  background-position: center;
}

.achievement-item {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.achievement-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
}

/* Анимация разблокировки */
@keyframes unlockPulse {
  0% { box-shadow: 0 0 0 0 rgba(34, 197, 94, 0.7); }
  70% { box-shadow: 0 0 0 10px rgba(34, 197, 94, 0); }
  100% { box-shadow: 0 0 0 0 rgba(34, 197, 94, 0); }
}

.achievement-item:has(.unlocked) {
  animation: unlockPulse 0.6s ease-out;
}
</style>