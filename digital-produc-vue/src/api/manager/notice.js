import request from '@/utils/request'

export const addNoticeApi = (notice) => {
  return request.post('/manager/notice/add', notice)
}

export const removeNoticeApi = (id) => {
  return request.delete(`/manager/notice/remove/${id}`)
}

export const removeBatchNoticeApi = (ids) => {
  return request.delete('/manager/notice/remove/batch', { data: ids })
}

export const editNoticeApi = (notice) => {
  return request.put('/manager/notice/edit', notice)
}

export const queryNoticeApi = (id) => {
  return request.get(`/manager/notice/query/${id}`)
}

export const pageNoticeApi = (notice, pageNum, pageSize) => {
  return request.get('/manager/notice/page', {
    params: {
      ...notice,
      pageNum,
      pageSize,
    },
  })
}
