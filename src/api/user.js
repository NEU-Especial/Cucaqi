import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/cucaqi/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/cucaqi/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/cucaqi/user/logout',
    method: 'post'
  })
}
