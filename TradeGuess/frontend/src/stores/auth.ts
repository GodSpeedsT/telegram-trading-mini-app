import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const authError = ref('')
  const telegramWebApp = ref<any>(null)

  const getToken = () => localStorage.getItem('token') || ''
  const getUserId = () => {
    try {
      const userData = JSON.parse(localStorage.getItem('userData') || '{}')
      return userData.telegramId?.toString() || '999999'
    } catch {
      return '999999'
    }
  }

  // Actions
  const safeShowAlert = (message: string) => {
    try {
      if (telegramWebApp.value?.showPopup) {
        telegramWebApp.value.showPopup({
          message,
          buttons: [{ text: 'OK', type: 'default' }]
        })
      } else if (telegramWebApp.value?.showAlert) {
        telegramWebApp.value.showAlert(message)
      } else {
        alert(message)
      }
    } catch (e) {
      console.warn('[ALERT]', message)
    }
  }

  const collectUserData = () => {
    try {
      if (!telegramWebApp.value || !telegramWebApp.value.initDataUnsafe?.user) {
        return {
          telegramId: 999999,
          username: 'testuser',
          firstName: 'Test User',
          isReal: false
        }
      }
      const user = telegramWebApp.value.initDataUnsafe.user
      return {
        telegramId: user.id,
        username: user.username || '',
        firstName: user.first_name || '',
        isReal: true
      }
    } catch (error) {
      console.error('[AUTH] collectUserData error:', error)
      return {
        telegramId: 999999,
        username: 'testuser',
        firstName: 'Test User',
        isReal: false
      }
    }
  }

  const authenticateUser = async () => {
    try {
      authError.value = ''
      const userData = collectUserData()

      localStorage.setItem('userData', JSON.stringify(userData))

      const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          telegramId: userData.telegramId,
          username: userData.username,
          firstName: userData.firstName
        })
      })

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`)
      }

      const data = await response.json()

      if (data.success && data.data?.token) {
        localStorage.setItem('token', data.data.token)
        isAuthenticated.value = true

        setTimeout(() => {
          isAuthenticated.value = false
        }, 3000)
        return true
      } else {
        throw new Error(data.message || 'Ошибка сервера')
      }
    } catch (error: any) {
      console.error('[AUTH] Ошибка:', error)
      authError.value = error.message || 'Ошибка авторизации'
      safeShowAlert(`❌ ${error.message}`)
      return false
    }
  }

  const initWebApp = () => {
    telegramWebApp.value = (window as any).Telegram?.WebApp || null

    if (!telegramWebApp.value) {
      console.warn('[TG] WebApp не найден')
      return
    }

    const version = parseFloat(telegramWebApp.value.version || '0')
    if (version < 6.0) {
      try {
        ;(telegramWebApp.value as any).setHeaderColor?.('#18181b')
        ;(telegramWebApp.value as any).setBackgroundColor?.('#18181b')
      } catch (e) {}
    }
    telegramWebApp.value.expand()
  }

  return {
    isAuthenticated,
    authError,
    authenticateUser,
    initWebApp,
    getToken,
    getUserId,
    safeShowAlert
  }
})
