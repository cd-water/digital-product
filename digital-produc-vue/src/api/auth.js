import request from '@/utils/request'

export const loginApi = (loginRequest) => {
  return request.post('/login', loginRequest)
}

export const phoneLoginApi = (phoneLoginRequest) => {
  return request.post('/phoneLogin', phoneLoginRequest)
}

export const registerApi = (registerRequest) => {
  return request.post('/register', registerRequest)
}

export const forgetApi = (forgetRequest) => {
  return request.post('/forget', forgetRequest)
}

export const getCodeApi = (phone) => {
  return request.post('/code', { phone })
}

export const changeApi = (changeRequest) => {
  return request.post('/change', changeRequest)
}

export const logoutApi = () => {
  return request.post('/logout')
}
