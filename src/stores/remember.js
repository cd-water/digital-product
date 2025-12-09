import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useRememberStore = defineStore(
  'remember',
  () => {
    const rememberMsg = ref({
      username: '',
      password: '',
    })

    const setRememberMsg = ({ username, password }) => {
      rememberMsg.value = {
        username,
        password,
      }
    }

    const removeRememberMsg = () => {
      rememberMsg.value = {
        username: '',
        password: '',
      }
    }

    return {
      rememberMsg,
      setRememberMsg,
      removeRememberMsg,
    }
  },
  {
    persist: {
      paths: ['rememberMsg'],
    },
  },
)
