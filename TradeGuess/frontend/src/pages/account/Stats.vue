<template>
  <div class="min-h-screen flex flex-col items-center p-4 relative bg-zinc-950 overflow-hidden">
    <!-- Декоративный фон -->
    <div class="absolute inset-0 w-full h-full pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-64 h-64 bg-purple-500/5 rounded-full blur-3xl"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-emerald-500/5 rounded-full blur-3xl"></div>
    </div>
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-black/80 pointer-events-none"></div>

    <!-- Основной контейнер с учетом навигации -->
    <div class="relative z-10 w-full flex flex-col items-center overflow-y-auto pb-[calc(70px+env(safe-area-inset-bottom)+1rem)] bg-zinc-900/90 border border-zinc-800/50 shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] backdrop-blur-sm max-w-[340px] sm:max-w-[420px] md:max-w-[500px] lg:max-w-[600px] xl:max-w-[700px] 2xl:max-w-[800px] mt-4 rounded-[30px] sm:rounded-[40px] lg:rounded-[50px]">

      <!-- Декоративная полоса сверху -->
      <div class="sticky top-0 left-1/2 -translate-x-1/2 w-3/4 h-1 bg-gradient-to-r from-transparent via-emerald-500/50 to-transparent z-20 mt-4"></div>

      <!-- Заголовок -->
      <div class="flex flex-col items-center gap-3 sm:gap-4 md:gap-5 py-6 sm:py-8 md:py-10 px-4 w-full">
        <div class="flex items-center justify-center gap-3">
          <div class="w-12 h-12 sm:w-14 sm:h-14 md:w-16 md:h-16 rounded-2xl bg-gradient-to-br from-emerald-500/20 to-emerald-600/10 border border-emerald-500/30 flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 sm:w-7 sm:h-7 md:w-8 md:h-8 text-emerald-400">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
            </svg>
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl lg:text-4xl font-bold bg-gradient-to-r from-emerald-400 to-cyan-400 bg-clip-text text-transparent">
            Статистика
          </h1>
        </div>
        <p class="text-center text-zinc-400 text-sm sm:text-base md:text-lg max-w-[300px] sm:max-w-[350px]">
          Ваши результаты и прогресс в торговой игре
        </p>
      </div>

      <!-- Основная статистика в карточках -->
      <div class="w-full px-4 sm:px-6 md:px-8 flex flex-col gap-4 sm:gap-5 md:gap-6 pb-4">
        
        <!-- Карточка общего счета -->
        <div class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-zinc-700/50 rounded-2xl sm:rounded-3xl p-4 sm:p-6 shadow-xl">
          <div class="flex flex-col items-center gap-2 sm:gap-3">
            <div class="flex items-center gap-2 text-zinc-400 text-sm sm:text-base">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0a7.454 7.454 0 01-.982-3.172M9.497 14.25a7.454 7.454 0 00.981-3.172M5.25 4.236c-.982.143-1.954.317-2.916.52A6.003 6.003 0 007.73 9.728M5.25 4.236V4.5c0 2.108.966 3.99 2.48 5.228M5.25 4.236V2.721C7.456 2.41 9.71 2.25 12 2.25c2.291 0 4.545.16 6.75.47v1.516M7.73 9.728a6.726 6.726 0 002.748 1.35m8.272-6.842V4.5c0 2.108-.966 3.99-2.48 5.228m2.48-5.492a46.32 46.32 0 012.916.52 6.003 6.003 0 01-5.395 4.972m0 0a6.726 6.726 0 01-2.749 1.35m0 0a6.772 6.772 0 01-3.044 0" />
              </svg>
              <span>Общий счет</span>
            </div>
            <div class="text-4xl sm:text-5xl md:text-6xl font-bold text-emerald-400">
              {{ totalScore }}
            </div>
            <div class="text-xs sm:text-sm text-zinc-500">
              Всего заработано очков
            </div>
          </div>
        </div>

        <!-- Две карточки рядом для десктопа -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-5">
          
          <!-- Максимальный стрик -->
          <div class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-orange-500/30 rounded-2xl p-4 sm:p-5 shadow-xl">
            <div class="flex flex-col items-center gap-2">
              <div class="flex items-center gap-2 text-zinc-400 text-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15.362 5.214A8.252 8.252 0 0112 21 8.25 8.25 0 016.038 7.048 8.287 8.287 0 009 9.6a8.983 8.983 0 013.361-6.867 8.21 8.21 0 003 2.48z" />
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 18a3.75 3.75 0 00.495-7.467 5.99 5.99 0 00-1.925 3.546 5.974 5.974 0 01-2.133-1A3.75 3.75 0 0012 18z" />
                </svg>
                <span>Макс. стрик</span>
              </div>
              <div class="text-3xl sm:text-4xl font-bold text-orange-400 flex items-center gap-2">
                <span>🔥</span>
                <span>{{ maxStreak }}</span>
              </div>
              <div class="text-xs text-zinc-500 text-center">
                Подряд правильных ответов
              </div>
            </div>
          </div>

          <!-- Текущий стрик -->
          <div class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-yellow-500/30 rounded-2xl p-4 sm:p-5 shadow-xl">
            <div class="flex flex-col items-center gap-2">
              <div class="flex items-center gap-2 text-zinc-400 text-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12a7.5 7.5 0 0015 0m-15 0a7.5 7.5 0 1115 0m-15 0H3m16.5 0H21m-1.5 0H12m-8.457 3.077l1.41-.513m14.095-5.13l1.41-.513M5.106 17.785l1.15-.964m11.49-9.642l1.149-.964M7.501 19.795l.75-1.3m7.5-12.99l.75-1.3m-6.063 16.658l.26-1.477m2.605-14.772l.26-1.477m0 17.726l-.26-1.477M10.698 4.614l-.26-1.477M16.5 19.794l-.75-1.299M7.5 4.205L12 12m6.894 5.785l-1.149-.964M6.256 7.178l-1.15-.964m15.352 8.864l-1.41-.513M4.954 9.435l-1.41-.514M12.002 12l-3.75 6.495" />
                </svg>
                <span>Текущий стрик</span>
              </div>
              <div class="text-3xl sm:text-4xl font-bold text-yellow-400">
                {{ currentStreak }}
              </div>
              <div class="text-xs text-zinc-500 text-center">
                Активная серия
              </div>
            </div>
          </div>

        </div>

        <!-- Детальная статистика -->
        <div class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-zinc-700/50 rounded-2xl sm:rounded-3xl p-4 sm:p-6 shadow-xl">
          <h3 class="text-lg sm:text-xl font-bold text-white mb-4 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
            </svg>
            Детальная статистика
          </h3>
          
          <div class="space-y-3 sm:space-y-4">
            <!-- Всего раундов -->
            <div class="flex justify-between items-center">
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 rounded-lg bg-cyan-500/10 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-cyan-400">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99" />
                  </svg>
                </div>
                <span class="text-zinc-300 text-sm sm:text-base">Всего раундов</span>
              </div>
              <span class="text-white font-bold text-lg sm:text-xl">{{ totalRounds }}</span>
            </div>

            <!-- Правильные ответы -->
            <div class="flex justify-between items-center">
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 rounded-lg bg-emerald-500/10 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-emerald-400">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <span class="text-zinc-300 text-sm sm:text-base">Правильные ответы</span>
              </div>
              <span class="text-emerald-400 font-bold text-lg sm:text-xl">{{ correctAnswers }} ({{ accuracy }}%)</span>
            </div>

            <!-- Неправильные ответы -->
            <div class="flex justify-between items-center">
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 rounded-lg bg-rose-500/10 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-rose-400">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </div>
                <span class="text-zinc-300 text-sm sm:text-base">Неправильные ответы</span>
              </div>
              <span class="text-rose-400 font-bold text-lg sm:text-xl">{{ wrongAnswers }}</span>
            </div>

            <!-- Средний счет за раунд -->
            <div class="flex justify-between items-center">
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 rounded-lg bg-purple-500/10 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-purple-400">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
                  </svg>
                </div>
                <span class="text-zinc-300 text-sm sm:text-base">Средний счет за раунд</span>
              </div>
              <span class="text-purple-400 font-bold text-lg sm:text-xl">{{ averageScorePerRound }}</span>
            </div>
          </div>
        </div>

        <!-- ЗАРАБОТАННЫЕ ДОСТИЖЕНИЯ -->
        <div v-if="unlockedAchievements.length > 0" class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-emerald-500/30 rounded-2xl sm:rounded-3xl p-4 sm:p-6 shadow-xl">
          <h3 class="text-lg sm:text-xl font-bold text-white mb-4 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-emerald-400">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 013 3h-15a3 3 0 013-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0a7.454 7.454 0 01-.982-3.172M9.497 14.25a7.454 7.454 0 00.981-3.172M5.25 4.236c-.982.143-1.954.317-2.916.52A6.003 6.003 0 007.73 9.728M5.25 4.236V4.5c0 2.108.966 3.99 2.48 5.228M5.25 4.236V2.721C7.456 2.41 9.71 2.25 12 2.25c2.291 0 4.545.16 6.75.47v1.516M7.73 9.728a6.726 6.726 0 002.748 1.35m8.272-6.842V4.5c0 2.108-.966 3.99-2.48 5.228m2.48-5.492a46.32 46.32 0 012.916.52 6.003 6.003 0 01-5.395 4.972m0 0a6.726 6.726 0 01-2.749 1.35m0 0a6.772 6.772 0 01-3.044 0" />
            </svg>
            Заработанные достижения
            <span class="text-xs px-2 py-1 bg-emerald-500/20 text-emerald-400 rounded-full">
              {{ unlockedAchievements.length }}
            </span>
          </h3>
          
          <div class="space-y-3 sm:space-y-4 max-h-[280px] sm:max-h-[320px] overflow-y-auto pr-1 custom-scrollbar">
            <div v-for="achievement in unlockedAchievements" :key="achievement.id"
                 class="group relative p-3 sm:p-4 rounded-xl border bg-emerald-500/10 border-emerald-500/30 transition-all duration-300 hover:bg-emerald-500/15">
              
              <div class="flex items-start sm:items-center gap-3 sm:gap-4">
                <!-- Icon -->
                <div class="shrink-0 w-10 h-10 sm:w-12 sm:h-12 rounded-lg flex items-center justify-center bg-gradient-to-br from-emerald-400 to-emerald-600 shadow-lg">
                  <span class="text-xl sm:text-2xl">🏆</span>
                </div>

                <!-- Content -->
                <div class="flex-1 min-w-0">
                  <!-- Title and Category -->
                  <div class="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-1 sm:gap-2 mb-1">
                    <h3 class="font-bold text-base sm:text-lg text-white truncate">
                      {{ achievement.title }}
                    </h3>
                    <span class="text-[9px] sm:text-[10px] px-1.5 sm:px-2 py-0.5 rounded-md bg-zinc-800 text-zinc-500 font-black uppercase tracking-widest self-start sm:self-auto">
                      {{ achievement.category }}
                    </span>
                  </div>

                  <!-- Description -->
                  <p class="text-xs sm:text-sm text-emerald-200/80 leading-snug">
                    {{ achievement.description }}
                  </p>

                  <!-- Date earned -->
                  <div v-if="achievement.earnedDate" class="mt-2 flex items-center gap-1 text-[10px] sm:text-xs text-emerald-300/70">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-3 h-3">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5" />
                    </svg>
                    <span>Получено: {{ formatDate(achievement.earnedDate) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Прогресс по достижениям -->
          <div class="mt-4 pt-4 border-t border-emerald-500/20">
            <div class="flex items-center justify-between text-sm">
              <span class="text-zinc-400">Общий прогресс достижений:</span>
              <span class="font-bold text-emerald-400">{{ achievementsProgress }}%</span>
            </div>
            <div class="h-2 mt-2 w-full bg-zinc-800 rounded-full overflow-hidden">
              <div class="h-full bg-gradient-to-r from-emerald-400 to-emerald-600 transition-all duration-1000"
                   :style="{ width: achievementsProgress + '%' }"></div>
            </div>
            <div class="flex justify-between text-xs text-zinc-500 mt-1">
              <span>{{ unlockedAchievements.length }} из {{ allAchievements.length }} разблокировано</span>
            </div>
          </div>
        </div>

        <!-- Достижения в процессе -->
        <div v-if="lockedAchievements.length > 0" class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-zinc-700/50 rounded-2xl sm:rounded-3xl p-4 sm:p-6 shadow-xl">
          <h3 class="text-lg sm:text-xl font-bold text-white mb-4 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-zinc-500">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z" />
            </svg>
            Достижения в процессе
            <span class="text-xs px-2 py-1 bg-zinc-800 text-zinc-400 rounded-full">
              {{ lockedAchievements.length }}
            </span>
          </h3>
          
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div v-for="achievement in lockedAchievements.slice(0, 4)" :key="achievement.id"
                 class="p-3 rounded-xl bg-zinc-800/20 border border-zinc-800">
              
              <div class="flex items-center gap-3">
                <!-- Icon -->
                <div class="shrink-0 w-8 h-8 rounded-lg bg-zinc-800 flex items-center justify-center">
                  <span class="text-lg">🔒</span>
                </div>

                <!-- Content -->
                <div class="flex-1 min-w-0">
                  <h4 class="font-medium text-sm text-zinc-500 truncate">{{ achievement.title }}</h4>
                  
                  <!-- Progress for non-boolean achievements -->
                  <div v-if="achievement.type !== 'boolean'" class="mt-2">
                    <div class="flex justify-between text-[9px] text-zinc-600 mb-1">
                      <span>{{ Math.min(achievement.progress, achievement.maxProgress) }} / {{ achievement.maxProgress }}</span>
                    </div>
                    <div class="h-1.5 w-full bg-zinc-800 rounded-full overflow-hidden">
                      <div class="h-full bg-zinc-600 transition-all duration-1000"
                           :style="{ width: `${Math.min((achievement.progress / achievement.maxProgress) * 100, 100)}%` }"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="lockedAchievements.length > 4" class="mt-4 text-center">
            <button @click="viewAllAchievements"
                    class="text-xs text-zinc-500 hover:text-zinc-300 transition-colors">
              + ещё {{ lockedAchievements.length - 4 }} достижений →
            </button>
          </div>
        </div>

        <!-- Прогресс по режимам -->
        <div class="bg-gradient-to-br from-zinc-800/80 to-zinc-900/80 border border-zinc-700/50 rounded-2xl sm:rounded-3xl p-4 sm:p-6 shadow-xl">
          <h3 class="text-lg sm:text-xl font-bold text-white mb-4">Прогресс по режимам</h3>
          
          <div class="space-y-4">
            <!-- Режим 1 свеча -->
            <div>
              <div class="flex justify-between items-center mb-2">
                <span class="text-zinc-300 text-sm sm:text-base">1 свеча</span>
                <span class="text-white font-bold">{{ candleModeCorrect }}/{{ candleModeTotal }}</span>
              </div>
              <div class="h-2 bg-zinc-800 rounded-full overflow-hidden">
                <div class="h-full bg-cyan-500 rounded-full transition-all duration-500" 
                     :style="{ width: candleModeProgress + '%' }"></div>
              </div>
            </div>

            <!-- Режим тренд -->
            <div>
              <div class="flex justify-between items-center mb-2">
                <span class="text-zinc-300 text-sm sm:text-base">Тренд</span>
                <span class="text-white font-bold">{{ trendModeCorrect }}/{{ trendModeTotal }}</span>
              </div>
              <div class="h-2 bg-zinc-800 rounded-full overflow-hidden">
                <div class="h-full bg-emerald-500 rounded-full transition-all duration-500" 
                     :style="{ width: trendModeProgress + '%' }"></div>
              </div>
            </div>
          </div>
        </div>

      </div>

      <!-- Кнопка Назад внутри контейнера -->
      <div class="w-full px-4 sm:px-6 md:px-8 pb-4 mt-2">
        <button @click="goBack" 
                class="relative w-full bg-zinc-800/50 hover:bg-zinc-700/50 border border-zinc-700/50 hover:border-zinc-500/50 rounded-2xl shadow-lg flex items-center justify-center transition-all duration-300 active:scale-95 h-[60px] sm:h-[64px] md:h-[68px]">
          <div class="absolute left-4 sm:left-6 flex items-center justify-center text-zinc-400 bg-zinc-500/5 rounded-full w-9 h-9 sm:w-10 sm:h-10">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5 sm:w-5 sm:h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 15L3 9m0 0l6-6M3 9h12a6 6 0 010 12h-3" />
            </svg>
          </div>
          <div class="text-center flex flex-col">
            <span class="text-zinc-300 font-bold leading-tight text-lg sm:text-lg md:text-xl">Назад в профиль</span>
          </div>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Интерфейс для достижений
interface Achievement {
  id: number
  title: string
  description: string
  category: string
  unlocked: boolean
  type: 'boolean' | 'progressive'
  progress?: number
  maxProgress?: number
  earnedDate?: string
  icon?: string
}

// Данные статистики
const totalScore = ref(0)
const maxStreak = ref(0)
const currentStreak = ref(0)
const totalRounds = ref(0)
const correctAnswers = ref(0)
const wrongAnswers = ref(0)

// Статистика по режимам
const candleModeTotal = ref(0)
const candleModeCorrect = ref(0)
const trendModeTotal = ref(0)
const trendModeCorrect = ref(0)

// Все достижения
const allAchievements = ref<Achievement[]>([
  { id: 1, title: 'Новичок', description: 'Сыграйте первую игру', category: 'НАЧАЛО', unlocked: true, type: 'boolean', earnedDate: '2024-01-15', icon: '🎯' },
  { id: 2, title: 'Стрик 10+', description: 'Достигните стрика из 10 правильных ответов подряд', category: 'СТРИКИ', unlocked: true, type: 'progressive', progress: 15, maxProgress: 10, earnedDate: '2024-01-20', icon: '🔥' },
  { id: 3, title: 'Точность 80%', description: 'Достигните точности 80% за 50 игр', category: 'ТОЧНОСТЬ', unlocked: true, type: 'progressive', progress: 85, maxProgress: 80, earnedDate: '2024-01-25', icon: '🎯' },
  { id: 4, title: 'Мастер свечей', description: 'Пройдите 50 раундов в режиме "1 свеча"', category: 'РЕЖИМЫ', unlocked: false, type: 'progressive', progress: 30, maxProgress: 50, icon: '📊' },
  { id: 5, title: 'Трендовый аналитик', description: 'Пройдите 50 раундов в режиме "Тренд"', category: 'РЕЖИМЫ', unlocked: false, type: 'progressive', progress: 40, maxProgress: 50, icon: '📈' },
  { id: 6, title: '1000 очков', description: 'Заработайте 1000 очков в игре', category: 'ОЧКИ', unlocked: false, type: 'progressive', progress: 850, maxProgress: 1000, icon: '🏆' },
  { id: 7, title: 'Безошибочный день', description: 'Пройдите день без ошибок', category: 'СОВЕРШЕНСТВО', unlocked: false, type: 'boolean', icon: '⭐' },
  { id: 8, title: 'Ветеран', description: 'Сыграйте 100 раундов', category: 'ВЫНОСЛИВОСТЬ', unlocked: false, type: 'progressive', progress: 120, maxProgress: 100, icon: '🛡️' },
])

// Вычисляемые значения
const accuracy = computed(() => {
  if (totalRounds.value === 0) return 0
  return Math.round((correctAnswers.value / totalRounds.value) * 100)
})

const averageScorePerRound = computed(() => {
  if (totalRounds.value === 0) return 0
  return Math.round(totalScore.value / totalRounds.value)
})

const candleModeProgress = computed(() => {
  if (candleModeTotal.value === 0) return 0
  return Math.round((candleModeCorrect.value / candleModeTotal.value) * 100)
})

const trendModeProgress = computed(() => {
  if (trendModeTotal.value === 0) return 0
  return Math.round((trendModeCorrect.value / trendModeTotal.value) * 100)
})

// Достижения
const unlockedAchievements = computed(() => 
  allAchievements.value.filter(a => a.unlocked).sort((a, b) => 
    new Date(b.earnedDate || 0).getTime() - new Date(a.earnedDate || 0).getTime()
  )
)

const lockedAchievements = computed(() => 
  allAchievements.value.filter(a => !a.unlocked)
)

const achievementsProgress = computed(() => {
  const unlocked = unlockedAchievements.value.length
  const total = allAchievements.value.length
  return Math.round((unlocked / total) * 100)
})

// Форматирование даты
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('ru-RU', {
    day: '2-digit',
    month: 'long',
    year: 'numeric'
  })
}

// Загрузка статистики из localStorage
const loadStatistics = () => {
  try {
    const gameData = JSON.parse(localStorage.getItem('tradeGameStats') || '{}')
    
    totalScore.value = gameData.totalScore || 0
    maxStreak.value = gameData.maxStreak || 0
    currentStreak.value = gameData.currentStreak || 0
    totalRounds.value = gameData.totalRounds || 0
    correctAnswers.value = gameData.correctAnswers || 0
    wrongAnswers.value = gameData.wrongAnswers || 0
    
    // Данные по режимам
    const modesData = gameData.modes || {}
    candleModeTotal.value = modesData.candle?.total || 0
    candleModeCorrect.value = modesData.candle?.correct || 0
    trendModeTotal.value = modesData.trend?.total || 0
    trendModeCorrect.value = modesData.trend?.correct || 0
    
    // Загружаем достижения из отдельного хранилища
    const savedAchievements = JSON.parse(localStorage.getItem('achievements') || 'null')
    if (savedAchievements) {
      allAchievements.value = savedAchievements
    }
    
  } catch (error) {
    console.error('Ошибка загрузки статистики:', error)
  }
}

// Инициализация тестовых данных
const initializeDemoData = () => {
  const hasStats = localStorage.getItem('tradeGameStats')
  const hasAchievements = localStorage.getItem('achievements')
  
  if (!hasStats) {
    const demoData = {
      totalScore: 850,
      maxStreak: 15,
      currentStreak: 5,
      totalRounds: 120,
      correctAnswers: 85,
      wrongAnswers: 35,
      modes: {
        candle: { total: 60, correct: 45 },
        trend: { total: 60, correct: 40 }
      },
      lastUpdated: new Date().toISOString()
    }
    
    localStorage.setItem('tradeGameStats', JSON.stringify(demoData))
  }
  
  if (!hasAchievements) {
    localStorage.setItem('achievements', JSON.stringify(allAchievements.value))
  }
  
  loadStatistics()
}

// Навигация
const goBack = () => {
  router.push({ path: '/account' })
}

const viewAllAchievements = () => {
  router.push({ path: '/achievements' })
}

onMounted(() => {
  initializeDemoData()
})
</script>

<style scoped>
/* Анимация появления карточек */
.bg-gradient-to-br {
  transition: all 0.3s ease;
}

.bg-gradient-to-br:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
}

/* Кастомный скроллбар */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #10b981 transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #10b981;
  border-radius: 20px;
}

/* Анимация прогресс-баров */
@keyframes progressFill {
  from { width: 0; }
  to { width: var(--target-width); }
}

.h-2 > div {
  animation: progressFill 1s ease-out;
}

/* Safe area padding для навигации */
:deep(.pb-nav) {
  padding-bottom: calc(70px + env(safe-area-inset-bottom, 24px));
}

/* Улучшение для touch устройств */
.touch-manipulation {
  touch-action: manipulation;
}
</style>