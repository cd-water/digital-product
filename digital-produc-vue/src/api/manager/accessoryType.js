import request from '@/utils/request'

export const addAccessoryTypeApi = (accessoryType) => {
  return request.post('/manager/accessoryType/add', accessoryType)
}

export const removeAccessoryTypeApi = (id) => {
  return request.delete(`/manager/accessoryType/remove/${id}`)
}

export const removeBatchAccessoryTypeApi = (ids) => {
  return request.delete('/manager/accessoryType/remove/batch', { data: ids })
}

export const editAccessoryTypeApi = (accessoryType) => {
  return request.put('/manager/accessoryType/edit', accessoryType)
}

export const queryAccessoryTypeApi = (id) => {
  return request.get(`/manager/accessoryType/query/${id}`)
}

export const pageAccessoryTypeApi = (accessoryType, pageNum, pageSize) => {
  return request.get('/manager/accessoryType/page', {
    params: {
      ...accessoryType,
      pageNum,
      pageSize,
    },
  })
}

export const listAccessoryTypeApi = () => {
  return request.get('/manager/accessoryType/list')
}
