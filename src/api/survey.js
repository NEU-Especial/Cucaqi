import request from "@/utils/request";

//根据ID查找问卷
export function getSurveyById(id) {
  return request({
    url: `/cucaqi/survey/${id}`,
    method: "get"
  });
}

//查找所有问卷
export function findAllSurvey(params) {
  return request({
    url: `/cucaqi/survey/findAllSurvey`,
    method: 'get',
    params: params
  })
}

//根据CreatedBy查找问卷
export function findSurveyByCreatedBy(params) {
  return request({
    url: `/cucaqi/survey/findSurveyByCreatedBy`,
    method: 'get',
    params: params
  })
}

//新增问卷
export function addSurvey(data) {
  return request({
    url: `/cucaqi/survey/addSurvey`,
    method: 'post',
    data
  })
}

//删除问卷
export function deleteSurvey(data) {
  return request({
    url: `/cucaqi/survey/deleteSurvey`,
    method: 'delete',
    data
  })
}

//软删除问卷
export function softDeleteSurvey(data) {
  return request({
    url: `/cucaqi/survey/softDeleteSurvey`,
    method: 'delete',
    data
  })
}

//修改问卷
export function updateSurvey(data) {
  return request({
    url: `/cucaqi/survey/updateSurvey`,
    method: 'put',
    data
  })
}
