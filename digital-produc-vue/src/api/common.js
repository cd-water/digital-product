import request from '@/utils/request'

//文件上传接口
export const uploadApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload', formData)
}

//管理员前台数据相关接口
export const adminCountApi = () => {
  return request.get('/admin/count')
}
export const productPieApi = () => {
  return request.get('/admin/productPie')
}
export const productOrdersBarApi = () => {
  return request.get('/admin/productOrdersBar')
}
export const accessoryPieApi = () => {
  return request.get('/admin/accessoryPie')
}
export const accessoryOrdersBarApi = () => {
  return request.get('/admin/accessoryOrdersBar')
}

//店铺前台数据相关接口
export const shopTodayApi = (id) => {
  return request.get(`/shop/today/${id}`)
}
export const amountLineApi = (id, begin, end) => {
  return request.get(`/shop/amountLine/${id}`, {
    params: { begin, end },
  })
}
export const numberLineApi = (id, begin, end) => {
  return request.get(`/shop/numberLine/${id}`, {
    params: { begin, end },
  })
}
