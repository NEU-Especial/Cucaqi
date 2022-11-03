import request from '@/utils/request'

export function getAllAnswererByUserId(id) {
  return request({
    url: `/cucaqi/answerer/${id}`,
    method: 'get'
  })
}

export function getAllAnswererByGroupId(id) {
  return request({
    url: `/cucaqi/answerer/details/${id}`,
    method: 'get'
  })
}

export function save(data) {
  return request({
    url: `/cucaqi/answerer`,
    method: 'post',
    data
  })
}

export function Delete(id) {
  return request({
    url: `/cucaqi/answerer/${id}`,
    method: 'delete'
  })
}

export function update(data) {
  return request({
    url: `/cucaqi/answerer`,
    method: 'put',
    data
  })
}

