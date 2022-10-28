import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/cucaqi/table/list',
    method: 'get',
    params
  })
}
