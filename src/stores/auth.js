import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const authMsg = ref({
      token: '',
      id: '',
      role: '',
      nickname: '',
      avatar: '',
    })

    const setAuthMsg = ({ token, id, role, nickname, avatar }) => {
      authMsg.value = {
        token,
        id,
        role,
        nickname,
        avatar,
      }
    }

    const removeAuthMsg = () => {
      authMsg.value = {
        token: '',
        id: '',
        role: '',
        nickname: '',
        avatar: '',
      }
    }

    return {
      authMsg,
      setAuthMsg,
      removeAuthMsg,
    }
  },
  {
    persist: {
      paths: ['authMsg'],
    },
  },
)
