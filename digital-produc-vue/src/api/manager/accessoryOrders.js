import request from '@/utils/request'

export const pageAccessoryOrdersApi = (pageParam, pageNum, pageSize) => {
  return request.get('/manager/accessoryOrders/page', {
    params: {
      ...pageParam,
      pageNum,
      pageSize,
    },
  })
}

export const acceptAccessoryOrdersApi = (orderNo) => {
  return request.post(`/manager/accessoryOrders/accept/${orderNo}`)
}

export const deliveryAccessoryOrdersApi = (orderNo) => {
  return request.post(`/manager/accessoryOrders/delivery/${orderNo}`)
}

export const cancelAccessoryOrdersApi = (orderNo) => {
  return request.post(`/manager/accessoryOrders/cancel/${orderNo}`)
}

export const refundAccessoryOrdersApi = (orderNo) => {
  return request.post(`/manager/accessoryOrders/refund/${orderNo}`)
}

export const getAccessoryOrderNumApi = (shopId) => {
  return request.get(`/manager/accessoryOrders/count/${shopId}`)
}
