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

      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-delete"
        @click="handleRecover"
      >
        恢复历史记录
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
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="开始时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="结束时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="限制人数" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.limitCount|limit }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答题人数" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.curCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="问卷状态" class-name="status-col" width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.state | statusFilter">
            {{ ['未发布', '已发布', '已截止', '已删除'][row.state] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否公开" width="100px" align="center">
        <template slot-scope="{row}">
          {{ row.isPublic }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="625" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="info" v-show="row.state===1" size="mini" @click="handleGetLink(row)">
            获取链接
          </el-button>
          <el-button type="warning" v-show="row.state===1" size="mini" @click="handleUpdate(row)">
            提前结束
          </el-button>
          <el-button
            v-show="row.state===0"
            size="mini"
            type="success"
            @click="handleModifyStatus(row)"
          >
            发布问卷
          </el-button>
          <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除问卷
          </el-button>
          <el-button type="primary" size="mini" @click="handleAnswerList(row)">
            答卷列表
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

    <!--答卷列表弹出框-->
    <el-dialog
      title="答卷列表"
      :visible.sync="answerListTableVisible"
    >
      <el-row>
        <el-button type="primary" round @click="jump">查看整体情况</el-button>
      </el-row>
      <el-table :data="answerListData">
        <el-table-column property="answererName" label="答卷人姓名"/>
        <el-table-column property="answerTime" label="交卷时间"/>
        <el-table-column property="operation" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleAnswerDetail(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!--答卷细节弹出框-->
    <el-dialog title="答卷详情" :visible.sync="answerDetailTableVisible">
      <survey :survey="survey"/>
    </el-dialog>

    <!--恢复历史记录弹出框-->
    <el-dialog title="历史记录" :visible.sync="openRecoverDialog" width="1400px">
      <recover ref="recover" :parent-list="list" @refresh="getNewList"/>
    </el-dialog>

    <!--编辑问卷弹出框-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="90px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startedTime">
          <el-input v-model="temp.startedTime"/>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-input v-model="temp.endTime"/>
        </el-form-item>

        <el-form-item label="问卷状态">
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            v-model="temp.remark"
            readonly
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="此问卷处于发布状态不允许修改！"
          />
        </el-form-item>
        <el-form-item label-width="10px">
          <el-button>编辑问卷题目</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!--    获取已发布问卷的弹窗-->
    <el-dialog :visible.sync="openLinkDialog" title="获取问卷链接">
      <el-table :data="postData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="postAddress" label="问卷地址"/>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="openLinkDialog=false">确认</el-button>
      </span>
    </el-dialog>

    <!--    公开问卷发布时的弹窗-->
    <el-dialog :visible.sync="postPublicDialog" title="发布问卷">
      <el-table :data="postData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="postAddress" label="问卷地址"/>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="postToPublic">确认发布</el-button>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="postPublicDialog=false">取消</el-button>
      </span>
    </el-dialog>

    <!--    私有问卷发布时的弹窗-->
    <el-dialog :visible.sync="postPrivateDialog" title="发布问卷" width="1000px">
      <el-table
        :key="tableKey"
        v-loading="postListLoading"
        :data="postGroupData"
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

        <el-table-column label="组名" min-width="50px" width="80px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.groupName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="描述" min-width="50px" width="80px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.description }}</span>
          </template>
        </el-table-column>

        <el-table-column label="创建时间" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createdTime |parse }}</span>
          </template>
        </el-table-column>

        <el-table-column label="创建人" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.createdBy | statusFilter">
              {{ row.createdBy }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.status!=='deleted'" size="mini" type="success" @click="handlePost(row)">
              发布
            </el-button>
          </template>
        </el-table-column>

      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="postPrivateDialog = false">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import { Model, StylesManager } from 'survey-vue'
import 'survey-vue/defaultV2.css'
import Pagination from '@/components/Pagination'
import { getGroupPage } from '@/api/group'
import recover from '@/views/questionnaire/recover'
import { findAllSurvey, PostToGroup, PostToPublic, softDeleteSurvey, updateSurveyState } from '@/api/survey'
import { Message } from 'element-ui'

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
  components: { Pagination, recover },
  directives: { waves },
  filters: {
    limit(count) {
      if (count === 0) {
        return '无限制'
      }
      return count
    },
    statusFilter(status) {
      const statusMap = [
        'success',
        'primary',
        'danger'
      ]
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    },
    parse(date) {
      var arr = date
      if (arr == null || arr === '') {
        return '-'
      } else {
        for (var i = 0; i < arr.length; i++) {
          if (arr[i].length === 1) {
            arr[i] = '0' + arr[i]
          }
        }
        var getFormatTime
        if (arr.length === 5) {
          getFormatTime = arr[0] + '-' + arr[1] + '-' + arr[2] + '\t' + arr[3] + ':' + arr[4] + ':' + '00'
        } else {
          getFormatTime = arr[0] + '-' + arr[1] + '-' + arr[2] + '\t' + arr[3] + ':' + arr[4] + ':' + arr[5]
        }
        return getFormatTime
      }
    }
  },
  data() {
    const answerListData = []
    const surveyJson = ''
    const answerJson = ''
    const survey = new Model(surveyJson)
    survey.data = answerJson
    survey.mode = 'display'
    return {
      tableKey: 0,
      list: [],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      statusOptions: ['已发布', 'draft', '未发布'],
      showReviewer: false,
      temp: {}, // 临时问卷数据
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
      openRecoverDialog: false,
      openLinkDialog: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleDelete(row, index) {
      if (row.state === 1) {
        Message({
          message: '问卷进行中不允许删除',
          type: 'error',
          duration: 2000
        })
        return
      }
      softDeleteSurvey({ surveyId: row.id }).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 2000
          })
          updateSurveyState({ state: 3, surveyId: row.id })
          this.list.splice(index, 1)
        }
      )
    },
    postToPublic() {
      PostToPublic({ surveyId: this.temp.id, userId: this.$store.getters.user.id }).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 2000
          })
          if (this.temp.state === 0) {
            updateSurveyState({ state: 1, surveyId: this.temp.id })
          }

          this.temp.state = 1
          this.postPublicDialog = false
        }
      )
    },
    jump() {
      this.$router.push('/questionnaire/jump')
    },
    getList() {
      findAllSurvey({ id: this.$store.getters.user.id }).then(
        (res) => {
          this.list = res.data
          this.total = res.data.length
        }
      )
      this.listLoading = false
      return
    },
    getNewList() {
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row) {
      this.temp = row
      if (row.isPublic === true) {
        // 公开问卷发布，生成链接，发布给所有的已注册答者
        this.postPublicDialog = true
      } else {
        this.postListLoading = true
        getGroupPage(this.$store.getters.user.id).then(
          res => {
            this.postGroupData = res.data
            this.postListLoading = false
          }
        )
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
      updateSurveyState({ state: 2, surveyId: row.id })
      row.state = 2
    },
    updateData() {
      this.list[0].title = this.temp.title
      this.$notify({
        title: 'Success',
        message: '问卷修改成功',
        type: 'success',
        duration: 2000
      })
      this.dialogFormVisible = false
    },
    handlePost(row) {
      PostToGroup({ surveyId: this.temp.id, groupId: row.id }).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 2000
          })
          if (this.temp.state === 0) {
            updateSurveyState({ state: 1, surveyId: this.temp.id })
          }
          this.temp.state = 1
          this.postPublicDialog = false
        }
      )
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
    handleRecover() {
      this.openRecoverDialog = true
      this.$nextTick(() => {
        this.$refs.recover.getList()
      })
    },
    handleGetLink(row) {
      this.openLinkDialog = true;
    }
  }
}
</script>
