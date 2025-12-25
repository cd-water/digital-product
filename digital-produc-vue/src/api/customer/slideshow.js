import request from '@/utils/request'

export const listSlideshowApi = () => {
  return request.get('/customer/slideshow/visitor/list')
}
