import request from '@/utils/request'

export function getLesseeList() {
  return request({
    url: '/cucaqi/lessee',
    method: 'get'
  })
}

export function getDeletedLesseeList() {
  return request({
    url: '/cucaqi/lessee/deleted',
    method: 'get'
  })
}

export function addLessee(data) {
  return request({
    url: '/cucaqi/lessee/addLessee',
    method: 'post',
    data
  })
}

export function updateLessee(data) {
  return request({
    url: '/cucaqi/lessee/updateLessee',
    method: 'put',
    data
  })
}

export function deleteLessee(data) {
  return request({
    url: '/cucaqi/lessee/deleteLessee',
    method: 'delete',
    data
  })
}
