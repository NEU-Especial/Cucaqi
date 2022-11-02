import request from '@/utils/request'

// 查询租户id下的所有user
export function listUser(id) {
  return request({
    url: `/cucaqi/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: `/cucaqi/user/addUser`,
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: `/cucaqi/user/deleteUser`,
    method: 'delete',
    data
  })
}

export function updateUser(data) {
  return request({
    url: `/cucaqi/user/updateUser`,
    method: 'put',
    data
  })
}
