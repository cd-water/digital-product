import request from '@/utils/request'

export const joinCollectApi = (collect) => {
  return request.post('/customer/collect/join', collect)
}

export const outCollectApi = (userId, productId) => {
  return request.delete('/customer/collect/out', {
    params: { userId, productId },
  })
}

export const listCollectApi = () => {
  return request.get('/customer/collect/list')
}
