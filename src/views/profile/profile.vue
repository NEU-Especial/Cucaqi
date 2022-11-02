<!--  创建问卷表单-->
<template>
  <div>
    <transition name="fade">

      <el-container v-show="!isEditing" style="height: 1200px">
        <el-aside width="40%" style="height:100%;margin: 30px">

          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="头像" label-width="120px">
              <el-col :span="16" align="left">
                <el-image
                  style="width: 100px; height: 100%"
                  src="https://avatars.githubusercontent.com/u/116621808"
                  fit="fill"
                />
              </el-col>
            </el-form-item>

            <el-form-item label="用户名" label-width="120px">
              <el-input disabled="true" :value="form.username" />
            </el-form-item>

            <el-form-item label="新密码" label-width="120px">
              <el-input v-model="form.password" type="password" />
            </el-form-item>

            <el-form-item label="确认密码" label-width="120px">
              <el-input v-model="form.confirmPassword" type="password" />
            </el-form-item>

            <el-form-item style="margin-top: 20px">
              <el-button type="primary" style="margin-left: 30px" @click="updatePassword">更新密码</el-button>
            </el-form-item>

            <el-form-item label="邮箱地址" label-width="120px">
              <el-col :span="16" align="center">
                <el-input v-model="form.email" placeholder="输入新的邮箱地址" />
              </el-col>
              <el-col :span="8" align="right">
                <el-button type="primary" @click="getAuthCodeByEmail">发送验证码</el-button>
              </el-col>
            </el-form-item>

            <el-form-item label="邮箱验证码" label-width="120px">
              <el-col :span="8" align="center">
                <el-input v-model="authCode" placeholder="验证码" />
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right" @click="updateEmail">更新邮箱地址</el-button>
            </el-form-item>

            <el-form-item label="绑定手机号码" label-width="120px" style="margin-top: 50px">
              <el-col :span="16" align="center">
                <el-input v-model="form.telephone" placeholder="输入电话号码" />
              </el-col>
              <el-col :span="8" align="right">
                <el-button type="primary">发送验证码</el-button>
              </el-col>
            </el-form-item>

            <el-form-item label="短信验证码" label-width="120px">
              <el-col :span="8" align="center">
                <el-input v-model="authCode" placeholder="短信验证码" />
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right" @click="onSubmit">更新电话号码</el-button>
            </el-form-item>

            <el-form-item label="选择密保问题" label-width="120px" style="margin-top: 50px" align="left">
              <el-col :span="24">
                <el-select v-model="form.securityQuestion" :label="ques" placeholder="请选择密保问题" style="width: 70%">
                  <el-option
                    v-for="question in securityQuestions"
                    :key="question.id"
                    :label="question.content"
                    :value="question.id"
                  />
                </el-select>
              </el-col>
            </el-form-item>

            <el-form-item label="答案" label-width="120px">
              <el-col :span="10">
                <el-input v-model="form.securityAnswer" :disabled="form.securityQuestion===''" />
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right" @click="onSubmit">更新密保</el-button>
            </el-form-item>

            <el-form-item label="邀请码" label-width="120px">
              <span style="font-size: 14px">{{ form.inviteCode }}</span>
            </el-form-item>

            <el-form-item style="margin-top: 20px">
              <el-button type="primary" style="margin-left: 30px" @click="onSubmit">生成新邀请码</el-button>
            </el-form-item>

          </el-form>
        </el-aside>
        <!--    这里放预览表单-->
      </el-container>
    </transition>
  </div>
</template>

<script>

import { askAuthCodeByEmail, bindEmail, updatePassword } from '@/api/info'
import { Message } from 'element-ui'
import { getAllSecurityQuestion } from '@/api/login'

export default {
  name: 'Profile',

  data: function() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不应该小于六位'))
      } else {
        callback()
      }
    }
    const validateConfirmPassord = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: {
        id: '',
        username: '',
        password: '',
        confirmPassword: '',
        securityQuestion: '',
        securityAnswer: '',
        telephone: '',
        email: '',
        role: '',
        inviteCode: ''
      },
      authCode: '',
      teleCode: '',
      loginRules: {
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassord }]
      },
      securityQuestions: '',
      ques: ''
    }
  },
  computed: {},
  mounted() {
    this.form = this.$store.getters.user
    getAllSecurityQuestion().then(
      res => {
        this.securityQuestions = res.data
      }
    )
  },
  methods: {
    updatePassword() {
      updatePassword(this.form).then((res) => {
        Message({
          message: res.msg,
          type: 'success',
          duration: 2000
        })
      })
    },
    getAuthCodeByEmail() {
      askAuthCodeByEmail(this.form.email, this.form.id).then((res) => {
        Message({
          message: res.msg,
          type: 'info',
          duration: 2000
        })
      })
    },
    updateEmail() {
      bindEmail(this.authCode, this.form).then((res) => {
        Message({
          message: res.msg,
          type: 'success',
          duration: 2000
        })
      })
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

