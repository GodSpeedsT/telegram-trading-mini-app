<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

defineOptions({ name: 'AdminPanel' });

interface AdminUser {
  id: number;
  telegramId: number;
  username: string;
  firstName: string;
  role: string;
  totalAttempts: number;
  accuracy: number;
}

const router = useRouter();
const authStore = useAuthStore();
const users = ref<AdminUser[]>([]);
const searchQuery = ref('');
const loading = ref(false);
const statusMessage = ref('');

const BASE_URL = 'https://tradeguess-backend.onrender.com/api/admin/users';

const fetchUsers = async (query = '') => {
  loading.value = true;
  try {
    const token = authStore.getToken();
    const url = query ? `${BASE_URL}?search=${query}` : BASE_URL;
    const response = await fetch(url, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const result = await response.json();
    if (result.success) {
      users.value = result.data;
    }
  } catch (error) {
    console.error('Ошибка загрузки пользователей:', error);
  } finally {
    loading.value = false;
  }
};

const resetAttempts = async (userId: number) => {
  try {
    const token = authStore.getToken();
    const response = await fetch(`https://tradeguess-backend.onrender.com/api/admin/users/${userId}/reset-attempts?adminId=1`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const result = await response.json();
    if (result.success) {
      statusMessage.value = 'Попытки сброшены!';
      setTimeout(() => statusMessage.value = '', 3000);
      fetchUsers(searchQuery.value);
    }
  } catch (error) {
    console.error(error);
  }
};

const promoteToAdmin = async (username: string) => {
  try {
    const token = authStore.getToken();
    const response = await fetch(`https://tradeguess-backend.onrender.com/api/admin/users/promote?adminId=1`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ telegramUsername: username })
    });
    const result = await response.json();
    if (result.success) {
      statusMessage.value = 'Пользователь повышен!';
      setTimeout(() => statusMessage.value = '', 3000);
      fetchUsers(searchQuery.value);
    }
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => fetchUsers());
</script>

<template>
  <div class="h-[100dvh] w-full flex flex-col items-center justify-center p-4 relative bg-[#09090b] overflow-hidden pb-[120px] md:pb-4">

    <div class="absolute inset-0 w-full h-full pointer-events-none z-0">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="adminPuzzle" x="0" y="0" width="200" height="200" patternUnits="userSpaceOnUse">
            <path d="M-2 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M-2 100 v35 a15 15 0 0 0 0 30 v37 M98 0 v35 a15 15 0 0 0 0 30 v70 a15 15 0 0 1 0 30 v37 M198 0 v35 a15 15 0 0 1 0 30 v70 a15 15 0 0 0 0 30 v37 M0 -2 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37 M0 98 h35 a15 15 0 0 0 30 0 h70 a15 15 0 1 1 30 0 h37 M0 198 h35 a15 15 0 0 1 30 0 h70 a15 15 0 0 0 30 0 h37"
                  fill="none" stroke="white" stroke-width="1.5" opacity="0.08" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#adminPuzzle)" />
      </svg>
    </div>

    <div class="relative z-10 w-full max-w-[360px] sm:max-w-[500px] md:max-w-[700px] lg:max-w-[900px] flex flex-col items-center bg-zinc-900 border border-zinc-800 shadow-2xl overflow-hidden rounded-[32px] sm:rounded-[48px] h-fit max-h-[85vh]">

      <div class="w-full flex flex-col shrink-0 gap-4 p-6 sm:p-8 bg-zinc-950/50 border-b border-zinc-800">
        <div class="flex items-center justify-between">
          <button @click="router.back()" class="w-10 h-10 flex items-center justify-center rounded-2xl bg-zinc-800 border border-zinc-700 active:scale-90 transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 text-zinc-300">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
          </button>
          <h1 class="text-xl sm:text-2xl font-black text-white uppercase tracking-tighter">Админ-панель</h1>
          <div class="w-10"></div>
        </div>

        <div class="relative">
          <input
            v-model="searchQuery"
            @input="fetchUsers(searchQuery)"
            type="text"
            placeholder="Поиск по username..."
            class="w-full bg-zinc-900 border border-zinc-800 rounded-2xl p-4 text-sm text-white outline-none focus:border-emerald-500 transition-all"
          />
          <div v-if="loading" class="absolute right-4 top-1/2 -translate-y-1/2">
            <div class="w-4 h-4 border-2 border-zinc-700 border-t-emerald-500 rounded-full animate-spin"></div>
          </div>
        </div>
      </div>

      <div class="flex-1 w-full overflow-y-auto no-scrollbar px-4 sm:px-8 py-6 flex flex-col gap-3">
        <div v-if="statusMessage" class="bg-emerald-500 text-black text-[10px] font-black uppercase tracking-widest p-2 rounded-lg text-center animate-pulse">
          {{ statusMessage }}
        </div>

        <div v-for="user in users" :key="user.id" class="bg-zinc-800/40 border border-zinc-800 rounded-2xl p-4 flex flex-col gap-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-full bg-zinc-700 flex items-center justify-center text-white font-black text-sm">
                {{ user.username.charAt(0).toUpperCase() }}
              </div>
              <div class="flex flex-col">
                <span class="text-white font-bold text-sm">{{ user.firstName }}</span>
                <span class="text-zinc-500 text-[10px]">@{{ user.username }} | ID: {{ user.id }}</span>
              </div>
            </div>
            <div class="px-2 py-1 rounded bg-zinc-950 text-[8px] font-black text-emerald-400 border border-emerald-500/20 uppercase">
              {{ user.role }}
            </div>
          </div>

          <div class="grid grid-cols-3 gap-2">
            <div class="bg-zinc-950/50 p-2 rounded-xl border border-zinc-800 text-center">
              <div class="text-zinc-600 text-[8px] uppercase font-black">Accuracy</div>
              <div class="text-white font-mono text-xs">{{ user.accuracy }}%</div>
            </div>
            <button @click="resetAttempts(user.id)" class="bg-emerald-500/10 border border-emerald-500/30 text-emerald-500 text-[9px] font-black uppercase rounded-xl hover:bg-emerald-500 hover:text-black transition-all">
              Reset Attempts
            </button>
            <button @click="promoteToAdmin(user.username)" class="bg-purple-500/10 border border-purple-500/30 text-purple-400 text-[9px] font-black uppercase rounded-xl hover:bg-purple-500 hover:text-white transition-all">
              Make Admin
            </button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>
