<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.title"
        placeholder="问卷标题"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.importance" placeholder="状态" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item"/>
      </el-select>
      <el-select v-model="listQuery.type" placeholder="问卷类型" clearable class="filter-item" style="width: 130px">
        <el-option
          v-for="item in calendarTypeOptions"
          :key="item.key"
          :label="item.display_name+'('+item.key+')'"
          :value="item.key"
        />
      </el-select>
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>

    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column
        label="ID"
        prop="id"
        sortable="custom"
        align="center"
        width="80"
        :class-name="getSortClass('id')"
      >
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="问卷标题" min-width="150px" width="150px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="开始时间" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startedTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="结束时间" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="限制人数" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.limit }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答题人数" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.curCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="问卷状态" class-name="status-col" width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否公开" width="100px" align="center">
        <template slot-scope="{row}">
          {{ row.isPublic }}
        </template>
      </el-table-column>
      <el-table-column label="是否推荐" width="100px" align="center">
        <template slot-scope="{row}">
          {{ row.isRecommend }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!=='deleted'" size="mini" type="success" @click="handleRecover(row,$index)">
            恢复
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import { Model, StylesManager } from 'survey-vue'
import 'survey-vue/defaultV2.css'
import Pagination from '@/components/Pagination'
import { getGroupPage } from '@/api/group'
import {Message} from "element-ui";

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

StylesManager.applyTheme('defaultV2')
// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: '未发布',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    const answerListData = [{
      answererName: '小王',
      answerTime: '2022-05-02'
    }, {
      answererName: '小李',
      answerTime: '2016-05-06'
    }, {
      answererName: '大王',
      answerTime: '2018-08-17'
    }]
    const surveyJson = {
      'title': '计算机专业调查问卷',
      'description': '调查计算机专业学生信息',
      'logoPosition': 'right',
      'pages': [
        {
          'name': '页面1',
          'elements': [
            {
              'type': 'text',
              'name': '问题1',
              'title': '你的学校'
            },
            {
              'type': 'boolean',
              'name': '问题2',
              'title': '你是否喜欢计算机'
            },
            {
              'type': 'matrix',
              'name': '问题3',
              'title': '你对编程语言的熟悉程度',
              'columns': [
                {
                  'value': 'Column 1',
                  'text': '了解'
                },
                {
                  'value': 'Column 2',
                  'text': '熟练'
                },
                {
                  'value': 'Column 3',
                  'text': '擅长'
                }
              ],
              'rows': [
                {
                  'value': 'Row 1',
                  'text': 'Java'
                },
                {
                  'value': 'Row 2',
                  'text': 'C++'
                }
              ]
            }
          ]
        }
      ]
    }
    const answerJson = {
      '问题1': '东北大学',
      '问题2': true,
      '问题3': {
        'Row 1': 'Column 1',
        'Row 2': 'Column 3'
      }
    }

    const survey = new Model(surveyJson)
    survey.data = answerJson
    survey.mode = 'display'
    var Random = require('mockjs').Random
    return {
      tableKey: 0,
      list: [
        {
          title: 'hello java',
          createdTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          startedTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          endTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          status: '未发布',
          type: '优质问卷',
          limit: 20,
          curCount: 6,
          isPublic: '是',
          isRecommend: '是',
          id: 8
        },
        {
          title: 'hello spring',
          createdTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          startedTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          endTime: Random.date('yyyy-MM-dd-hh:mm:ss'),
          status: '未发布',
          type: '优质问卷',
          limit: 30,
          curCount: 6,
          isPublic: '否',
          isRecommend: '是',
          id: 9
        }
      ],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        status: 'published'
      },
      dialogFormVisible2: false,
      dialogFormVisible: false,
      answerListTableVisible: false,
      answerDetailTableVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      answerListData: answerListData,
      surveyJson: surveyJson,
      answerJson: answerJson,
      survey: survey,
      downloadLoading: false,
      postPublicDialog: false,
      postPrivateDialog: false,
      postData: [
        {
          postAddress: 'http://localhost:9528/survey?surver='
        }
      ],
      postListLoading: false,
      postGroupData: [],
      openRecoverDialog:false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime(date) {
      const Y = date.getFullYear() + '-'
      const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      const D = date.getDate() + ' '
      const h = date.getHours() + ':'
      const m = date.getMinutes() + ':'
      const s = date.getSeconds()
      return Y + M + D + h + m + s
    },
    jump() {
      this.$router.push('/questionnaire/jump')
    },
    getList() {
      this.listLoading = false
      return
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      // postQuestionaire(row).then(()=>{
      //
      // })
      if (row.isPublic === '是') {
        this.$message({
          message: '操作Success',
          type: 'success'
        })
        row.status = status
        this.postPublicDialog = true
      } else {
        this.postListLoading = true
        getGroupPage(this.$store.getters.user.id).then(
          res => {
            this.postGroupData = res.data
            this.postListLoading = false
            this.total = res.data.length
          }
        )
        row.status = status
        this.postPrivateDialog = true
      }
      this.postData[0].postAddress = 'http://localhost:9528/survey?surver=' + row.id
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      return
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      return
    },
    handlePost(row, index) {
      this.$notify({
        title: 'Success',
        message: 'Post Successfully',
        type: 'success',
        duration: 2000
      })
    },
    handleAnswerList(row) {
      this.answerListTableVisible = true
    },
    handleAnswerDetail() {
      this.answerListTableVisible = false
      this.answerDetailTableVisible = true
    },
    handleAnswersDetail() {
      this.answerListTableVisible = false
      // this.answerDetailTableVisible = true
      this.dialogFormVisible2 = true
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    handleRecover(row,index){
      this.$message({
        message: '恢复成功',
        type: 'success'
      })
      this.list.splice(index, 1)
      this.$emit("refresh")
    }
  }
}
</script>
