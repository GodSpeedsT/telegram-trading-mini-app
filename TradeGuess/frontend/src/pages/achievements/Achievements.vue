<template>
  <div class="min-h-screen flex items-center justify-center p-3 sm:p-4 bg-zinc-950 font-sans text-white safe-area-padding pb-nav">
    <div class="relative w-full max-w-4xl rounded-[24px] sm:rounded-[32px] overflow-hidden shadow-2xl border border-zinc-800 bg-zinc-900">
      <div class="absolute inset-0 bg-gradient-to-br from-green-900/20 via-zinc-900/80 to-black/50 pointer-events-none"></div>
      
      <div class="relative z-10 p-4 sm:p-6 md:p-8 lg:p-10">
        <!-- Header -->
        <header class="text-center mb-6 sm:mb-8 md:mb-10">
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-black mb-2 tracking-tight leading-tight">{{ block.title }}</h1>
          <p class="text-zinc-500 font-medium text-sm sm:text-base">{{ block.subtitle }}</p>
        </header>

        <!-- Achievements List -->
        <div class="space-y-3 sm:space-y-4 max-h-[calc(100vh-320px)] sm:max-h-[calc(100vh-360px)] overflow-y-auto pr-1 custom-scrollbar">
          <div v-for="achievement in achievements" :key="achievement.id"
               class="group relative p-3 sm:p-4 md:p-5 rounded-xl sm:rounded-2xl border transition-all duration-300 backdrop-blur-md"
               :class="achievement.unlocked ? 'bg-emerald-500/10 border-emerald-500/30' : 'bg-zinc-800/20 border-zinc-800'">
            
            <div class="flex items-start sm:items-center gap-3 sm:gap-4 md:gap-5">
              <!-- Icon -->
              <div class="shrink-0 w-10 h-10 sm:w-12 sm:h-12 md:w-14 md:h-14 rounded-lg sm:rounded-xl md:rounded-2xl flex items-center justify-center transition-transform group-hover:scale-110 shadow-lg"
                   :class="achievement.unlocked ? 'bg-gradient-to-br from-emerald-400 to-emerald-600' : 'bg-zinc-800'">
                <span class="text-xl sm:text-2xl">{{ achievement.unlocked ? '🏆' : '🔒' }}</span>
              </div>

              <!-- Content -->
              <div class="flex-1 min-w-0">
                <!-- Title and Category -->
                <div class="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-1 sm:gap-2 mb-1">
                  <h3 class="font-bold text-base sm:text-lg md:text-lg truncate"
                      :class="achievement.unlocked ? 'text-white' : 'text-zinc-500'">
                    {{ achievement.title }}
                  </h3>
                  <span class="text-[9px] sm:text-[10px] px-1.5 sm:px-2 py-0.5 rounded-md bg-zinc-800 text-zinc-500 font-black uppercase tracking-widest self-start sm:self-auto">
                    {{ achievement.category }}
                  </span>
                </div>

                <!-- Description -->
                <p class="text-xs sm:text-sm text-zinc-500 leading-snug line-clamp-2 sm:line-clamp-none">
                  {{ achievement.description }}
                </p>

                <!-- Progress Bar (for non-boolean achievements) -->
                <div v-if="achievement.type !== 'boolean'" class="mt-3 sm:mt-4">
                  <div class="flex justify-between text-[9px] sm:text-[10px] font-black text-zinc-600 mb-1.5 uppercase">
                    <span>Прогресс</span>
                    <span>{{ achievement.progress }} / {{ achievement.maxProgress }}</span>
                  </div>
                  <div class="h-1.5 w-full bg-zinc-800 rounded-full overflow-hidden">
                    <div class="h-full bg-emerald-500 transition-all duration-1000"
                         :style="{ width: `${Math.min((achievement.progress / achievement.maxProgress) * 100, 100)}%` }"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Reset Button -->
        <div class="mt-6 sm:mt-8">
          <button @click="handleReset" 
                  class="w-full py-3 sm:py-4 text-zinc-600 hover:text-rose-500 text-[9px] sm:text-[10px] font-bold uppercase tracking-widest transition-colors active:scale-95 touch-manipulation border border-zinc-800/50 hover:border-rose-500/30 rounded-xl sm:rounded-2xl bg-zinc-900/50 backdrop-blur-sm">
            Сбросить весь прогресс
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { loadAchievements, getStats, resetAchievements, type Achievement } from './utils/achievements';

defineOptions({ name: 'AchievementsPage' });

const achievements = ref<Achievement[]>([]);
const stats = computed(() => getStats());
const block = { title: 'Достижения', subtitle: 'Ваш путь к мастерству TradeGuess' };

const handleReset = () => {
  if (confirm('Вы уверены? Это действие необратимо.')) achievements.value = resetAchievements();
};

onMounted(() => { achievements.value = loadAchievements(); });
</script>

<style>
/* Safe area для мобильных устройств */
.safe-area-padding {
  padding-left: env(safe-area-inset-left, 12px);
  padding-right: env(safe-area-inset-right, 12px);
  padding-top: env(safe-area-inset-top, 12px);
}

/* Дополнительный отступ для навигации (70px + safe-area-inset-bottom) */
.pb-nav {
  padding-bottom: calc(70px + env(safe-area-inset-bottom, 24px));
}

/* Улучшение для touch устройств */
.touch-manipulation {
  touch-action: manipulation;
}

/* Кастомный скроллбар */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #4ade80 transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #4ade80;
  border-radius: 20px;
}

/* Улучшение для маленьких экранов */
@media (max-width: 360px) {
  .text-2xl {
    font-size: 1.5rem;
  }
}

/* Улучшение для горизонтальной ориентации на мобильных */
@media (max-height: 500px) and (orientation: landscape) {
  .min-h-screen {
    min-height: auto;
  }
  .p-3 {
    padding-top: 8px;
    padding-bottom: 8px;
  }
  .pb-nav {
    padding-bottom: calc(60px + env(safe-area-inset-bottom, 12px));
  }
  .max-h-\[calc\(100vh-320px\)\] {
    max-height: calc(100vh - 280px);
  }
}

/* Оптимизация для очень высоких экранов */
@media (min-height: 1000px) {
  .min-h-screen {
    min-height: 100vh;
  }
}

/* Line clamp для длинных текстов */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@media (min-width: 640px) {
  .line-clamp-2 {
    -webkit-line-clamp: unset;
    display: block;
  }
}

/* Плавная прокрутка для мобильных */
@media (max-width: 768px) {
  .custom-scrollbar {
    -webkit-overflow-scrolling: touch;
  }
}

/* Адаптация для разных высот экрана */
@media (max-height: 700px) {
  .max-h-\[calc\(100vh-320px\)\] {
    max-height: calc(100vh - 280px);
  }
}

@media (min-height: 900px) {
  .max-h-\[calc\(100vh-320px\)\] {
    max-height: calc(100vh - 360px);
  }
}
</style>