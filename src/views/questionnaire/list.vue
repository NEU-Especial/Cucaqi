<template>
  <el-container style="margin-top: 2%;width: 95%;margin-left: 2%;background-color: #99a9bf">
    <el-header>
      <!--      header中定义搜索框以及添加新问卷框-->
      <el-row>
        <el-col :span="8">
          <el-button style="margin-right: 20px" type="primary" icon=" el-icon-refresh">刷新</el-button>
          <vxe-input v-model="searchContent" style="width: 70%" placeholder="搜索问卷" type="search" clearable/>
        </el-col>
        <el-col :span="8">
          <div class="grid-content "/>
        </el-col>
        <el-col :span="8">
          <vxe-button size="medium" status="primary" content="创建问卷"/>
          <vxe-button size="medium" status="default" content="历史问卷"/>
          <vxe-button size="medium" status="danger" content="删除选中" @click="deleteSurveys"/>
        </el-col>
      </el-row>
    </el-header>

    <el-main>
      <vxe-table
        border
        stripe
        height="550"
        :loading="loading"
        :column-config="{resizable: true}"
        :row-config="{isHover: true}"
        :checkbox-config="{labelField: 'id', highlight: true, range: true}"
        :data="tableData"
      >
        <vxe-column type="checkbox" title="选择" width="80"/>
        <vxe-column type="seq" title="序号" width="60"/>
        <vxe-column field="title" title="标题" width="240" show-overflow="ellipsis"/>
        <vxe-column field="title" title="创建人" width="120"/>
        <vxe-column field="title" title="创建时间" width="120"/>
        <vxe-column field="title" title="开始时间" width="140"/>
        <vxe-column field="title" title="结束时间" width="140"/>
        <vxe-column field="title" title="问卷类型" width="80"/>
        <vxe-column field="title" title="限制人数" width="80"/>
        <vxe-column field="title" title="答题人数" width="100" sortable/>
        <vxe-column field="option" title="操作"/>
      </vxe-table>
    </el-main>
    <el-footer>

      <el-row>
        <el-col :span="16">
          <div class="grid-content "/>
        </el-col>
        <el-col :span="8">
          <vxe-pager
            :loading="loading1"
            :current-page="tablePage.currentPage"
            :page-size="tablePage.pageSize"
            :total="tablePage.totalResult"
            :layouts="['PrevPage', 'JumpNumber', 'NextPage', 'FullJump', 'Sizes', 'Total']"
            @page-change="handlePageChange"
          >
          </vxe-pager>
        </el-col>
      </el-row>
    </el-footer>
  </el-container>
</template>

<script>

import { VXETable } from 'vxe-table'

export default {
  data() {
    return {
      searchContent: '',
      loading1: false,
      tableData: [],
      tablePage: {
        currentPage: 1,
        pageSize: 10,
        totalResult: 0
      }
    }
  },
  methods: {
    deleteSurveys() {
      VXETable.modal.confirm('您确定要删除吗？').then(type => {
        VXETable.modal.message({ content: `删除成功`, status: 'success' })
      })
    },
    findList() {
      // 这里发请求，处理数据
    },
    handlePageChange({ currentPage, pageSize }) {
      this.tablePage.currentPage = currentPage
      this.tablePage.pageSize = pageSize
      this.findList()
    }
  }
}
</script>

<style>
.el-header, .el-footer {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
