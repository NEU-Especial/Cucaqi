<template>
  <!--  创建问卷表单-->
  <div>
    <transition name="fade">

      <el-container v-show="!isEditing" style="height: 1200px">
        <el-aside width="40%" style="height:800px;margin: 30px">

          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="头像" label-width="120px">
              <el-col :span="16" align="left">
                <el-image
                  style="width: 100px; height: 100px"
                  src="https://avatars.githubusercontent.com/u/116621808"
                  fit="fill"
                />
              </el-col>
            </el-form-item>

            <el-form-item label="用户名" label-width="120px">
              <el-input v-model="form.name" />
            </el-form-item>

            <el-form-item label="密码" label-width="120px">
              <el-input v-model="form.password" type="password" />
            </el-form-item>

            <el-form-item label="确认密码" label-width="120px">
              <el-input v-model="form.password" type="password" />
            </el-form-item>

            <el-form-item label="绑定手机号码" label-width="120px">
              <el-col :span="16" align="center">
                <el-input v-model="form.telephone" placeholder="输入电话号码" />
              </el-col>
              <el-col :span="8" align="right">
                <el-button type="primary">发送验证码</el-button>
              </el-col>
            </el-form-item>

            <el-form-item label="选择密保问题" label-width="120px" :disabled="form.open">
              <el-col :span="24">
                <el-select
                  v-model="form.question"
                  :disabled="form.open"
                  placeholder="选择密保问题"
                  style="width: 100%;height: 50px"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  size="medium"
                >
                  <el-option label="密保一" value="shanghai" />
                  <el-option label="密保二" value="shanghai" />
                </el-select>
              </el-col>
            </el-form-item>

            <el-form-item label="答案" label-width="120px">
              <el-input v-model="form.answer" :disabled="form.question===''" />
            </el-form-item>

            <el-form-item style="margin-top: 50px">
              <el-button type="primary" style="width: 200px;margin-left: 30px" @click="onSubmit">更新个人信息</el-button>
            </el-form-item>
          </el-form>
        </el-aside>
        <!--    这里放预览表单-->
      </el-container>
    </transition>
  </div>
</template>

<script>

export default {
  name: 'Profile',
  data: function() {
    return {
      form: {
        name: '',
        password: '',
        confirmPassword: '',
        question: '',
        answer: ''
      }
    }
  },
  computed: {},
  mounted() {
  },
  methods: {
    onSubmit() {
    }
  }
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 200px;
  height: 80%;
}

.el-main {

  background-color: white;
  color: #333;
  text-align: center;
  line-height: 160px;
}

.el-container {
  margin-bottom: 40px;
}

.el-form-item {
  margin-top: 20px;
  margin-right: 20px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.edit-survey {
  width: 100%;
  height: 100%;
}

#surveyCreator {
  height: 1000px;
}

.fade-leave-active {
  transition: opacity 0s;
}

.fade-enter-active {
  transition: opacity .5s;
}

.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
{
  opacity: 0;
}
</style>

