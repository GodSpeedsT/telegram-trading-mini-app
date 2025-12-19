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

export interface GameStats {
  totalWins: number;
  trendWins: number;
  candleWins: number;
  currentTrendStreak: number;
  currentCandleStreak: number;
  totalScore: number;
  currentStreak: number;
}

const STORAGE_KEY = 'trade_game_achievements';
const SCORE_KEY = 'trade_game_score';
const STATS_KEY = 'trade_game_stats';

const initialAchievements: Achievement[] = [
  { id: 1, title: 'Первое достижение', description: 'Зайти в игру', unlocked: true, category: 'Старт', progress: 1, maxProgress: 1, type: 'boolean' },
  { id: 2, title: 'Первая прибыль', description: 'Выиграть первую сделку', unlocked: false, category: 'Старт', progress: 0, maxProgress: 1, type: 'counter' },
  { id: 3, title: 'Самодостаточный', description: 'Выиграть 5 сделок подряд', unlocked: false, category: 'Старт', progress: 0, maxProgress: 5, type: 'streak' },
  { id: 4, title: 'Миллионер', description: 'Выиграть 15 сделок подряд', unlocked: false, category: 'Старт', progress: 0, maxProgress: 15, type: 'streak' },
  { id: 5, title: 'Трендовый гений', description: 'Определить 10 трендов подряд', unlocked: false, category: 'Навыки', progress: 0, maxProgress: 10, type: 'trend_streak' },
  { id: 6, title: 'Свечной эксперт', description: 'Предсказать 5 свечей подряд', unlocked: false, category: 'Навыки', progress: 0, maxProgress: 5, type: 'candle_streak' },
  { id: 7, title: 'Стратег', description: '50 побед в режиме "Тренд"', unlocked: false, category: 'Мастерство', progress: 0, maxProgress: 50, type: 'trend_counter' },
  { id: 8, title: 'Технический аналитик', description: '50 побед в режиме "1 Свеча"', unlocked: false, category: 'Мастерство', progress: 0, maxProgress: 50, type: 'candle_counter' },
  { id: 9, title: 'Ученик', description: 'Набрать 100 очков', unlocked: false, category: 'Прогресс', progress: 0, maxProgress: 100, type: 'score_counter' },
  { id: 10, title: 'Мастер', description: 'Набрать 1000 очков', unlocked: false, category: 'Прогресс', progress: 0, maxProgress: 1000, type: 'score_counter' },
  { id: 12, title: 'Биржевой волк', description: 'Выиграть 100 сделок всего', unlocked: false, category: 'Мастерство', progress: 0, maxProgress: 100, type: 'total_wins_counter' },
  { id: 99, title: 'Умнейший', description: 'Выполнить всё', unlocked: false, category: 'Старт', progress: 0, maxProgress: 1, type: 'boolean' }
];

export const loadAchievements = (): Achievement[] => {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (saved) return JSON.parse(saved);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(initialAchievements));
  return initialAchievements;
};

export const saveAchievements = (achievements: Achievement[]): void => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(achievements));
};

export const updateAllAchievements = (stats: GameStats): Achievement[] => {
  const achievements = loadAchievements();
  const newlyUnlocked: Achievement[] = [];
  let changed = false;

  achievements.forEach(a => {
    if (a.unlocked && a.id !== 99) return;
    const oldUnlocked = a.unlocked;
    let newProgress = a.progress;

    switch (a.type) {
      case 'streak': newProgress = Math.max(newProgress, stats.currentStreak); break;
      case 'trend_streak': newProgress = Math.max(newProgress, stats.currentTrendStreak); break;
      case 'candle_streak': newProgress = Math.max(newProgress, stats.currentCandleStreak); break;
      case 'trend_counter': newProgress = stats.trendWins; break;
      case 'candle_counter': newProgress = stats.candleWins; break;
      case 'total_wins_counter': newProgress = stats.totalWins; break;
      case 'score_counter': newProgress = stats.totalScore; break;
      case 'counter': if (stats.totalWins > 0) newProgress = Math.max(newProgress, 1); break;
    }

    if (newProgress !== a.progress) {
      a.progress = Math.min(newProgress, a.maxProgress);
      if (a.progress >= a.maxProgress && !a.unlocked) a.unlocked = true;
      changed = true;
      if (!oldUnlocked && a.unlocked) newlyUnlocked.push(a);
    }
  });

  const filteredAchievements = achievements.filter(a => a.id !== 99);
  if (filteredAchievements.every(a => a.unlocked)) {
    const last = achievements.find(a => a.id === 99);
    if (last && !last.unlocked) {
      last.unlocked = true;
      last.progress = 1;
      changed = true;
      newlyUnlocked.push(last);
    }
  }

  if (changed) saveAchievements(achievements);
  return newlyUnlocked;
};

export const getStats = () => {
  const achievements = loadAchievements();
  const unlocked = achievements.filter(a => a.unlocked).length;
  const byCategory: Record<string, { unlocked: number; total: number }> = {};

  achievements.forEach(a => {
    const cat = a.category;
    if (!byCategory[cat]) byCategory[cat] = { unlocked: 0, total: 0 };
    const currentCat = byCategory[cat];
    if (currentCat) {
      currentCat.total++;
      if (a.unlocked) currentCat.unlocked++;
    }
  });

  return {
    unlockedCount: unlocked,
    totalCount: achievements.length,
    remainingCount: achievements.length - unlocked,
    progressPercentage: Math.round((unlocked / achievements.length) * 100),
    byCategory
  };
};

export const resetAchievements = (): Achievement[] => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(initialAchievements));
  return initialAchievements;
};

export const loadGameData = () => {
  const score = localStorage.getItem(SCORE_KEY);
  const stats = localStorage.getItem(STATS_KEY);
  return {
    score: parseInt(score || '0'),
    stats: stats ? JSON.parse(stats) : { totalWins: 0, trendWins: 0, candleWins: 0, currentTrendStreak: 0, currentCandleStreak: 0 }
  };
};

export const saveGameData = (score: number, stats: GameStats): void => {
  localStorage.setItem(SCORE_KEY, score.toString());
  localStorage.setItem(STATS_KEY, JSON.stringify(stats));
};
