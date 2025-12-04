import request from '@/utils/request'

export const editUserApi = (user) => {
  return request.put('/customer/user/edit', user)
}

export const queryUserApi = (id) => {
  return request.get(`/customer/user/query/${id}`)
}

export const topupApi = (topupBody) => {
  return request.post('/customer/user/topup', topupBody)
}
