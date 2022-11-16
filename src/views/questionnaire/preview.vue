<template>
  <Survey :survey="survey"/>
</template>

<script>
// 需要接收路由传参
import 'survey-core/defaultV2.min.css'
import { Model, StylesManager, Survey } from 'survey-vue'
import 'survey-vue/defaultV2.css'
import { getSurveyById, saveAnswerResult } from '@/api/survey'
import { Message } from 'element-ui'

StylesManager.applyTheme('defaultV2')

let surveyJson

export default {
  name: 'MyFirstSurvey',
  components: {
    Survey
  },
  data() {
    const survey = new Model(surveyJson)
    survey.onComplete.add(this.saveResult)
    return {
      mySurvey: {},
      answererId: undefined,
      survey
    }
  },
  beforeCreate() {
    const jsontext = this.$route.query['survey']
    if (jsontext !== undefined) {
      surveyJson = JSON.parse(jsontext)
      return
    }
    const surveyId = this.$route.query['surveyId']
    const answererId = this.$route.query['answererId']
    getSurveyById(surveyId).then((res) => {
      this.mySurvey = res.data
      this.answererId = answererId
      if (this.answererId === undefined) {
        this.answererId = -1
      }
      this.survey.fromJSON(JSON.parse(res.data.content))
    })
  },
  methods: {
    saveResult(sender) {
      // 判断当前问卷的状态，由后端判断
      const results = JSON.stringify(sender.data)
      saveAnswerResult(this.mySurvey.id, this.answererId, results).then((res) => {
        Message({
          message: res.msg,
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
