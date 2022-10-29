<template>
  <div id="surveyCreator"/>
</template>

<script>
import { SurveyCreator } from 'survey-creator-knockout'
// In modular applications:
import { localization } from 'survey-creator-core'

const creatorOptions = {
  showLogicTab: true,
  isAutoSave: true
}
export default {
  name: 'SurveyCreator',
  mounted() {
    localization.currentLocale = 'zh-cn'
    const creator = new SurveyCreator(creatorOptions)
    creator.render('surveyCreator')
  }
}
</script>

<style scoped>
#surveyCreator {
  height: 100%;
  width: 100%;
}
</style>

