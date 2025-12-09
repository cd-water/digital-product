import request from '@/utils/request'

export const recommendProductApi = () => {
  return request.get('/customer/product/visitor/recommend')
}

export const pageProductApi = (searchForm, pageNum, pageSize) => {
  return request.get('/customer/product/visitor/page', {
    params: {
      ...searchForm,
      pageNum,
      pageSize,
    },
  })
}

export const detailProductApi = (id, userId, role) => {
  return request.get(`/customer/product/visitor/detail/${id}`, {
    params: { userId, role },
  })
}
