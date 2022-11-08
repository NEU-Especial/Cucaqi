<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.lessee_name"
        placeholder="答者姓名"
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
      style="width: 90%;"
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
      <el-table-column label="答者姓名" min-width="50px" width="80px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答者密码" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.password }}</span>
        </template>
      </el-table-column>

      <el-table-column label="答者电话" min-width="150px" width="180px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.telephone }}</span>
        </template>
      </el-table-column>

      <el-table-column label="答者邮箱" min-width="150px" width="180px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建人ID" min-width="150px" width="180px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.createdBy }}</span>
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
            编辑答者信息
          </el-button>
          <el-button
            class="filter-item"
            size="mini"
            type="primary"
            @click="handleBelongGroup(row)"
          >
            答者所属群组
          </el-button>
          <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除答者
          </el-button>

        </template>
      </el-table-column>
    </el-table>
    <!--使用所属群组组件-->
    <belongGroup title="所属群组" v-if="openBelongDialog" ref="belongGroup" />

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
        <el-form-item label="答者姓名" prop="username">
          <el-input v-model="temp.username"/>
        </el-form-item>
        <el-form-item label="答者密码" prop="password">
          <el-input v-model="temp.password"/>
        </el-form-item>
        <el-form-item label="答者电话号码" prop="title">
          <el-input v-model="temp.telephone"/>
        </el-form-item>
        <el-form-item label="答者邮箱号码" prop="title">
          <el-input v-model="temp.email"/>
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
</template>

<script>

import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import { Message } from 'element-ui'
import { Delete, getAllAnswererByUserId, save, update } from '@/api/answerer' // secondary package based on el-pagination
import belongGroup from "@/views/answerer/belongs/belongGroup";

export default {
  name: 'ComplexTable',
  components: { Pagination,belongGroup },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
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
        payment: 0,
        limitCount: 0
      },
      openBelongDialog:false,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        username: [{ required: true, message: '答者名必填', trigger: 'blur' }],
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
    getList() {
      this.listLoading = true
      getAllAnswererByUserId(this.$store.getters.user.id).then(
        res => {
          this.list = res.data
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
      var filterList = this.list.filter(function(user) {
        return user.username.includes(word)
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
    handleBelongGroup(row) {
      this.openBelongDialog = true;
      this.$nextTick(() => {
        this.$refs.belongGroup.getList(row.id);
      });
      // this.$router.replace({ path: '/answerer/belongs', query:{id:row.id}})
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
      save(this.temp).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.getList()
          this.dialogFormVisible = false
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
      update(this.temp).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.list[this.index] = { ...this.temp }
          this.dialogFormVisible = false
        }
      )
    },
    handleDelete(row, index) {
      Delete(row.id).then(
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
