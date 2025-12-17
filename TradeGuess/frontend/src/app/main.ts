import { createApp } from 'vue'
import { createPinia } from 'pinia'
import echartsPlugin from '../features/echarts'
import App from './App.vue'
import router from './router'
import "../style.css"

declare global {
  interface Window {
    Telegram: {
      WebApp: any
    }
  }
}

const app = createApp(App)

if (window.Telegram?.WebApp) {
  const tg = window.Telegram.WebApp
  tg.expand()
  tg.setHeaderColor('#2b6cb0')
  tg.setBackgroundColor('#f7fafc')
}

app.use(createPinia())
app.use(router)
app.use(echartsPlugin)

app.mount('#app')
