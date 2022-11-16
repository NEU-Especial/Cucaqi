import request from '@/utils/request'

// 根据ID查找问卷
export function getSurveyById(id) {
  return request({
    url: `/cucaqi/survey/${id}`,
    method: 'get'
  })
}

// 查找所有问卷
export function findAllSurvey(params) {
  return request({
    url: `/cucaqi/survey/findAllSurvey`,
    method: 'get',
    params: params
  })
}

// 根据CreatedBy查找问卷
export function findDeletedSurvey(params) {
  return request({
    url: `/cucaqi/survey/findDeletedSurvey`,
    method: 'get',
    params: params
  })
}

// 新增问卷
export function addSurvey(data) {
  return request({
    url: `/cucaqi/survey/addSurvey`,
    method: 'post',
    data
  })
}

// 删除问卷
export function deleteSurvey(data) {
  return request({
    url: `/cucaqi/survey/deleteSurvey`,
    method: 'delete',
    data
  })
}

// 软删除问卷
export function softDeleteSurvey(data) {
  return request({
    url: `/cucaqi/survey/softDeleteSurvey`,
    method: 'delete',
    params:data
  })
}

// 修改问卷
export function updateSurvey(data) {
  return request({
    url: `/cucaqi/survey/updateSurvey`,
    method: 'put',
    data
  })
}


export function PostToPublic(params) {
  return request({
    url: `/cucaqi/survey/postToPublic`,
    method: 'post',
    params: params
  })
}

// 修改问卷
export function PostToGroup(params) {
  return request({
    url: `/cucaqi/survey/postToGroup`,
    method: 'post',
    params: params
  })
}


// 更新问卷状态
export function updateSurveyState(params) {
  return request({
    url: `/cucaqi/survey/updateSurveyState`,
    method: 'put',
    params: params
  })
}
