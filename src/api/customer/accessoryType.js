import request from '@/utils/request'

export const allAccessoryTypeApi = () => {
  return request.get('/customer/accessoryType/visitor/all')
}

export const hotAccessoryTypeApi = () => {
  return request.get('/customer/accessoryType/visitor/hot')
}
