import request from '@/utils/request'
import axios from "axios";


export function getGroupPage(userId) {
  return request({
    url: `/cucaqi/group/${userId}`,
    method: 'get',
  })
}
export function getGroupAnswererPage(groupId) {
  return request({
    url: `/cucaqi/answerer/details/${groupId}`,
    method: 'get',
  })
}

export function addGroup(data) {
  return request({
    url: '/cucaqi/group',
    method: 'post',
    data
  })
}
export function updateGroup(data) {
  return request({
    url: '/cucaqi/group',
    method: 'put',
    data
  })
}

export function deleteGroup(data) {
  return request({
    url: `/cucaqi/group/deleteGroup`,
    method: 'delete',
    data
  })
}

