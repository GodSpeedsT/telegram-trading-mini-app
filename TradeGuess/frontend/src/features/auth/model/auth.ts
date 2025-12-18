import {telegramWebApp} from '@/app/main.ts'

export const authenticate = async () => {
  const tg = telegramWebApp;

  if (!tg?.initDataUnsafe) {
    throw new Error('Нет initData от Telegram');
  }

  const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      initData: tg.initDataUnsafe
    })
  });

  if (!response.ok) {
    throw new Error('Ошибка сервера авторизации');
  }

  const data = await response.json();

  if (data.success && data.token) {
    localStorage.setItem('token', data.token);
  }

  return data;
};
