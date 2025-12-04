import request from '@/utils/request'

export const addAccessoryApi = (accessory) => {
  return request.post('/manager/accessory/add', accessory)
}

export const removeAccessoryApi = (id) => {
  return request.delete(`/manager/accessory/remove/${id}`)
}

export const removeBatchAccessoryApi = (ids) => {
  return request.delete('/manager/accessory/remove/batch', { data: ids })
}

export const editAccessoryApi = (accessory) => {
  return request.put('/manager/accessory/edit', accessory)
}

export const queryAccessoryApi = (id) => {
  return request.get(`/manager/accessory/query/${id}`)
}

export const pageAccessoryApi = (pageParam, pageNum, pageSize) => {
  return request.get('/manager/accessory/page', {
    params: {
      ...pageParam,
      pageNum,
      pageSize,
    },
  })
}
