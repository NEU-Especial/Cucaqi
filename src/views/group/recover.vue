<template>
  <el-dialog :title="title" :visible.sync="openRecover" width="900px" :close-on-click-modal="false" append-to-body>
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
    <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row
      style="width: 90%;"
      @sort-change="sortChange"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')"
      >
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="组名" min-width="50px" width="80px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.groupName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="描述" min-width="50px" width="80px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.description }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{row.createdTime}}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建人" class-name="status-col" width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.createdBy | statusFilter">
            {{ row.createdBy }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!=='deleted'" size="mini" type="success" @click="handleRecover(row,$index)">
            恢复记录
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--使用群组详情组件-->
    <groupDetails title="群组详情" v-if="openDetailsDialog" ref="groupDetails" />

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
import {
  addGroup,
  deleteGroup,
  getDeletedGroupPage,
  getGroupPage,
  updateGroup,
  updateGroupDeletedStatus
} from "@/api/group";
import {Message} from "element-ui";
import groupDetails from "@/views/group/details/groupDetails";
import {updateDeletedStatus} from "@/api/answerer";


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
  components: { Pagination,groupDetails },
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
      userId: 1,
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
        limit: 10,
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
      openDetailsDialog:false,
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
        getDeletedGroupPage(this.$store.getters.user.id).then(
          res => {
            this.list = res.data
            this.totalList = res.data
            this.listLoading = false
            this.total = res.data.length
          }
        )
        this.openRecover = true
      })
    },
    handleGroupDetails(row) {
      this.openDetailsDialog = true;

      this.$nextTick(() => {
        this.$refs.groupDetails.getList(row.id);
      });
      // this.$router.push({ path: '/group/details', query:{id:row.id}})
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
    handleRecover(row,index){
      updateGroupDeletedStatus(row.id).then(
        (res) => {
          Message({
            message: res.msg,
            type: 'success',
            duration: 1000
          })
          this.list.splice(index, 1)
          this.$emit("refresh")
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
