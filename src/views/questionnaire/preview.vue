<template>
  <Survey :survey="survey" />
</template>

<script>
// 需要接收路由传参
import 'survey-core/defaultV2.min.css'
import { Model, StylesManager, Survey } from 'survey-vue'
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
    let jsontext = this.$route.query['survey']
    if (jsontext === undefined) {
      jsontext = '{\n' +
        ' "title": "一个问卷案例",\n' +
        ' "description": "这是一个问卷案例",\n' +
        ' "logoPosition": "right",\n' +
        ' "pages": [\n' +
        '  {\n' +
        '   "name": "页面1",\n' +
        '   "elements": [\n' +
        '    {\n' +
        '     "type": "radiogroup",\n' +
        '     "name": "问题1",\n' +
        '     "title": "第一个问题",\n' +
        '     "choices": [\n' +
        '      {\n' +
        '       "value": "item13",\n' +
        '       "text": "答案1"\n' +
        '      },\n' +
        '      {\n' +
        '       "value": "item2",\n' +
        '       "text": "答案2"\n' +
        '      },\n' +
        '      {\n' +
        '       "value": "item3",\n' +
        '       "text": "答案3"\n' +
        '      }\n' +
        '     ]\n' +
        '    },\n' +
        '    {\n' +
        '     "type": "rating",\n' +
        '     "name": "问题2"\n' +
        '    },\n' +
        '    {\n' +
        '     "type": "checkbox",\n' +
        '     "name": "问题3",\n' +
        '     "choices": [\n' +
        '      {\n' +
        '       "value": "item1",\n' +
        '       "text": "答案4"\n' +
        '      },\n' +
        '      {\n' +
        '       "value": "item2",\n' +
        '       "text": "答案1"\n' +
        '      },\n' +
        '      {\n' +
        '       "value": "item3",\n' +
        '       "text": "答案1"\n' +
        '      }\n' +
        '     ],\n' +
        '     "showSelectAllItem": true,\n' +
        '     "selectAllText": "选择全部"\n' +
        '    }\n' +
        '   ]\n' +
        '  }\n' +
        ' ]\n' +
        '}'
      surveyJson = JSON.parse(jsontext)
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
