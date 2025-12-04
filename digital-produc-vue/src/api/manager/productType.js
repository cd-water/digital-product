import request from '@/utils/request'

export const addProductTypeApi = (productType) => {
  return request.post('/manager/productType/add', productType)
}

export const removeProductTypeApi = (id) => {
  return request.delete(`/manager/productType/remove/${id}`)
}

export const removeBatchProductTypeApi = (ids) => {
  return request.delete('/manager/productType/remove/batch', { data: ids })
}

export const editProductTypeApi = (productType) => {
  return request.put('/manager/productType/edit', productType)
}

export const queryProductTypeApi = (id) => {
  return request.get(`/manager/productType/query/${id}`)
}

export const pageProductTypeApi = (productType, pageNum, pageSize) => {
  return request.get('/manager/productType/page', {
    params: {
      ...productType,
      pageNum,
      pageSize,
    },
  })
}

export const listProductTypeApi = () => {
  return request.get('/manager/productType/list')
}
