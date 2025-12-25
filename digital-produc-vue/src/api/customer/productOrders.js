import request from '@/utils/request'

export const placeProductOrdersApi = (orderMsg) => {
  return request.post('/customer/productOrders/place', orderMsg)
}

export const cancelProductOrdersApi = (orderNo) => {
  return request.post(`/customer/productOrders/cancel/${orderNo}`)
}

export const completedProductOrdersApi = (orderNo) => {
  return request.post(`/customer/productOrders/completed/${orderNo}`)
}

export const paymentProductOrdersApi = (orderNo) => {
  return request.post(`/customer/productOrders/payment/${orderNo}`)
}

export const listProductOrdersApi = () => {
  return request.get('/customer/productOrders/list')
}
