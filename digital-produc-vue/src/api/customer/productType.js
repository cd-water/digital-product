import request from '@/utils/request'

export const allProductTypeApi = () => {
  return request.get('/customer/productType/visitor/all')
}

export const hotProductTypeApi = () => {
  return request.get('/customer/productType/visitor/hot')
}
