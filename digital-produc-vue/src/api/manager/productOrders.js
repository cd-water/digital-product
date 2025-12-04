import request from '@/utils/request'

export const pageProductOrdersApi = (pageParam, pageNum, pageSize) => {
  return request.get('/manager/productOrders/page', {
    params: {
      ...pageParam,
      pageNum,
      pageSize,
    },
  })
}

export const acceptProductOrdersApi = (orderNo) => {
  return request.post(`/manager/productOrders/accept/${orderNo}`)
}

export const deliveryProductOrdersApi = (orderNo) => {
  return request.post(`/manager/productOrders/delivery/${orderNo}`)
}

export const cancelProductOrdersApi = (orderNo) => {
  return request.post(`/manager/productOrders/cancel/${orderNo}`)
}

export const refundProductOrdersApi = (orderNo) => {
  return request.post(`/manager/productOrders/refund/${orderNo}`)
}

export const getProductOrderNumApi = (shopId) => {
  return request.get(`/manager/productOrders/count/${shopId}`)
}
