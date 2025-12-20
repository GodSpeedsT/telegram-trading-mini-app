import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useGameStore = defineStore('game', () => {
  const score = ref(0)
  const streak = ref(0)

  const addScore = (points: number) => {
    score.value += points
    saveGameData()
  }

  const incrementStreak = () => {
    streak.value++
    saveGameData()
  }

  const resetStreak = () => {
    streak.value = 0
    saveGameData()
  }

  const saveGameData = () => {
    localStorage.setItem('gameData', JSON.stringify({ score: score.value, streak: streak.value }))
  }

  const loadGameData = () => {
    try {
      const saved = localStorage.getItem('gameData')
      if (saved) {
        const data = JSON.parse(saved)
        score.value = data.score || 0
        streak.value = data.streak || 0
      }
    } catch (e) {
      console.error('Game data load error:', e)
    }
  }

  return { score, streak, addScore, incrementStreak, resetStreak, saveGameData, loadGameData }
})
