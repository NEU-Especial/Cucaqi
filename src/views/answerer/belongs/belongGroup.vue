<template>
  <el-dialog :title="title" :visible.sync="openBelongs" width="1000px" :close-on-click-modal="false" append-to-body>
    <div class="app-container">
      <div class="filter-container">
        <el-input
          v-model="listQuery.group_name"
          placeholder="组名"
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

        <el-table-column label="所属群组" min-width="50px" width="80px">
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
            <span>{{ row.createdTime}}</span>
          </template>
        </el-table-column>

        <el-table-column label="群组创建人" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.createdBy | statusFilter">
              {{ row.createdBy }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row.id,$index)">
              移出群组
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


      <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
        <el-form
          ref="dataForm"
          :rules="rules"
          :model="temp"
          label-position="left"
          label-width="90px"
          style="width: 400px; margin-left:50px;"
        >
          <el-form-item label="组名" prop="type">
            <el-input v-model="temp.groupName" />
          </el-form-item>
          <el-form-item label="描述" prop="type">
            <el-input v-model="temp.description" />
          </el-form-item>
          <el-form-item label="创建时间" prop="timestamp">
            <el-date-picker v-model="temp.createdTime" type="datetime" placeholder="Please pick a date" />
          </el-form-item>
          <el-form-item label="创建人" prop="title">
            <el-input v-model="temp.createdBy" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
            {{ dialogStatus === 'create' ? '确认创建' : '确认修改' }}
          </el-button>
        </div>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import {
  addGroup,
  deleteFromGroupAnswererRelation,
  deleteGroup,
  getAllGroupByAnswererId,
  getGroupPage,
  updateGroup
} from "@/api/group";
import {Message} from "element-ui";
import {getLesseeList} from "@/api/lessee"; // secondary package based on el-pagination

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

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
      openBelongs:false,
      answererId:1,
      tableKey: 0,
      list:
       [
          {
          groupName: '秒天',
          description: '',
          createdTime:new Date(),
          createdBy: '太阳',
          id: 10
          }
      ],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        group_name: '', // 过滤名称
        sort: '+id'
      },
      importanceOptions: ['有答者', '无答者'],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      showReviewer: false,
      temp: {
        groupName:'',
        description:'',
        createdBy: '',
        createdTime: new Date(),
        // deleted:'',
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        groupName: [{ required: true, message: '组名必填', trigger: 'blur' }],
        description: [{ required: true, message: '描述必填', trigger: 'blur' }],
        createdTime: [{ type: 'date', required: true, message: '时间必填', trigger: 'change' }],
        createdBy: [{ required: true, message: '创建人必填', trigger: 'blur' }]
      },
      downloadLoading: false,
      idx: -1,
      totalList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList(answererId) {
      this.answererId = answererId
      this.listLoading = true
      this.$nextTick(()=>{
        getAllGroupByAnswererId(answererId).then(
          res => {
            this.list = res.data
            this.totalList = res.data
            this.listLoading = false
            this.total = res.data.length
          }
        )
        this.openBelongs=true
      })
    },
    handleFilter() {
      // 执行过滤，需要查询分页条件等信息
      // 分别有关键字，id升降序，分页
      // 先找关键字
      this.list = this.totalList
      var word = this.listQuery.group_name
      var filterList = this.list.filter(function(group) {
        return group.groupName.includes(word)
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
        groupName: undefined,
        createdBy: this.$store.getters.user.id,
        createdTime: new Date()
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {

      addGroup(this.temp).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.getList()
        }
      )
    },
    handleUpdate(row, index) {
      this.temp = row
      this.idx = index
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    updateData() {
      updateGroup(this.temp).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.list[this.index] = { ...this.temp }
          this.resetTemp()
        }
      )
    },
    handleDelete(groupId,index) {
      var answererId =this.answererId
      deleteFromGroupAnswererRelation(groupId,answererId).then(
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
