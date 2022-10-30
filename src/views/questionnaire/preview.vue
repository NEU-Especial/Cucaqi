<template>
  <Survey :survey="survey" />
</template>

<script>
import 'survey-core/defaultV2.min.css'
import { Survey, Model, StylesManager } from 'survey-vue'
import 'survey-vue/defaultV2.css'

StylesManager.applyTheme('defaultV2')

const surveyJson = {
  elements: [{
    name: 'FirstName',
    title: 'Enter your first name:',
    type: 'text'
  }, {
    name: 'LastName',
    title: 'Enter your last name:',
    type: 'text'
  }]
}

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
  methods: {
    alertResults(sender) {
      const results = JSON.stringify(sender.data)
      alert(results)
    }
  }
}
</script>
