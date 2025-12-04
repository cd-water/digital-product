import request from '@/utils/request'

export const addDigitalShopApi = (digitalShop) => {
  return request.post('/manager/digitalShop/add', digitalShop)
}

export const removeDigitalShopApi = (id) => {
  return request.delete(`/manager/digitalShop/remove/${id}`)
}

export const removeBatchDigitalShopApi = (ids) => {
  return request.delete('/manager/digitalShop/remove/batch', { data: ids })
}

export const editDigitalShopApi = (digitalShop) => {
  return request.put('/manager/digitalShop/edit', digitalShop)
}

export const queryDigitalShopApi = (id) => {
  return request.get(`/manager/digitalShop/query/${id}`)
}

export const pageDigitalShopApi = (digitalShop, pageNum, pageSize) => {
  return request.get('/manager/digitalShop/page', {
    params: {
      ...digitalShop,
      pageNum,
      pageSize,
    },
  })
}
