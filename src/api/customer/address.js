import request from '@/utils/request'

export const listAddressApi = () => {
  return request.get('/customer/address/list')
}

export const addAddressApi = (address) => {
  return request.post('/customer/address/add', address)
}

export const removeAddressApi = (id) => {
  return request.delete(`/customer/address/remove/${id}`)
}

export const editAddressApi = (address) => {
  return request.put('/customer/address/edit', address)
}

export const queryAddressApi = (id) => {
  return request.get(`/customer/address/query/${id}`)
}
