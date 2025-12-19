<template>
  <div class="min-h-screen flex items-center justify-center p-4 bg-zinc-950 font-sans pb-28 text-white">
    <div class="relative w-full max-w-4xl rounded-[32px] overflow-hidden shadow-2xl border border-zinc-800 bg-zinc-900">
      <div class="absolute inset-0 bg-gradient-to-br from-green-900/20 via-zinc-900/80 to-black/50 pointer-events-none"></div>
      <div class="relative z-10 p-6 sm:p-10">
        <header class="text-center mb-10">
          <h1 class="text-3xl sm:text-4xl font-black mb-2 tracking-tight">{{ block.title }}</h1>
          <p class="text-zinc-500 font-medium">{{ block.subtitle }}</p>
        </header>
        <div class="space-y-4">
          <div v-for="achievement in achievements" :key="achievement.id"
               class="group relative p-5 rounded-2xl border transition-all duration-300 backdrop-blur-md"
               :class="achievement.unlocked ? 'bg-emerald-500/10 border-emerald-500/30' : 'bg-zinc-800/20 border-zinc-800'">
            <div class="flex items-center gap-5">
              <div class="shrink-0 w-14 h-14 rounded-2xl flex items-center justify-center transition-transform group-hover:scale-110 shadow-lg"
                   :class="achievement.unlocked ? 'bg-gradient-to-br from-emerald-400 to-emerald-600' : 'bg-zinc-800'">
                <span class="text-2xl">{{ achievement.unlocked ? '🏆' : '🔒' }}</span>
              </div>
              <div class="flex-1 min-w-0">
                <div class="flex justify-between items-start mb-1">
                  <h3 class="font-bold text-lg" :class="achievement.unlocked ? 'text-white' : 'text-zinc-500'">{{ achievement.title }}</h3>
                  <span class="text-[10px] px-2 py-0.5 rounded-md bg-zinc-800 text-zinc-500 font-black uppercase tracking-widest">{{ achievement.category }}</span>
                </div>
                <p class="text-sm text-zinc-500 leading-snug">{{ achievement.description }}</p>
                <div v-if="achievement.type !== 'boolean'" class="mt-4">
                  <div class="flex justify-between text-[10px] font-black text-zinc-600 mb-1.5 uppercase">
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

        <div class="mt-12 grid grid-cols-2 sm:grid-cols-4 gap-4 text-center">
          <div v-for="(cat, name) in stats.byCategory" :key="name" class="bg-zinc-800/50 p-4 rounded-2xl border border-zinc-800/50">
            <div class="text-2xl font-black text-emerald-400">{{ cat.unlocked }}/{{ cat.total }}</div>
            <div class="text-[10px] font-bold text-zinc-500 uppercase mt-1">{{ name }}</div>
          </div>
        </div>
        <button @click="handleReset" class="w-full mt-10 py-4 text-zinc-600 hover:text-rose-500 text-[10px] font-bold uppercase tracking-widest transition-colors">
          Сбросить весь прогресс
        </button>
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
