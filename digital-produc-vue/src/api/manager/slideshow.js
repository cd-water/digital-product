import request from '@/utils/request'

export const addSlideshowApi = (slideshow) => {
  return request.post('/manager/slideshow/add', slideshow)
}

export const removeSlideshowApi = (id) => {
  return request.delete(`/manager/slideshow/remove/${id}`)
}

export const removeBatchSlideshowApi = (ids) => {
  return request.delete('/manager/slideshow/remove/batch', { data: ids })
}

export const editSlideshowApi = (slideshow) => {
  return request.put('/manager/slideshow/edit', slideshow)
}

export const querySlideshowApi = (id) => {
  return request.get(`/manager/slideshow/query/${id}`)
}

export const pageSlideshowApi = (slideshow, pageNum, pageSize) => {
  return request.get('/manager/slideshow/page', {
    params: {
      ...slideshow,
      pageNum,
      pageSize,
    },
  })
}
