<template>
  <!--  创建问卷表单-->
  <div>
    <transition name="fade">
      <el-container v-show="!isEditing" style="height: 1200px">
        <el-aside width="40%" style="height:800px;margin: 30px">
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="问卷标题" label-width="120px">
              <el-input v-model="form.name"/>
            </el-form-item>
            <el-form-item label="开始时间" label-width="120px">
              <el-col :span="11">
                <el-date-picker v-model="form.date1" type="date" placeholder="选择日期" style="width: 100%;"/>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="11">
                <el-time-picker v-model="form.date2" placeholder="选择时间" style="width: 100%;"/>
              </el-col>
            </el-form-item>

            <el-form-item label="结束时间(可选)" label-width="120px">
              <el-col :span="11">
                <el-date-picker v-model="form.date1" type="date" placeholder="选择日期" style="width: 100%;"/>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="11">
                <el-time-picker v-model="form.date2" placeholder="选择时间" style="width: 100%;"/>
              </el-col>
            </el-form-item>

            <el-form-item label="公开问卷" label-width="120px">
              <el-switch v-model="form.open"/>
            </el-form-item>

            <el-form-item label="选择群组" label-width="120px" :disabled="form.open">
              <el-col :span="24">
                <el-select
                  v-model="form.region"
                  :disabled="form.open"
                  placeholder="请选择群组"
                  multiple
                  style="width: 100%;height: 50px"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  size="medium"
                >
                  <el-option label="群组一" value="shanghai"/>
                  <el-option label="群组二" value="beijing"/>
                </el-select>
              </el-col>
            </el-form-item>

            <el-form-item label="限制回答人数" label-width="120px">
              <el-col :span="4">
                <el-switch v-model="form.limited" style="right: 0px"/>
              </el-col>

              <el-col :span="20">
                <el-input-number
                  v-model="form.maxCount"
                  :disabled="!form.limited"
                  :min="1"
                  label="回答人数"
                  style="float: right"
                />
              </el-col>
            </el-form-item>

            <el-form-item label="问卷类型" label-width="120px">
              <el-radio-group v-model="form.resource">
                <el-radio label="普通问卷"/>
                <el-radio label="优质问卷"/>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="问卷编辑" label-width="120px">
              <el-button type="primary" style="margin-right: 20px" @click="startEdit">编辑问卷</el-button>
              <el-button type="primary" plain @click="preview">模拟答题</el-button>
            </el-form-item>

            <el-form-item style="margin-top: 50px">

              <el-button type="primary" style="width: 400px;margin-left: 30px" @click="onSubmit">立即创建</el-button>
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
            <Survey :survey="surveyForm"/>
          </div>
        </el-main>
      </el-container>
    </transition>

    <transition name="fade">
      <div v-show="isEditing" style="width: 100%;height:1200px">
        <el-button type="primary" style="margin: 5px" icon="el-icon-arrow-left" @click="isEditing=false">返回创建
        </el-button>
        <br>
        <div id="surveyCreator"/>
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
      form: {
        name: '',
        region: '',
        dateStart: '',
        timeStart: '',
        dateEnd: '',
        timeEnd: '',
        limited: true,
        open: true,
        maxCount: 0,
        resource: '',
        desc: ''
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
  methods: {
    preview() {
      // 模拟答题
      const next = this.$router.resolve({ path: '/questionnaire/preview', query: { survey: JSON.stringify(this.survey) }})
      window.open(next.href, '_blank')
    },
    onSubmit() {
      this.isEditing = false
      // 提交问卷给后台
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

