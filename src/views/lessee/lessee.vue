
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.lessee_name"
        placeholder="租户姓名"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
<!--      id排序-->
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
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加租户
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
      <el-table-column label="租户性别" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.gender }}</span>
        </template>
      </el-table-column>
      <el-table-column label="租户职业" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.job }}</span>
        </template>
      </el-table-column>
      <el-table-column label="租户年龄" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.age }}</span>
        </template>
      </el-table-column>
      <el-table-column label="租户生日" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.birth }}</span>
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
      <el-table-column label="租户邀请码" min-width="150px" width="180px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.inviteCode }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button
            class="filter-item"
            size="mini"
            type="primary"
            @click="handleUpdate(row,$index)"
          >
            编辑租户信息
          </el-button>
          <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除租户
          </el-button>
          <el-button size="mini" type="info" @click="handleLook(row,$index)">
            查看租户缴费
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
        :model="temp"
        :rules="rules"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="租户姓名" prop="username">
          <el-input v-model="temp.username"/>
        </el-form-item>
<!--        <el-form-item label="租户性别" prop="username">-->
<!--          <el-input v-model="temp.gender"/>-->
<!--        </el-form-item>-->
        <el-form-item label="租户密码" prop="password">
          <el-input v-model="temp.password"/>
        </el-form-item>
        <el-form-item label="租户性别" prop="gender">
          <el-select v-model="temp.gender" placeholder="请选择" >
            <el-option v-for="item in genderTypes" :key="item.id" :label="item.value" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="租户职业" prop="job">
          <el-input v-model="temp.job"/>
        </el-form-item>
        <el-form-item label="租户年龄" prop="age">
          <el-input v-model="temp.age"/>
        </el-form-item>
        <el-form-item label="租户生日" prop="birth">
          <div class="block">
            <el-date-picker
              v-model="temp.birth"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="租户电话号码" prop="title">
          <el-input v-model="temp.telephone"/>
        </el-form-item>
        <el-form-item label="租户邮箱号码" prop="title">
          <el-input v-model="temp.email"/>
        </el-form-item>
        <el-form-item label="租户邀请码" prop="title">
          <el-input v-model="temp.inviteCode"/>
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
<!--    下面的表单弹框为计费管理-->
    <el-dialog title="租户缴费详情" :visible.sync="dialogVisible" width="55%">
      <template>
        <el-table
          :data="listOfCost"
          border
          fit
          highlight-current-row
          >
          <el-table-column label="ID" min-width="150px" width="180px">
            <template slot-scope="{row}">
              <span class="link-type">{{ row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户名" min-width="150px" width="180px">
            <template slot-scope="{row}">
              <span class="link-type">{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="群组费用" min-width="150px" width="180px">
            <template slot-scope="{row}">
              <span class="link-type">{{ row.groupCost }}</span>
            </template>
          </el-table-column>
            <el-table-column label="问卷费用" min-width="150px" width="180px">
              <template slot-scope="{row}">
                <span class="link-type">{{ row.questionnaireCost }}</span>
              </template>
            </el-table-column>
            <el-table-column label="答卷费用" min-width="150px" width="180px">
              <template slot-scope="{row}">
                <span class="link-type">{{ row.surveyCost }}</span>
              </template>
            </el-table-column>
            <el-table-column label="总费用" min-width="150px" width="180px">
              <template slot-scope="{row}">
                <span class="link-type">{{ row.totalCost }}</span>
              </template>
            </el-table-column>

        </el-table>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import {addLessee, deleteLessee, getLesseeList, getMoneyList, updateLessee} from '@/api/lessee'
import { Message } from 'element-ui' // secondary package based on el-pagination

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
      tableKey: 0,
     listOfCost:[],

      list: [{
        username: '刘德华',
        createdTime: Date.parse(new Date()),
        id: 10,
        telephone: 11111111111,
        email: '16381316928@qq.com',
        payment: 20,
        birth:'',
        age:'',
        gender:'男',
        job:'演员',
      }],
      genderType: '',
      genderTypes: [{
        value: '男',
        id: '1',
        icon:'el-icon-male'
      }, {
        value: '女',
        id: '2',
        icon:'el-icon-male'
      }],
      total: 1,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        lessee_name: '', // 过滤名称
        sort: '+id'
      },
      paymentListQuery: {
        page: 1,
        limit: 20,
        lessee_name: '', // 过滤名称
        sort: '+payment'
      },
      importanceOptions: ['有群组', '无群组'],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      paymentSortOptions: [{ label: '账单额升序', key: '+payment' }, { label: '账单额降序', key: '-payment' }],
      showReviewer: false,
      temp: {
        username: undefined,
        password: undefined,
        gender:'',
        inviteCode: '',
        telephone: '',
        email: '',
        payment: 0,
        job:'',
        age:'',
        birth:undefined
      },
      value1:'',
      dialogFormVisible: false,
      dialogVisible:false,
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
      totalList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    add(m) {
      return m < 10 ? "0" + m : m;
    },
    format(shijianchuo) {
      //shijianchuo是整数，否则要parseInt转换
      var time = new Date(shijianchuo);
      var y = time.getFullYear();
      var m = time.getMonth() + 1;
      var d = time.getDate();
      var h = time.getHours();
      var mm = time.getMinutes();
      var s = time.getSeconds();
      return (
        y +
        "-" +
        this.add(m) +
        "-" +
        this.add(d)
      );
    },

    getList() {
      this.listLoading = true
      getLesseeList().then(
        res => {
          this.list = res.data
          for (var i = 0; i < this.list.length; i++) {
            this.list[i].birth = this.format(this.list[i].birth);
          }
          this.totalList = res.data
          this.listLoading = false
          this.total = res.data.length
        }
      )
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
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      addLessee(this.temp).then(
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
    handleLook(row, index){

      getMoneyList(row.id).then(

        (res) => {
          this.resetTemp()
          console.log(this.listOfCost)
          this.listOfCost=[]
          for (var i = 0; i < res.data.length; i++) {
            this.listOfCost.push(res.data[i])
            this.listOfCost[i].id = res.data[i].id
            this.listOfCost[i].name = res.data[i].username
            this.listOfCost[i].groupCost =  res.data[i].groupNum*1+"元"
            this.listOfCost[i].questionnaireCost = res.data[i].answerNum*0.5+"元"
            this.listOfCost[i].surveyCost = res.data[i].surveyNum*0.1+"元"
            this.listOfCost[i].totalCost=
              res.data[i].groupNum*1+res.data[i].answerNum*0.5+res.data[i].surveyNum*0.1+"元"
          }
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
        }
      )
      this.dialogVisible = true

    },
    updateData() {
      updateLessee(this.temp).then(
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
    },
    getPaymentClass: function(key) {
      const sort = this.paymentListQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
  }
}
</script>
