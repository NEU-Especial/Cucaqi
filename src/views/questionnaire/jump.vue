<template>
  <div id="surveyVizPanel" />
</template>

<script>
import 'survey-analytics/survey.analytics.min.css'
import { Model } from 'survey-core'
import { VisualizationPanel } from 'survey-analytics'
const surveyJson = {
  elements: [
    {
      name: 'satisfaction-score',
      title: '问题1：你平常使用什么工具开发Java软件',
      type: 'radiogroup',
      choices: [
        { value: 5, text: 'IDEA' },
        { value: 3, text: 'ECLIPSE' },
        { value: 1, text: 'VSCODE' }
      ],
      isRequired: true
    },
    {
      name: 'nps-score',
      title:
          '问题2：你对灵动岛的喜爱程度打分?',
      type: 'rating',
      rateMin: 0,
      rateMax: 10
    }
  ],
}

const surveyResults = [
  {
    'satisfaction-score': 5,
    'nps-score': 10
  },
  {
    'satisfaction-score': 5,
    'nps-score': 9
  },
  {
    'satisfaction-score': 3,
    'nps-score': 6
  },
  {
    'satisfaction-score': 3,
    'nps-score': 6
  },
  {
    'satisfaction-score': 1,
    'nps-score': 6
  },
  {
    'satisfaction-score': 3,
    'nps-score': 8
  }
]

const vizPanelOptions = {
  allowHideQuestions: false
}

export default {
  name: 'SurveyAnalytics',
  mounted() {
    const survey = new Model(surveyJson)
    const vizPanel = new VisualizationPanel(
      survey.getAllQuestions(),
      surveyResults,
      vizPanelOptions
    )
    vizPanel.showHeader = false
    vizPanel.render(document.getElementById('surveyVizPanel'))
  }
}
</script>
