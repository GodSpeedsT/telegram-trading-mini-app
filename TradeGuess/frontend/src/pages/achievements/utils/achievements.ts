// utils/achievements.ts

export interface Achievement {
  id: number;
  title: string;
  description: string;
  unlocked: boolean;
  category: string;
  progress: number;
  maxProgress: number;
  type: string;
}

export interface AchievementStats {
  unlockedCount: number;
  totalCount: number;
  remainingCount: number;
  progressPercentage: number;
  byCategory: Record<string, { unlocked: number; total: number }>;
}

const STORAGE_KEY = 'trade_game_achievements';

// ЕДИНСТВЕННЫЙ источник начальных достижений
export const initialAchievements: Achievement[] = [
  {
    id: 1,
    title: 'Первое достижение',
    description: 'Зайти в игру',
    unlocked: true,
    category: 'Старт',
    progress: 1,
    maxProgress: 1,
    type: 'boolean'
  },
  {
    id: 2,
    title: 'Первая прибыль',
    description: 'Выиграть первую сделку',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 1,
    type: 'counter'
  },
  {
    id: 3,
    title: 'Самодостаточный',
    description: 'Выиграть 5 сделок подряд',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 5,
    type: 'streak'
  },
  {
    id: 4,
    title: 'Миллионер',
    description: 'Выиграть 15 сделок подряд',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 15,
    type: 'streak'
  },
  {
    id: 5,
    title: 'Трендовый гений',
    description: 'Правильно определить 10 трендов подряд',
    unlocked: false,
    category: 'Навыки',
    progress: 0,
    maxProgress: 10,
    type: 'trend_streak'
  },
  {
    id: 6,
    title: 'Свечной эксперт',
    description: 'Правильно предсказать 5 свечей подряд',
    unlocked: false,
    category: 'Навыки',
    progress: 0,
    maxProgress: 5,
    type: 'candle_streak'
  },
  {
    id: 7,
    title: 'Стратег',
    description: 'Выиграть 50 сделок в режиме "Тренд"',
    unlocked: false,
    category: 'Мастерство',
    progress: 0,
    maxProgress: 50,
    type: 'trend_counter'
  },
  {
    id: 8,
    title: 'Технический аналитик',
    description: 'Выиграть 50 сделок в режиме "1 Свеча"',
    unlocked: false,
    category: 'Мастерство',
    progress: 0,
    maxProgress: 50,
    type: 'candle_counter'
  },
  {
    id: 9,
    title: 'Ученик',
    description: 'Набрать 100 очков',
    unlocked: false,
    category: 'Прогресс',
    progress: 0,
    maxProgress: 100,
    type: 'score_counter'
  },
  {
    id: 10,
    title: 'Мастер',
    description: 'Набрать 1000 очков',
    unlocked: false,
    category: 'Прогресс',
    progress: 0,
    maxProgress: 1000,
    type: 'score_counter'
  },
  {
    id: 11,
    title: 'Легенда',
    description: 'Набрать 5000 очков',
    unlocked: false,
    category: 'Прогресс',
    progress: 0,
    maxProgress: 5000,
    type: 'score_counter'
  },
  {
    id: 12,
    title: 'Биржевой волк',
    description: 'Выиграть 100 сделок в общей сложности',
    unlocked: false,
    category: 'Мастерство',
    progress: 0,
    maxProgress: 100,
    type: 'total_wins_counter'
  },
  {
    id: 99,
    title: 'Умнейший',
    description: 'Выполнить все достижения',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 1,
    type: 'boolean'
  }
];

// Загрузка достижений из localStorage
export const loadAchievements = (): Achievement[] => {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      const parsed = JSON.parse(saved);
      
      // Проверяем структуру данных и добавляем отсутствующие достижения
      const currentIds = new Set(parsed.map((a: Achievement) => a.id));
      const missingAchievements = initialAchievements.filter(
        a => !currentIds.has(a.id)
      );
      
      if (missingAchievements.length > 0) {
        const merged = [...parsed, ...missingAchievements];
        saveAchievements(merged);
        return merged;
      }
      
      return parsed;
    }
    // Сохраняем начальные данные при первом запуске
    saveAchievements(initialAchievements);
    return initialAchievements;
  } catch (error) {
    console.error('Error loading achievements:', error);
    // При ошибке возвращаем начальные данные
    return initialAchievements;
  }
};

// Сохранение достижений в localStorage
export const saveAchievements = (achievements: Achievement[]): void => {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(achievements));
  } catch (error) {
    console.error('Error saving achievements:', error);
  }
};

// Получение статистики
export const getStats = (): AchievementStats => {
  const achievements = loadAchievements();
  const unlockedCount = achievements.filter(a => a.unlocked).length;
  const totalCount = achievements.length;
  const remainingCount = totalCount - unlockedCount;
  const progressPercentage = Math.round((unlockedCount / totalCount) * 100);
  
  // Группировка по категориям
  const byCategory: Record<string, { unlocked: number; total: number }> = {};
  
  achievements.forEach(achievement => {
    if (!byCategory[achievement.category]) {
      byCategory[achievement.category] = { unlocked: 0, total: 0 };
    }
    byCategory[achievement.category].total++;
    if (achievement.unlocked) {
      byCategory[achievement.category].unlocked++;
    }
  });
  
  return {
    unlockedCount,
    totalCount,
    remainingCount,
    progressPercentage,
    byCategory
  };
};

// Сброс всех достижений
export const resetAchievements = (): Achievement[] => {
  saveAchievements(initialAchievements);
  return initialAchievements;
};

// Обновление конкретного достижения
export const updateAchievement = (id: number, updates: Partial<Achievement>): boolean => {
  const achievements = loadAchievements();
  const index = achievements.findIndex(a => a.id === id);
  
  if (index !== -1) {
    achievements[index] = { ...achievements[index], ...updates };
    
    // Проверяем, нужно ли разблокировать
    if (!achievements[index].unlocked && 
        achievements[index].progress >= achievements[index].maxProgress) {
      achievements[index].unlocked = true;
    }
    
    saveAchievements(achievements);
    return true;
  }
  
  return false;
};

// Получение достижения по ID
export const getAchievement = (id: number): Achievement | null => {
  const achievements = loadAchievements();
  return achievements.find(a => a.id === id) || null;
};

// Проверка всех достижений
export const checkAllAchievementsCompleted = (): boolean => {
  const achievements = loadAchievements();
  // Исключаем достижение "Умнейший" из проверки
  return achievements
    .filter(a => a.id !== 99)
    .every(a => a.unlocked);
};

// Обновление прогресса достижения на основе статистики игры
export const updateAchievementProgress = (
  achievementId: number, 
  gameStats: {
    totalWins?: number;
    trendWins?: number;
    candleWins?: number;
    currentTrendStreak?: number;
    currentCandleStreak?: number;
    totalScore?: number;
    currentStreak?: number;
  }
): boolean => {
  const achievements = loadAchievements();
  const achievement = achievements.find(a => a.id === achievementId);
  
  if (!achievement || achievement.unlocked) {
    return false;
  }

  let shouldUpdate = false;
  let newProgress = achievement.progress;

  // Обновляем прогресс в зависимости от типа достижения
  switch (achievement.type) {
    case 'streak': // Общая серия побед
      if (gameStats.currentStreak !== undefined) {
        newProgress = Math.max(newProgress, gameStats.currentStreak);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'trend_streak': // Серия в режиме тренда
      if (gameStats.currentTrendStreak !== undefined) {
        newProgress = Math.max(newProgress, gameStats.currentTrendStreak);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'candle_streak': // Серия в режиме свечи
      if (gameStats.currentCandleStreak !== undefined) {
        newProgress = Math.max(newProgress, gameStats.currentCandleStreak);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'trend_counter': // Количество побед в тренде
      if (gameStats.trendWins !== undefined) {
        newProgress = Math.min(gameStats.trendWins, achievement.maxProgress);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'candle_counter': // Количество побед в свечах
      if (gameStats.candleWins !== undefined) {
        newProgress = Math.min(gameStats.candleWins, achievement.maxProgress);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'total_wins_counter': // Общее количество побед
      if (gameStats.totalWins !== undefined) {
        newProgress = Math.min(gameStats.totalWins, achievement.maxProgress);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'score_counter': // Количество очков
      if (gameStats.totalScore !== undefined) {
        newProgress = Math.min(gameStats.totalScore, achievement.maxProgress);
        shouldUpdate = newProgress !== achievement.progress;
      }
      break;
      
    case 'counter': // Простой счетчик
      newProgress = Math.min(newProgress + 1, achievement.maxProgress);
      shouldUpdate = true;
      break;
  }

  if (shouldUpdate) {
    achievement.progress = newProgress;
    
    // Проверяем, разблокировано ли достижение
    if (achievement.progress >= achievement.maxProgress && !achievement.unlocked) {
      achievement.unlocked = true;
    }

    saveAchievements(achievements);
    return true;
  }
  
  return false;
};

// Автоматическое обновление всех достижений
export const updateAllAchievements = (gameStats: {
  totalWins: number;
  trendWins: number;
  candleWins: number;
  currentTrendStreak: number;
  currentCandleStreak: number;
  totalScore: number;
  currentStreak: number;
}): boolean[] => {
  const results: boolean[] = [];
  
  // Обновляем все достижения (кроме первого и последнего)
  for (let i = 2; i <= 12; i++) {
    const updated = updateAchievementProgress(i, gameStats);
    results.push(updated);
  }
  
  // Проверяем достижение "Умнейший"
  const allCompleted = checkAllAchievementsCompleted();
  if (allCompleted) {
    updateAchievement(99, { unlocked: true, progress: 1 });
  }
  
  return results;
};