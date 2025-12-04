import axios from 'axios'
import { useAuthStore } from '@/stores'
import { ElMessage } from 'element-plus'
import router from '@/router/index'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 携带token
    const authStore = useAuthStore()
    const token = authStore.authMsg.token
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  (error) => {
    ElMessage.error('请求发送失败，请检查网络连接')
    return Promise.reject(error)
  },
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    //拦截需要跳转页面的code
    if (res.code === 401) {
      const authStore = useAuthStore()
      authStore.removeAuthMsg()
      router.push('/auth/login')
    } else if (res.code === 403) {
      router.push('/common/403')
    } else if (res.code === 404) {
      router.push('/common/404')
    }
    ElMessage.error(res.msg)
    return res
  },
  (error) => {
    ElMessage.error('接口访问异常')
    return Promise.reject(error)
  },
)

export default request
