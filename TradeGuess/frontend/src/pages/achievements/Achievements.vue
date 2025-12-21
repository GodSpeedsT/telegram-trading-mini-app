<template>
  <div class="h-[100dvh] w-full flex flex-col items-center justify-center p-4 pb-28 md:pb-6 pt-4 relative bg-[#09090b] overflow-hidden">
    <div class="absolute inset-0 w-full h-full pointer-events-none z-0">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="puzzlePattern" x="0" y="0" width="200" height="200" patternUnits="userSpaceOnUse">
            <path id="p" d="M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 0 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37"
                  fill="none" stroke="white" stroke-width="1.5" opacity="0.08" />
            <use href="#p" stroke="black" stroke-width="4" opacity="0.6" transform="translate(1,1)" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#puzzlePattern)" />
      </svg>
    </div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none z-1"></div>
    <div class="relative z-10 w-full max-w-[98%] sm:max-w-[650px] md:max-w-[950px] lg:max-w-[1200px] xl:max-w-[1500px] flex flex-col items-center bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] overflow-hidden transition-all duration-500 rounded-[32px] md:rounded-[56px] h-fit max-h-[80vh]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>
      <div class="flex items-center justify-between shrink-0 w-full px-5 md:px-12 py-4 md:py-6 bg-zinc-900/50 border-b border-zinc-800/50 gap-4">
        <div class="flex items-center gap-3 md:gap-5">
          <div class="w-10 h-10 md:w-16 md:h-16 bg-zinc-800 rounded-2xl border border-zinc-700 flex items-center justify-center shadow-lg relative overflow-visible shrink-0 transition-transform hover:scale-110">
            <svg viewBox="0 0 24 24" fill="currentColor" class="absolute w-8 h-8 md:w-14 md:h-14 text-yellow-500 blur-lg opacity-40 animate-pulse" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8"/>
            </svg>
            <svg viewBox="0 0 24 24" fill="currentColor" class="relative w-6 h-6 md:w-10 md:h-10 text-yellow-400 drop-shadow-md" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8"/>
            </svg>
          </div>
          <div class="flex flex-col text-left">
            <h1 class="text-xl md:text-3xl font-black text-white uppercase tracking-tighter leading-none">{{ block.title }}</h1>
            <span class="text-[8px] md:text-xs font-bold text-zinc-500 uppercase tracking-widest">{{ block.subtitle }}</span>
          </div>
        </div>
        <div class="flex items-center gap-2 px-3 py-1.5 md:px-5 md:py-2 bg-zinc-950/80 border border-zinc-800 rounded-2xl shadow-inner shrink-0">
          <div class="w-1.5 h-1.5 md:w-2 md:h-2 bg-emerald-500 rounded-full shadow-[0_0_8px_rgba(16,185,129,0.5)]"></div>
          <span class="text-[10px] md:text-base font-black text-zinc-300 uppercase tracking-wider font-mono">{{ stats.unlockedCount }}/{{ stats.totalCount }}</span>
        </div>
      </div>
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-4 md:px-12 lg:px-16 py-4 md:py-8">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-2 md:gap-4 lg:gap-5">
          <div v-for="achievement in achievements" :key="achievement.id" class="relative border transition-all duration-300 flex items-center p-3 md:p-5 lg:p-6 gap-3 md:gap-5 overflow-hidden rounded-2xl md:rounded-[32px] shrink-0"
               :class="achievement.unlocked
                 ? 'bg-emerald-500/10 border-emerald-500/30 shadow-[0_0_20px_rgba(16,185,129,0.03)]'
                 : 'bg-zinc-800/30 border-zinc-800 hover:border-zinc-700'">
            <div class="shrink-0 flex items-center justify-center rounded-xl md:rounded-2xl border border-white/5 shadow-md text-base md:text-3xl w-10 h-10 md:w-16 md:h-16" :class="achievement.unlocked ? 'bg-gradient-to-br from-emerald-400 to-emerald-600' : 'bg-zinc-700/50'">
              {{ achievement.unlocked ? '🏆' : '🔒' }}
            </div>
            <div class="flex flex-col min-w-0 flex-1 gap-1">
              <div class="flex items-center justify-between gap-2 leading-none">
                <h3 class="font-bold truncate text-[11px] md:text-lg" :class="achievement.unlocked ? 'text-white' : 'text-zinc-500'">
                  {{ achievement.title }}
                </h3>
                <span class="text-[6px] md:text-[8px] bg-zinc-950 text-zinc-600 px-1.5 py-0.5 rounded-md font-black uppercase tracking-tighter shrink-0 border border-zinc-800/50">{{ achievement.category }}</span>
              </div>
              <p class="text-[9px] md:text-xs text-zinc-500 font-medium leading-tight line-clamp-2">
                {{ achievement.description }}
              </p>
              <div v-if="achievement.type !== 'boolean'" class="flex flex-col gap-1 md:gap-1.5 pt-1">
                <div class="flex justify-between text-[7px] md:text-[9px] font-black uppercase text-zinc-700 tracking-tighter">
                  <span>{{ achievement.progress }}/{{ achievement.maxProgress }}</span>
                  <span>{{ Math.round((achievement.progress / achievement.maxProgress) * 100) }}%</span>
                </div>
                <div class="h-0.5 md:h-1 w-full bg-zinc-950/50 rounded-full overflow-hidden">
                  <div class="h-full bg-emerald-500 transition-all duration-1000" :style="{ width: `${Math.min((achievement.progress / achievement.maxProgress) * 100, 100)}%` }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="w-full shrink-0 bg-zinc-950/40 border-t border-zinc-800/50 z-40 px-6 py-3 md:py-4 flex items-center justify-center">
        <div v-if="stats" class="flex items-center gap-3 md:gap-4 px-4 py-1.5 bg-zinc-900 border border-zinc-800 rounded-full shadow-inner">
          <div class="relative flex items-center justify-center">
            <div class="absolute w-4 h-4 bg-emerald-500/20 rounded-full animate-ping"></div>
            <div class="w-1.5 h-1.5 md:w-2 md:h-2 bg-emerald-500 rounded-full shadow-[0_0_8px_rgba(16,185,129,0.6)]"></div>
          </div>
          <span class="text-[9px] md:text-sm text-zinc-400 font-black uppercase tracking-widest whitespace-nowrap">Прогресс мастерства: {{ stats.progressPercentage }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { loadAchievements, getStats, type Achievement } from './utils/achievements';

defineOptions({ name: 'AchievementsPage' });

const achievements = ref<Achievement[]>([]);
const stats = computed(() => getStats());
const block = {
  title: 'Достижения',
  subtitle: 'Мастерство TradeGuess'
};

onMounted(() => {
  achievements.value = loadAchievements();
});
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
