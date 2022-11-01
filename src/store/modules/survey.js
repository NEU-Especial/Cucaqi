const state = {
  surveyNumber: 0,
  survey: null // 问卷json体
}

const mutations = {
  ClearSurvey: (state) => {
    state.surveyNumber = 0
    state.survey = null
  },
  SetSurvey: (state, surveyNumber, survey) => {
    state.surveyNumber = surveyNumber
    state.survey = survey
  }
}

export default {
  namespaced: true,
  state,
  mutations
}
