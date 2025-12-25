import request from '@/utils/request'

export const addAdminApi = (admin) => {
  return request.post('/manager/admin/add', admin)
}

export const removeAdminApi = (id) => {
  return request.delete(`/manager/admin/remove/${id}`)
}

export const removeBatchAdminApi = (ids) => {
  return request.delete('/manager/admin/remove/batch', { data: ids })
}

export const editAdminApi = (admin) => {
  return request.put('/manager/admin/edit', admin)
}

export const queryAdminApi = (id) => {
  return request.get(`/manager/admin/query/${id}`)
}

export const pageAdminApi = (admin, pageNum, pageSize) => {
  return request.get('/manager/admin/page', {
    params: {
      ...admin,
      pageNum,
      pageSize,
    },
  })
}
