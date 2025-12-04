import request from '@/utils/request'

export const hotAccessoryApi = () => {
  return request.get('/customer/accessory/visitor/hot')
}

export const pageAccessoryApi = (searchForm, pageNum, pageSize) => {
  return request.get('/customer/accessory/visitor/page', {
    params: {
      ...searchForm,
      pageNum,
      pageSize,
    },
  })
}
