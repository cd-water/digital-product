import request from '@/utils/request'

export const hotDigitalShopApi = () => {
  return request.get('/customer/digitalShop/visitor/hot')
}

export const pageDigitalShopApi = (searchForm, pageNum, pageSize) => {
  return request.get('/customer/digitalShop/visitor/page', {
    params: {
      ...searchForm,
      pageNum,
      pageSize,
    },
  })
}

export const detailDigitalShopApi = (id) => {
  return request.get(`/customer/digitalShop/visitor/detail/${id}`)
}
