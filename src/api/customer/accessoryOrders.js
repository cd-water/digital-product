import request from '@/utils/request'

export const placeAccessoryOrdersApi = (orderMsg) => {
  return request.post('/customer/accessoryOrders/place', orderMsg)
}

export const cancelAccessoryOrdersApi = (orderNo) => {
  return request.post(`/customer/accessoryOrders/cancel/${orderNo}`)
}

export const completedAccessoryOrdersApi = (orderNo) => {
  return request.post(`/customer/accessoryOrders/completed/${orderNo}`)
}

export const paymentAccessoryOrdersApi = (orderNoArray) => {
  return request.post('/customer/accessoryOrders/payment', orderNoArray)
}

export const listAccessoryOrdersApi = () => {
  return request.get('/customer/accessoryOrders/list')
}
