import request from '@/utils/request'

export const addUserApi = (user) => {
  return request.post('/manager/user/add', user)
}

export const removeUserApi = (id) => {
  return request.delete(`/manager/user/remove/${id}`)
}

export const removeBatchUserApi = (ids) => {
  return request.delete('/manager/user/remove/batch', { data: ids })
}

export const editUserApi = (user) => {
  return request.put('/manager/user/edit', user)
}

export const queryUserApi = (id) => {
  return request.get(`/manager/user/query/${id}`)
}

export const pageUserApi = (user, pageNum, pageSize) => {
  return request.get('/manager/user/page', {
    params: {
      ...user,
      pageNum,
      pageSize,
    },
  })
}
