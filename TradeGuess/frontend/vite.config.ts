import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],

  // ВАЖНО: Оставьте только ОДНУ директиву base
  base: '/', // Для продакшена на Render с SPA routing

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

  build: {
    outDir: 'dist',
    sourcemap: false,
    // Оптимизация для мини-приложений
    assetsInlineLimit: 4096, // Файлы меньше 4KB встраиваются в JS
    cssCodeSplit: true,

    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['vue', 'vue-router', 'pinia'],
          charts: ['echarts']
        },
        // Важно для правильных путей
        assetFileNames: (assetInfo) => {
          if (assetInfo.name && assetInfo.name.endsWith('.css')) {
            return 'assets/css/[name]-[hash][extname]'
          }
          return 'assets/[name]-[hash][extname]'
        },
        chunkFileNames: 'assets/js/[name]-[hash].js',
        entryFileNames: 'assets/js/[name]-[hash].js'
      }
    }
  },

  // Настройки сервера для разработки
  server: {
    host: true,
    port: 3000,
    // Прокси для разработки
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },

  // Предпросмотр
  preview: {
    host: true,
    port: 3000
  }
})
