import request from '@/utils/request'

export const addProductApi = (product) => {
  return request.post('/manager/product/add', product)
}

export const removeProductApi = (id) => {
  return request.delete(`/manager/product/remove/${id}`)
}

export const removeBatchProductApi = (ids) => {
  return request.delete('/manager/product/remove/batch', { data: ids })
}

export const editProductApi = (product) => {
  return request.put('/manager/product/edit', product)
}

export const queryProductApi = (id) => {
  return request.get(`/manager/product/query/${id}`)
}

export const pageProductApi = (pageParam, pageNum, pageSize) => {
  return request.get('/manager/product/page', {
    params: {
      ...pageParam,
      pageNum,
      pageSize,
    },
  })
}

export const groupProductApi = () => {
  return request.get('/manager/product/group')
}
