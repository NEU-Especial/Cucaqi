<template>
  <Survey :survey="survey" />
</template>

<script>
// 需要接收路由传参
import 'survey-core/defaultV2.min.css'
import { Survey, Model, StylesManager } from 'survey-vue'
import 'survey-vue/defaultV2.css'

StylesManager.applyTheme('defaultV2')

let surveyJson

export default {
  name: 'MyFirstSurvey',
  components: {
    Survey
  },
  data() {
    const survey = new Model(surveyJson)
    survey.onComplete.add(this.alertResults)
    return {
      survey
    }
  },
  beforeCreate() {
    const jsontext = this.$route.query['survey']
    surveyJson = JSON.parse(jsontext)
  },
  methods: {
    alertResults(sender) {
      const results = JSON.stringify(sender.data)
      alert(results)
    }
  }
}
</script>
