export interface Achievement {
  id: number
  title: string
  description: string
  unlocked: boolean
  category: string
  progress: number
  maxProgress: number
  type: 'counter' | 'boolean' | 'streak'
}

export interface Notification {
  id: number
  title: string
  description: string
}

// Начальные данные достижений
const initialAchievements: Achievement[] = [
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
    description: 'Закрыть сделку с положительным результатом',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 1,
    type: 'counter'
  },
  {
    id: 3,
    title: 'Самодостаточный',
    description: 'Закрыть 5 сделок подряд с положительным результатом',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 5,
    type: 'counter'
  },
  {
    id: 4,
    title: 'Миллионер',
    description: 'Закрыть 15 сделок подряд с положительным результатом',
    unlocked: false,
    category: 'Старт',
    progress: 0,
    maxProgress: 15,
    type: 'counter'
  },
  {
    id: 5,
    title: 'Трендовый гений',
    description: 'Правильно определить 10 трендов подряд',
    unlocked: false,
    category: 'Навыки',
    progress: 0,
    maxProgress: 10,
    type: 'streak'
  },
  {
    id: 6,
    title: 'Свечной эксперт',
    description: 'Правильно предсказать 5 свечей подряд',
    unlocked: false,
    category: 'Навыки',
    progress: 0,
    maxProgress: 5,
    type: 'streak'
  },
  {
    id: 7,
    title: 'Стратег',
    description: 'Выиграть 50 сделок в режиме "Тренд"',
    unlocked: false,
    category: 'Мастерство',
    progress: 0,
    maxProgress: 50,
    type: 'counter'
  },
  {
    id: 8,
    title: 'Технический аналитик',
    description: 'Выиграть 50 сделок в режиме "1 Свеча"',
    unlocked: false,
    category: 'Мастерство',
    progress: 0,
    maxProgress: 50,
    type: 'counter'
  },
  {
    id: 9,
    title: 'Ученик',
    description: 'Набрать 100 очков',
    unlocked: false,
    category: 'Прогресс',
    progress: 0,
    maxProgress: 100,
    type: 'counter'
  },
  {
    id: 10,
    title: 'Мастер',
    description: 'Набрать 1000 очков',
    unlocked: false,
    category: 'Прогресс',
    progress: 0,
    maxProgress: 1000,
    type: 'counter'
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
]

const STORAGE_KEY = 'trade_game_achievements'
const NOTIFICATIONS_KEY = 'trade_game_notifications'

// Сохраняем достижения в localStorage
export const saveAchievements = (achievements: Achievement[]) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(achievements))
}

// Загружаем достижения из localStorage
export const loadAchievements = (): Achievement[] => {
  const saved = localStorage.getItem(STORAGE_KEY)
  if (saved) {
    return JSON.parse(saved)
  }
  // Если сохраненных данных нет, возвращаем начальные
  saveAchievements(initialAchievements)
  return initialAchievements
}

// Сохраняем уведомления
export const saveNotifications = (notifications: Notification[]) => {
  localStorage.setItem(NOTIFICATIONS_KEY, JSON.stringify(notifications))
}

// Загружаем уведомления
export const loadNotifications = (): Notification[] => {
  const saved = localStorage.getItem(NOTIFICATIONS_KEY)
  return saved ? JSON.parse(saved) : []
}

// Обновляем прогресс достижения
export const updateProgress = (achievementId: number, increment = 1): { updated: boolean; achievement?: Achievement } => {
  const achievements = loadAchievements()
  const achievement = achievements.find(a => a.id === achievementId)
  
  if (!achievement || achievement.unlocked) {
    return { updated: false }
  }

  if (achievement.type === 'streak') {
    achievement.progress = increment > 0 ? achievement.progress + 1 : 0
  } else {
    achievement.progress = Math.min(achievement.progress + increment, achievement.maxProgress)
  }

  let unlocked = false
  if (achievement.progress >= achievement.maxProgress && !achievement.unlocked) {
    achievement.unlocked = true
    unlocked = true
    
    // Сохраняем уведомление
    const notifications = loadNotifications()
    notifications.push({
      id: Date.now(),
      title: achievement.title,
      description: achievement.description
    })
    saveNotifications(notifications)
    
    // Проверяем достижение "Умнейший"
    checkAllAchievements(achievements)
  }

  saveAchievements(achievements)
  return { updated: true, achievement: unlocked ? achievement : undefined }
}

// Проверяем все достижения для "Умнейший"
const checkAllAchievements = (achievements: Achievement[]) => {
  const allAchievementsUnlocked = achievements
    .filter(a => a.id !== 99)
    .every(a => a.unlocked)
  
  if (allAchievementsUnlocked) {
    const smartest = achievements.find(a => a.id === 99)
    if (smartest && !smartest.unlocked) {
      smartest.unlocked = true
      smartest.progress = 1
      
      // Сохраняем уведомление
      const notifications = loadNotifications()
      notifications.push({
        id: Date.now(),
        title: smartest.title,
        description: smartest.description
      })
      saveNotifications(notifications)
      
      saveAchievements(achievements)
    }
  }
}

// Сбрасываем все достижения
export const resetAchievements = (): Achievement[] => {
  const resetAchievements = initialAchievements.map(a => ({
    ...a,
    unlocked: a.id === 1, // Только первое достижение разблокировано
    progress: a.id === 1 ? 1 : 0
  }))
  
  saveAchievements(resetAchievements)
  saveNotifications([])
  return resetAchievements
}

// Получаем статистику
export const getStats = () => {
  const achievements = loadAchievements()
  const unlockedCount = achievements.filter(a => a.unlocked).length
  const totalCount = achievements.length
  const progressPercentage = Math.round((unlockedCount / totalCount) * 100)
  
  return {
    unlockedCount,
    totalCount,
    remainingCount: totalCount - unlockedCount,
    progressPercentage
  }
}

// Получаем уведомления и очищаем старые
export const getAndClearNotifications = (): Notification[] => {
  const notifications = loadNotifications()
  saveNotifications([]) // Очищаем после получения
  return notifications
}