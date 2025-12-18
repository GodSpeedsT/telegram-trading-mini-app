import { telegramWebApp } from '@/app/main.ts'; // Из вашего main.ts

interface AuthData {
  telegramId: number;
  username?: string;
  firstName?: string;
}

export const authenticate = async () => {
  const tg = telegramWebApp;
  const user = tg?.initDataUnsafe?.user;

  if (!user) {
    throw new Error('Пользователь не найден в Telegram');
  }

  const authData: AuthData = {
    telegramId: user.id,           // 93435467
    username: user.username || '', // "GodSpeed"
    firstName: user.first_name || '' // "pupupu"
  };

  const response = await fetch('https://tradeguess-backend.onrender.com/api/auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(authData)
  });

  if (!response.ok) {
    throw new Error('Ошибка сервера авторизации');
  }

  const data = await response.json();

  // Сохраняем токен
  if (data.success && data.data?.token) {
    localStorage.setItem('token', data.data.token);
  }

  return data;
};
