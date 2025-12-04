import request from '@/utils/request'

export const latestNoticeApi = () => {
  return request.get('/customer/notice/visitor/latest')
}
