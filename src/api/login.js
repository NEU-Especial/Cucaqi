import request from '@/utils/request'

export function loginByPassword(data) {
  return request({
    url: '/cucaqi/login/password',
    method: 'post',
    data: data
  })
}

export function getInfo(token) {
  return request({
    url: '/cucaqi/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/cucaqi/logout',
    method: 'post'
  })
}

export function getAuthCodeByTelephone(telephone, role) {
  return request({
    url: `/cucaqi/login/authCode/${role}`,
    method: 'get',
    params: {
      telephone: telephone
    }
  })
}

export function LoginByTelephone(code, data) {
  return request({
    url: `/cucaqi/login/telephone/${code}`,
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: `/cucaqi/login/register`,
    method: 'post',
    data
  })
}

export function getAllSecurityQuestion() {
  return request({
    url: `/cucaqi/login/securityQuestions`,
    method: 'get'
  })
}

export function findbackByQues(data) {
  return request({
    url: `/cucaqi/login/findbackByQues`,
    method: 'post',
    data
  })
}
