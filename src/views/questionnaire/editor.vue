<template>
  <div id="surveyCreator" />
</template>

<script>
import { localization } from 'survey-creator-core'
import { SurveyCreator } from 'survey-creator-knockout'
// In modular applications:
import 'survey-core/defaultV2.min.css'
import 'survey-creator-core/survey-creator-core.min.css'

const creatorOptions = {
  showLogicTab: true,
  isAutoSave: true
}
export default {
  name: 'Editor',
  mounted() {
    // this.$store.commit('ClearSurvey')// 删除原来存放的数据
    localization.currentLocale = 'zh-cn'
    const creator = new SurveyCreator(creatorOptions)
    creator.render('surveyCreator')
  }
}
</script>

<style scoped>
#surveyCreator {
  height: 1200px;
  width: 100%;
}
</style>
