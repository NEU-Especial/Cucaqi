<template>
  <el-table
    :data="tableData"
    style="width: 100%"
  >
    <el-table-column
      prop="ID"
      label="题号"
      width="80"
    />
    <el-table-column
      prop="question"
      label="问题"
      width="240"
    />
    <el-table-column
      prop="type"
      label="问题类型"
      width="120"
    />
    <el-table-column
      prop="answer"
      label="回答人数"
      width="80"
    />
    <el-table-column
      prop="count"
      label="是否计分"
      width="80"
    />
    <el-table-column
      prop="goal"
      label="平均得分情况"
      width="120"
    />
  </el-table>
</template>

<script>
export default {
  data() {
    return {
      tableData: [{
        ID: 1,
        question: '你的学校是什么？',
        type: '问答题',
        answer: '18',
        count: '不计分',
        goal: ''
      }, {
        ID: 2,
        question: '你是否喜欢计算机？',
        type: '选择题',
        answer: '18',
        count: '计分',
        goal: '90'
      }, {
        ID: 3,
        question: '你对编程语言的熟悉程度',
        type: '选做题',
        answer: '8',
        count: '不计分',
        goal: ''
      }]
    }
  }
}
</script>
