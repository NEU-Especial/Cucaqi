import request from '@/utils/request'

export function updatePassword(data) {
  return request({
    url: '/cucaqi/info/updatePassword',
    method: 'post',
    data: data
  })
}

export function askAuthCodeByEmail(email, id) {
  return request({
    url: `/cucaqi/info/email/${email}/${id}`,
    method: 'get'
  })
}

export function bindEmail(code, data) {
  return request({
    url: `/cucaqi/info/email/${code}`,
    method: 'post',
    data
  })
}
