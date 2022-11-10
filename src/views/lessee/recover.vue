<template>
  <el-dialog :title="title" :visible.sync="openRecover" width="1500px" :close-on-click-modal="false" append-to-body>
    <div class="app-container">
      <div class="filter-container">
        <el-input
          v-model="listQuery.lessee_name"
          placeholder="租户姓名"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
          <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>

      </div>
      <br>
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
        <el-table-column label="租户姓名" min-width="50px" width="80px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租户密码" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.password }}</span>
          </template>
        </el-table-column>

        <el-table-column label="租户电话" min-width="150px" width="180px">
          <template slot-scope="{row}">
            <span class="link-type">{{ row.telephone }}</span>
          </template>
        </el-table-column>

        <el-table-column label="租户邮箱" min-width="150px" width="180px">
          <template slot-scope="{row}">
            <span class="link-type">{{ row.email }}</span>
          </template>
        </el-table-column>

        <el-table-column label="租户账单" min-width="150px" width="180px">
          <template slot-scope="{row}">
            <span class="link-type">{{ row.payment }}</span>
          </template>
        </el-table-column>

        <el-table-column label="租户邀请码" min-width="150px" width="180px">
          <template slot-scope="{row}">
            <span class="link-type">{{ row.inviteCode }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button v-if="row.status!=='deleted'" size="mini" type="success" @click="handleDelete(row,$index)">
              恢复记录
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :limit.sync="listQuery.limit"
        :page.sync="listQuery.page"
        :total="total"
        @pagination="pagination"
      />
    </div>
  </el-dialog>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import {addLessee, deleteLessee, getDeletedLesseeList, getLesseeList, updateLessee} from '@/api/lessee'
import { Message } from 'element-ui' // secondary package based on el-pagination
export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [{
        username: '刘德华',
        createdTime: Date.parse(new Date()),
        id: 10,
        telephone: 11111111111,
        email: '16381316928@qq.com',
        payment: 20
      }],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        lessee_name: '', // 过滤名称
        sort: '+id'
      },
      importanceOptions: ['有群组', '无群组'],
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      showReviewer: false,
      temp: {
        username: undefined,
        password: undefined,
        inviteCode: '',
        telephone: '',
        email: '',
        payment: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        username: [{ required: true, message: '用户名必填', trigger: 'blur' }],
        password: [{ required: true, message: '密码必填', trigger: 'blur' }]
      },
      downloadLoading: false,
      idx: -1,
      totalList: [],
      openRecover:false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      this.$nextTick(()=>{
        getDeletedLesseeList().then(
          res => {
            this.list = res.data
            this.totalList = res.data
            this.listLoading = false
            this.total = res.data.length
          }
        )
        this.openRecover=true
      })
    },
    handleFilter() {
      // 执行过滤，需要查询分页条件等信息
      // 分别有关键字，id升降序，分页
      // 先找关键字
      this.list = this.totalList
      var word = this.listQuery.lessee_name
      var filterList = this.list.filter(function(lessee) {
        return lessee.username.includes(word)
      })
      // 过滤完成之后需要进行排序
      filterList.sort((a, b) => {
        if (this.listQuery.sort === '+id') {
          return a.id - b.id
        } else {
          return b.id - a.id
        }
      })
      var page = this.listQuery.page
      var limit = this.listQuery.limit

      // 进行分页处理,找到对应的位置
      this.list = filterList.slice((page - 1) * limit, (page - 1) * limit + limit)
    },
    handleModifyStatus(row) {

    },
    pagination() {
      this.list = this.totalList
      var page = this.listQuery.page
      var limit = this.listQuery.limit
      this.list = this.list.slice((page - 1) * limit, (page - 1) * limit + limit)
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
        username: undefined,
        password: undefined,
        inviteCode: '',
        telephone: '',
        email: '',
        payment: 0,
        createdBy: this.$store.getters.user.id
      }
    },

    handleDelete(row, index) {
      deleteLessee(row).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.list.splice(index, 1)
        }
      )
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
