<template>
  <el-dialog :title="title" :visible.sync="openDetails" width="1000px" :close-on-click-modal="false" append-to-body>
    <div class="app-container">
      <div class="filter-container">
        <el-input
          v-model="listQuery.answerer_name"
          placeholder="姓名"
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
        <el-button
          class="filter-item"
          style="margin-left: 10px;"
          type="primary"
          icon="el-icon-edit"
          @click="handleCreate"
        >
          添加答者
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

        <el-table-column label="组员姓名" min-width="50px" width="80px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.username }}</span>
          </template>
        </el-table-column>

        <el-table-column label="联系电话" min-width="50px" width="120px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.telephone }}</span>
          </template>
        </el-table-column>

        <el-table-column label="邮箱" min-width="50px" width="120px">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.email }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
  <!--          第一个id为groupId，第二个id为answererId-->
            <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row.id,$index)">
              删除组员
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
          :data="allAnswerers"
          label-position="left"
          label-width="90px"
          style="width: 400px; margin-left:50px;"
        >
          <el-form-item label="答者姓名">
            <el-select v-model="temp.username" style="width: 140px" clearable="" @change="answererChange" >
              <el-option v-for="item in allAnswerers" :key="item.id" :label="item.username" :value="item.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="答者电话号码" prop="title">
            <el-input class="answerer" v-model="temp.telephone" value=""/>
          </el-form-item>
          <el-form-item label="答者邮箱号码" prop="title">
            <el-input class="answerer" v-model="temp.email"/>
          </el-form-item>
        </el-form>


        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
            确认添加
          </el-button>
        </div>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import {
  addGroup, addToGroupAnswererRelation,
  deleteFromGroupAnswererRelation,
  updateGroup
} from "@/api/group";
import {Message} from "element-ui";
import {getAllAnswerer, getAllAnswererByGroupId} from "@/api/answerer"; // secondary package based on el-pagination

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
      openDetails:false,
      groupId: '',
      tableKey: 0,
      allAnswerers:[],
      selectAnswerer:'请选择答者',
      list:
       [
          {
          username: '张三',
          telephone:13940131469,
          email:'',
          createBy: '地球组',
          id: 10
          },
      ],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        answerer_name: '', // 过滤名称
        sort: '+id'
      },
      importanceOptions: ['有答者', '无答者'],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      showReviewer: false,
      temp: {
        id:'',
        username:'',
        password:'',
        telephone:'',
        payment:0,
        createdBy:'',
        email:'',
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
        username: [{ required: true, message: 'username is required', trigger: 'change' }],
        password: [{ required: true, message: 'password is required', trigger: 'blur' }],
        telephone: [{ required: true, message: 'telephone is required', trigger: 'blur' }],
        email: [{ required: true, message: 'email is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList(groupId) {
      this.groupId=groupId
      this.listLoading = true
      this.$nextTick(()=>{
        getAllAnswererByGroupId(groupId).then(
          res => {
            this.list = res.data
            this.totalList = res.data
            this.total = res.data.length
            this.listLoading = false
          }
        )
        getAllAnswerer().then(
          res => {
            this.allAnswerers = res.data
          }
        )
        this.openDetails=true
      })
    },
    handleFilter() {
      // 执行过滤，需要查询分页条件等信息
      // 分别有关键字，id升降序，分页
      // 先找关键字
      this.list = this.totalList
      var word = this.listQuery.answerer_name
      var filterList = this.list.filter(function(answerer) {
        return answerer.username.includes(word)
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
    answererChange(val){
        this.allAnswerers.forEach((v) => {
          if (v.username == val) {
            this.temp.id = v.id;
            this.temp.telephone = v.telephone;
            this.temp.email = v.email;
            this.temp.createdBy = v.createBy;
          }
          return false;
        });
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
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      var groupId = this.groupId
      var answererId = this.temp.id
      addToGroupAnswererRelation(groupId,answererId).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.getList(groupId)
          this.dialogFormVisible=false
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
          this.dialogFormVisible=false
        }
      )
    },
    handleDelete(answererId,index) {
      var groupId =this.groupId
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
