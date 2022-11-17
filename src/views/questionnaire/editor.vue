<!--<template>-->
<!--  <div id="surveyCreator" />-->
<!--</template>-->

<!--<script>-->
<!--import { localization } from 'survey-creator-core'-->
<!--import { SurveyCreator } from 'survey-creator-knockout'-->
<!--// In modular applications:-->
<!--import 'survey-core/defaultV2.min.css'-->
<!--import 'survey-creator-core/survey-creator-core.min.css'-->

<!--const creatorOptions = {-->
<!--  showLogicTab: true,-->
<!--  isAutoSave: true-->
<!--}-->
<!--export default {-->
<!--  name: 'Editor',-->
<!--  mounted() {-->
<!--    // this.$store.commit('ClearSurvey')// 删除原来存放的数据-->
<!--    localization.currentLocale = 'zh-cn'-->
<!--    const creator = new SurveyCreator(creatorOptions)-->
<!--    creator.render('surveyCreator')-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--#surveyCreator {-->
<!--  height: 1200px;-->
<!--  width: 100%;-->
<!--}-->
<!--</style>-->
<template>
  <!--  创建问卷表单-->
  <div>
    <transition name="fade">
      <el-container v-show="!isEditing" style="height: 1200px;margin-top: 5%">
        <el-aside width="40%" style="height:800px;margin: 5% 30px 30px;">
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="问卷标题" label-width="120px">
              <el-input v-model="form.title" style="width: 400px;margin-left: 1px"></el-input>
            </el-form-item>
            <el-form-item label="时间范围" label-width="120px">
              <el-date-picker
                v-model="timeRange"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              />
            </el-form-item>

            <el-form-item label="公开问卷" label-width="120px">
              <el-switch v-model="form.isPublic" />
            </el-form-item>
            <el-form-item label="限制回答人数" label-width="120px">
              <el-col :span="4">
                <el-switch v-model="form.limitCount!==0" style="right: 0px" />
              </el-col>

              <el-col :span="20">
                <el-input-number
                  v-model="form.limitCount"
                  :min="0"
                  label="回答人数"
                  style="float: right"
                />
              </el-col>
            </el-form-item>
            <el-form-item label="问卷编辑" label-width="120px">
              <el-button type="primary" style="margin-right: 20px" @click="startEdit">编辑问卷</el-button>
              <el-button type="primary" plain @click="preview">模拟答题</el-button>
            </el-form-item>

            <el-form-item style="margin-top: 50px">
              <el-button type="primary" style="width: 400px;margin-left: 30px" @click="onSubmit">立即修改</el-button>
            </el-form-item>
          </el-form>
        </el-aside>
        <!--    这里放预览表单-->
        <el-main>
          <div v-if="saveNo===-1">
            <el-empty description="暂无问卷数据">
              <el-button type="primary" @click="startEdit">编辑问卷</el-button>
            </el-empty>
          </div>
          <div v-else>
            <Survey :survey="surveyForm" />
          </div>
        </el-main>
      </el-container>
    </transition>

    <transition name="fade">
      <div v-show="isEditing" style="width: 100%;height:1200px">
        <el-button type="primary" style="margin: 5px" icon="el-icon-arrow-left" @click="isEditing=false">返回创建
        </el-button>
        <br>
        <div id="surveyCreator" />
      </div>
    </transition>
  </div>
</template>

<script>
import 'survey-core/defaultV2.min.css'
import 'survey-creator-core/survey-creator-core.min.css'
import { localization } from 'survey-creator-core'
import { SurveyCreator } from 'survey-creator-knockout'
import { Model, StylesManager, Survey } from 'survey-vue'
import { Message } from 'element-ui'
import {addSurvey, findAllSurvey, updateSurvey} from '@/api/survey'

StylesManager.applyTheme('defaultV2')
const creatorOptions = {
  showLogicTab: true,
  isAutoSave: true
}
export default {
  name: 'SurveyCreator',
  components: {
    Survey
  },
  data: function() {
    return {
      isEditing: false, // 是否正在编辑
      survey: '', // 存放当前调查问卷对象
      saveNo: -1,
      timeRange: [],
      isLimit: false,
      form: {
        content: '',
        createdBy: '',
        startTime: '',
        endTime: '',
        limitCount: 0,
        isPublic: '',
        state: 0,
        title: ''
      }
    }
  },
  computed: {
    surveyForm: function() {
      return new Model(this.survey)
    }
  },
  mounted() {
    // this.$store.commit('ClearSurvey')// 删除原来存放的数据
    localization.currentLocale = 'zh-cn'
    const creator = new SurveyCreator(creatorOptions)
    creator.saveSurveyFunc = (saveNo, callback) => {
      this.survey = creator.JSON
      this.saveNo = saveNo
      callback(saveNo, true)// 将当前问卷保存到本地
    }
    creator.render('surveyCreator')
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.form = this.$route.query.survey
      this.timeRange[0] = this.form.startTime
      this.timeRange[1] = this.form.endTime
      this.listLoading = false
      return
    },
    preview() {
      // 模拟答题
      const next = this.$router.resolve({
        path: '/questionnaire/preview',
        query: { survey: JSON.stringify(this.survey) }
      })
      window.open(next.href, '_blank')
    },
    onSubmit() {
      this.isEditing = false
      this.form.createdBy = this.$store.getters.user.id
      this.form.startTime = this.timeRange[0]
      this.form.endTime = this.timeRange[1]
      // 提交问卷给后台
      updateSurvey(this.form).then(
        (res)=>{
          Message({
            message: res.msg,
            type: 'success',
            duration: 2000
          })
        }
      )
    },
    startEdit() {
      // 开始编辑问卷
      this.isEditing = true
    }
  }
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 200px;
  height: 80%;
}

.el-main {

  background-color: white;
  color: #333;
  text-align: center;
  line-height: 160px;
}

.el-container {
  margin-bottom: 40px;
}

.el-form-item {
  margin-top: 20px;
  margin-right: 20px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.edit-survey {
  width: 100%;
  height: 100%;
}

#surveyCreator {
  height: 1000px;
}

.fade-leave-active {
  transition: opacity 0s;
}

.fade-enter-active {
  transition: opacity .5s;
}

.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
{
  opacity: 0;
}
</style>

