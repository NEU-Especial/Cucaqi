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
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
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
          <span>{{ row.createdTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="开始时间" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="结束时间" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime| parse }}</span>
        </template>
      </el-table-column>

      <el-table-column label="限制人数" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.limitCount }}</span>
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

      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="success" @click="handleRecover(row,$index)">
            恢复
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import 'survey-vue/defaultV2.css'
import { getGroupPage } from '@/api/group'
import { findDeletedSurvey, RecoverSurvey } from '@/api/survey'
// arr to obj, such as { CN : "China", US : "USA" }

export default {
  name: 'ComplexTable',
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
        'primary',
        'success',
        'warning',
        'danger'
      ]
      return statusMap[status]
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
  props: {
    parentList: []
  },
  data() {
    return {
      tableKey: 0,
      list: [],
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
      openRecoverDialog: false
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
    getList() {
      this.listLoading = false
      findDeletedSurvey({ id: this.$store.getters.user.id }).then(
        (res) => {
          this.list = res.data
          this.total = res.data.length
        }
      )
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
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
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    handleRecover(row, index) {
      RecoverSurvey({ surveyId: row.id }).then(
        (res) => {
          this.$message({
            message: res.msg,
            type: 'success'
          })
          row.state = 0
          this.parentList.push(row)
          this.list.splice(index, 1)
        }
      )
    }
  }
}
</script>
