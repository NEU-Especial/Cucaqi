<template>
  <el-table :data="list" style="width: 100%;padding-top: 15px;">
    <el-table-column label="问卷名" min-width="130">
      <template slot-scope="scope">
        {{ scope.row.title | orderNoFilter }}
      </template>
    </el-table-column>
    <el-table-column label="答题人数" width="195" align="center">
      <template slot-scope="scope">
        {{ scope.row.num | toThousandFilter }}
      </template>
    </el-table-column>
    <el-table-column label="开启状态" width="100" align="center">
      <template slot-scope="{row}">
        <el-tag :type="row.status | statusFilter">
          {{ row.status }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'danger'
      }
      return statusMap[status]
    },
    orderNoFilter(str) {
      return str.substring(0, 30)
    }
  },
  data() {
    return {
      list: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.list = [
        { title: '酒水调查', num: 213, status: '发布中' },
        { title: '编程语言调查', num: 1346, status: '发布中' },
        { title: '大学生作息时间调查', num: 713, status: '已结束' },
        { title: '学校满意度调查', num: 1355, status: '已结束' },
        { title: '作业量调查', num: 265, status: '发布中' }
      ]
    }
  }
}

</script>
