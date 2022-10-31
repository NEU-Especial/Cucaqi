<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.title"
        placeholder="用户姓名"
        style="width:400px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.importance" placeholder="状态" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item" />
      </el-select>
      <!--      <el-select v-model="listQuery.type" placeholder="问卷类型" clearable class="filter-item" style="width: 130px">-->
      <!--        <el-option-->
      <!--          v-for="item in calendarTypeOptions"-->
      <!--          :key="item.key"-->
      <!--          :label="item.display_name+'('+item.key+')'"-->
      <!--          :value="item.key"-->
      <!--        />-->
      <!--      </el-select>-->
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
        添加用户
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
      >
        批量导入
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
      style="width: 39%;"
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
      <el-table-column label="用户姓名" min-width="50px" width="80px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <!--      <el-table-column label="开始时间" width="130px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          <span>{{ row.startedTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <!--      <el-table-column label="结束时间" width="130px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          <span>{{ row.endTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <!--      <el-table-column label="限制人数" width="110px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          <span>{{ row.limit }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="答题人数" width="110px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          <span>{{ row.curCount }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="用户状态" class-name="status-col" width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column label="是否推荐" width="100px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          {{ row.isRecommend }}-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" >
            查询用户账单
          </el-button>
          <!--          <el-button-->
          <!--            v-if="row.status!=='published'"-->
          <!--            size="mini"-->
          <!--            type="success"-->
          <!--            @click="handleModifyStatus(row,'published')"-->
          <!--          >-->
          <!--            发布问卷-->
          <!--          </el-button>-->
          <!--          <el-button v-if="row.status!=='draft'" size="mini" @click="handleModifyStatus(row,'draft')">-->
          <!--            恢复问卷-->
          <!--          </el-button>-->
          <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除用户
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="90px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="用户姓名" prop="type">
          <el-input v-model="temp.type" />
          <!--          <el-select v-model="temp.type" class="filter-item" placeholder="Please select">-->
          <!--            <el-option-->
          <!--              v-for="item in calendarTypeOptions"-->
          <!--              :key="item.key"-->
          <!--              :label="item.display_name"-->
          <!--              :value="item.key"-->
          <!--            />-->
          <!--          </el-select>-->
        </el-form-item>
        <el-form-item label="创建时间" prop="timestamp">
          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date" />
        </el-form-item>
        <el-form-item label="初始密码" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <!--        <el-form-item label="Status">-->
        <!--          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">-->
        <!--            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="Imp">-->
        <!--          <el-rate-->
        <!--            v-model="temp.importance"-->
        <!--            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"-->
        <!--            :max="3"-->
        <!--            style="margin-top:8px;"-->
        <!--          />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="Remark">-->
        <!--          <el-input-->
        <!--            v-model="temp.remark"-->
        <!--            :autosize="{ minRows: 2, maxRows: 4}"-->
        <!--            type="textarea"-->
        <!--            placeholder="Please input"-->
        <!--          />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

    <!--    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>-->
  </div>
</template>

<script>
  import { createArticle, fetchList, fetchPv, updateArticle } from '@/api/article'
  import waves from '@/directive/waves' // waves directive
  import { parseTime } from '@/utils'
  import Pagination from '@/components/Pagination/index' // secondary package based on el-pagination
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
        list: [{
          title: '刘德华',
          createdTime: Date.parse(new Date()),
          startedTime: Date.parse(new Date()),
          endTime: Date.parse(new Date()),
          status: '',
          type: '',
          limit: 43,
          curCount: 3,
          isRecommend: '是',
          id: 10
        }],
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
        importanceOptions: ['有群组', '无群组'],
        calendarTypeOptions,
        sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
        // statusOptions: ['有群组', '无群组'],
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
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: 'Edit',
          create: 'Create'
        },
        dialogPvVisible: false,
        pvData: [],
        rules: {
          type: [{ required: true, message: 'type is required', trigger: 'change' }],
          timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
          title: [{ required: true, message: 'password is required', trigger: 'blur' }]
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = false
        return
        // eslint-disable-next-line no-unreachable
        fetchList(this.listQuery).then(response => {
          this.list = response.data.items
          this.total = response.data.total
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      handleModifyStatus(row, status) {
        this.$message({
          message: '操作Success',
          type: 'success'
        })
        row.status = status
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
          // status: '有群组',
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
        // eslint-disable-next-line no-unreachable
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
            this.temp.author = 'vue-element-admin'
            createArticle(this.temp).then(() => {
              this.list.unshift(this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: 'Success',
                message: 'Created Successfully',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
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
        // eslint-disable-next-line no-unreachable
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)
            tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
            updateArticle(tempData).then(() => {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: 'Success',
                message: 'Update Successfully',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },
      handleDelete(row, index) {
        this.$notify({
          title: 'Success',
          message: 'Delete Successfully',
          type: 'success',
          duration: 2000
        })
        this.list.splice(index, 1)
      },
      handleFetchPv(pv) {
        fetchPv(pv).then(response => {
          this.pvData = response.data.pvData
          this.dialogPvVisible = true
        })
      },
      handleDownload() {
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
