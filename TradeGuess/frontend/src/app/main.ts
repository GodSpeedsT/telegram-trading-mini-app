import { createApp } from 'vue'
import { createPinia } from 'pinia'
import echartsPlugin from '../features/echarts'
import App from './App.vue'
import router from './router'
import "../style.css"

// Типизация для Telegram WebApp
declare global {
  interface Window {
    Telegram: {
      WebApp: {
        expand: () => void
        setHeaderColor: (color: string) => void
        setBackgroundColor: (color: string) => void
        BackButton: {
          show: () => void
          hide: () => void
          onClick: (callback: () => void) => void
        }
        themeParams: {
          bg_color: string
          text_color: string
          hint_color: string
          link_color: string
          button_color: string
          button_text_color: string
        }
        initDataUnsafe: {
          user?: {
            id: number
            first_name: string
            last_name?: string
            username?: string
            language_code?: string
            is_premium?: boolean
          }
          chat?: any
        }
        platform: string
        version: string
        close: () => void
        sendData: (data: string) => void
      }
    }
  }
}

const app = createApp(App)

// Настройка Pinia store для Telegram
const pinia = createPinia()

// Плагин для доступа к Telegram WebApp в компонентах
app.use(pinia)
app.use(router)
app.use(echartsPlugin)

// Глобальное свойство для доступа к Telegram WebApp
app.config.globalProperties.$tg = window.Telegram?.WebApp || null

app.mount('#app')

// Экспортируем для использования в компонентах
export const telegramWebApp = window.Telegram?.WebApp || null
