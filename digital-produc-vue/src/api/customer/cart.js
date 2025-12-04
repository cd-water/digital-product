import request from '@/utils/request'

export const listCartApi = () => {
  return request.get('/customer/cart/list')
}

export const joinCartApi = (cart) => {
  return request.post('/customer/cart/join', cart)
}

export const outCartApi = (userId, accessoryId) => {
  return request.delete('/customer/cart/out', {
    params: { userId, accessoryId },
  })
}
