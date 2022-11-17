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
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 60%;"
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
      <el-table-column label="操作" align="center" width="625" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button :disabled="row.answer!==null" type="primary" size="mini" @click="answerSurvey(row)">
            开始答卷
          </el-button>
          <el-button type="primary" size="mini" @click="viewAnalytical(row)">
            查看答卷统计
          </el-button>
          <el-button v-show="row.answer!==undefined" type="success" size="mini">
            回顾答题结果
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
import 'survey-vue/defaultV2.css'
import Pagination from '@/components/Pagination'
import { allSurveyToAnswer } from '@/api/survey'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = [
        'success',
        'primary',
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
  data() {
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
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      statusOptions: ['已发布', 'draft', '未发布'],
      showReviewer: false,
      temp: {}, // 临时问卷数据
      dialogFormVisible2: false,
      dialogFormVisible: false

    }
  },
  created() {
    this.getList()
  },
  methods: {
    answerSurvey(row) {
      const next = this.$router.resolve({
        path: '/survey',
        query: { surveyId: row.id, answererId: this.$store.getters.user.id }
      })
      window.open(next.href, '_blank')
    },
    getList() {
      allSurveyToAnswer({ id: this.$store.getters.user.id }).then(
        (res) => {
          for (let i = 0; i < res.data.surveyList.length; i++) {
            this.list.push(res.data.surveyList[i])
            this.list[i].answer = res.data.answers[i]
          }
          this.total = res.data.surveyList.length
        }
      )
      this.listLoading = false
      return
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row) {
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
    }
  }
}
</script>
