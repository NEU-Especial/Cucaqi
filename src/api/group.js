import request from '@/utils/request'
import axios from 'axios'

export function getGroupPage(userId) {
  return request({
    url: `/cucaqi/group/${userId}`,
    method: 'get'
  })
}

export function getDeletedGroupPage(userId) {
  return request({
    url: `/cucaqi/group/deleted/${userId}`,
    method: 'get'
  })
}

export function getAllGroupByAnswererId(answererId) {
  return request({
    url: `/cucaqi/group/belongs/${answererId}`,
    method: 'get'
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
export function updateGroupDeletedStatus(groupId) {
  return request({
    url: `/cucaqi/group/recover/${groupId}`,
    method: 'put'
  })
}

export function deleteGroup(data) {
  return request({
    url: `/cucaqi/group/deleteGroup`,
    method: 'delete',
    data
  })
}

export function deleteFromGroupAnswererRelation(groupId, answererId) {
  return request({
    url: `/cucaqi/group/deleteRelation/${groupId}/${answererId}`,
    method: 'delete'
  })
}

export function addToGroupAnswererRelation(groupId, answererId) {
  return request({
    url: `/cucaqi/group/addRelation/${groupId}/${answererId}`,
    method: 'post'
  })
}

