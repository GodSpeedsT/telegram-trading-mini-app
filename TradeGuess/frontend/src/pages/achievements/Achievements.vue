<template>
  <div class="h-[100dvh] w-full flex flex-col items-center justify-center p-4 pb-32 md:pb-6 relative bg-[#09090b] overflow-hidden">
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
    <div class="relative z-10 w-full max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[700px] xl:max-w-[850px] flex flex-col items-center bg-zinc-900 border border-zinc-800 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] overflow-hidden transition-all duration-500 rounded-[40px] max-h-[75vh]">
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-zinc-700/50 to-transparent opacity-50 animate-pulse"></div>
      <div class="flex flex-col items-center shrink-0 w-full px-6 pt-8 gap-5">
        <div class="w-16 h-16 bg-zinc-800 rounded-full border-2 border-zinc-700 flex items-center justify-center shadow-2xl relative overflow-visible shrink-0 transition-transform hover:scale-110">
          <div class="relative flex items-center justify-center w-full h-full">
            <svg viewBox="0 0 24 24" fill="currentColor" class="absolute w-12 h-12 text-yellow-500 blur-xl opacity-40 animate-pulse" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8"/>
            </svg>
            <svg viewBox="0 0 24 24" fill="currentColor" class="relative w-8 h-8 text-yellow-400 drop-shadow-[0_0_12px_rgba(234,179,8,0.7)]" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0-11l1.15 2.33 2.57.37-1.86 1.81.44 2.56L12 9.86l-2.3 1.21.44-2.56-1.86-1.81 2.57-.37L12 4z M8 14v8l4-3 4 3v-8"/>
            </svg>
          </div>
        </div>
        <div class="flex flex-col items-center gap-3 text-center">
          <h1 class="text-3xl md:text-5xl font-black text-white uppercase tracking-tighter leading-none">{{ block.title }}</h1>
          <div class="flex items-center gap-3">
            <div class="flex items-center gap-2 px-3 py-1 bg-zinc-950/50 border border-zinc-800 rounded-full shadow-inner">
              <div class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_8px_rgba(16,185,129,0.5)]"></div>
              <span class="text-[10px] font-black text-zinc-400 uppercase tracking-widest">{{ stats.unlockedCount }}/{{ stats.totalCount }}</span>
            </div>
            <span class="text-[9px] font-bold text-zinc-600 uppercase tracking-[0.2em]">{{ block.subtitle }}</span>
          </div>
        </div>
      </div>
      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-5 md:px-10 py-6 flex flex-col gap-3">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3 md:gap-4">
          <div v-for="achievement in achievements" :key="achievement.id" class="relative border transition-all duration-300 flex flex-col p-4 gap-3 overflow-hidden rounded-2xl shrink-0" :class="achievement.unlocked
                 ? 'bg-emerald-500/10 border-emerald-500/40 shadow-[0_0_20px_rgba(16,185,129,0.05)]'
                 : 'bg-zinc-800/40 border-zinc-800 hover:border-zinc-700'">
            <div class="flex items-center gap-3">
              <div class="shrink-0 flex items-center justify-center rounded-xl border border-white/5 shadow-xl text-lg w-10 h-10" :class="achievement.unlocked ? 'bg-gradient-to-br from-emerald-400 to-emerald-600' : 'bg-zinc-700/50'">
                {{ achievement.unlocked ? '🏆' : '🔒' }}
              </div>
              <div class="flex flex-col min-w-0 flex-1">
                <div class="flex items-center justify-between gap-2 leading-tight">
                  <h3 class="font-bold truncate text-sm" :class="achievement.unlocked ? 'text-white' : 'text-zinc-500'">
                    {{ achievement.title }}
                  </h3>
                  <span class="text-[7px] bg-zinc-950/80 text-zinc-500 px-1.5 py-0.5 rounded font-black uppercase tracking-tighter shrink-0">{{ achievement.category }}</span>
                </div>
                <p class="text-[10px] text-zinc-500 font-medium leading-snug line-clamp-1">
                  {{ achievement.description }}
                </p>
              </div>
            </div>
            <div v-if="achievement.type !== 'boolean'" class="flex flex-col gap-1 mt-auto">
              <div class="flex justify-between text-[8px] font-black uppercase text-zinc-600 tracking-tighter">
                <span>Прогресс</span>
                <span>{{ achievement.progress }} / {{ achievement.maxProgress }}</span>
              </div>
              <div class="h-1 w-full bg-zinc-950 rounded-full overflow-hidden shadow-inner">
                <div class="h-full bg-emerald-500 transition-all duration-1000" :style="{ width: `${Math.min((achievement.progress / achievement.maxProgress) * 100, 100)}%` }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="w-full flex flex-col shrink-0 bg-zinc-900 border-t border-zinc-800 z-40 px-8 py-6 gap-4">
        <button @click="openResetModal" class="relative w-full bg-zinc-800 hover:bg-zinc-700 border border-zinc-700 hover:border-rose-500/50 rounded-2xl shadow-xl flex items-center justify-center transition-all duration-300 active:scale-95 h-[56px]">
          <span class="text-zinc-500 font-black uppercase tracking-[0.2em] text-[10px]">Сбросить весь прогресс</span>
        </button>
        <div v-if="stats" class="flex items-center justify-center">
          <div class="px-4 py-1.5 bg-zinc-950/60 border border-zinc-800 rounded-full flex items-center gap-2 backdrop-blur-sm shadow-inner">
            <div class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_10px_rgba(16,185,129,0.5)]"></div>
            <span class="text-[9px] text-zinc-600 font-black uppercase tracking-widest whitespace-nowrap">Уровень мастерства: {{ stats.progressPercentage }}%</span>
          </div>
        </div>
      </div>
    </div>
    <transition name="modal">
      <div v-if="isResetModalOpen" class="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-black/80 backdrop-blur-sm">
        <div class="bg-zinc-900 border border-zinc-800 rounded-[32px] p-8 w-full max-w-[400px] flex flex-col items-center gap-6 shadow-2xl">
          <div class="w-16 h-16 bg-rose-500/10 rounded-full flex items-center justify-center border-2 border-rose-500/20 text-rose-500 text-3xl font-bold">!</div>
          <div class="text-center flex flex-col gap-2">
            <h2 class="text-xl font-black text-white uppercase tracking-tighter">Вы уверены?</h2>
            <p class="text-zinc-500 text-sm font-medium leading-relaxed">Все достижения будут удалены навсегда.</p>
          </div>
          <div class="flex flex-col w-full gap-3">
            <button @click="confirmReset" class="w-full py-4 bg-rose-500 hover:bg-rose-600 text-white rounded-2xl font-black uppercase tracking-widest text-xs transition-all active:scale-95">Да, сбросить</button>
            <button @click="isResetModalOpen = false" class="w-full py-4 bg-zinc-800 hover:bg-zinc-700 text-zinc-400 rounded-2xl font-black uppercase tracking-widest text-xs transition-all active:scale-95">Отмена</button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { loadAchievements, getStats, resetAchievements, type Achievement } from './utils/achievements';

defineOptions({ name: 'AchievementsPage' });

const achievements = ref<Achievement[]>([]);
const isResetModalOpen = ref(false);
const stats = computed(() => getStats());
const block = { title: 'Достижения', subtitle: 'Ваш путь к мастерству TradeGuess' };

const openResetModal = () => {
  isResetModalOpen.value = true;
};

const confirmReset = () => {
  achievements.value = resetAchievements();
  isResetModalOpen.value = false;
};

onMounted(() => {
  achievements.value = loadAchievements();
});
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.modal-enter-active, .modal-leave-active { transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); }
.modal-enter-from, .modal-leave-to { opacity: 0; transform: scale(0.9); }
</style>
