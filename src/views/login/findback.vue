<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title">账号找回</h3>
      </div>

      <el-form-item prop="roles">
        <el-select v-model="loginForm.role" :label="label" style="display: block;" placeholder="身份选择">
          <el-option
            v-for="item in roles"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            @click="loginForm.label=item.label"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="新密码"
          name="password"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.confirmPassword"
          :type="passwordType"
          placeholder="确认新密码"
          name="confirmPassword"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <br>
      <div style="margin-bottom: 30px">
        <el-row justify="center">

          <el-col :span="12" style="text-align: center">
            <el-link :type="findByTelePhone?'primary':'default'" @click="changeType(true)">短信验证码找回</el-link>
          </el-col>

          <el-col :span="12" style="text-align: center">
            <el-link :type="findByTelePhone?'default':'primary'" @click="changeType(false)">密保找回</el-link>
          </el-col>

        </el-row>
        <br>
        <div v-show="findByTelePhone">
          <el-form-item prop="telephone">
            <span class="svg-container">
              <i class="el-icon-phone" />
            </span>
            <el-input
              ref="telephone"
              v-model="loginForm.telephone"
              placeholder="电话号码"
              name="telephone"
              type="text"
              tabindex="1"
              auto-complete="on"
            />
          </el-form-item>

          <el-form-item prop="authCode" style="width:65%;display: inline-block">
            <span class="svg-container">
              <i class="el-icon-chat-square" />
            </span>
            <el-input
              ref="authCode"
              v-model="loginForm.authCode"
              maxlength="6"
              placeholder="验证码"
              name="authCode"
              tabindex="2"
              auto-complete="on"
            />
          </el-form-item>

          <el-button
            :loading="loading"
            type="primary"
            style="width:30%;float: right;margin-top: 10px"
            @click.native.prevent="sendAuthCode"
          >发送验证码
          </el-button>
          <br>
        </div>
        <div v-show="!findByTelePhone">
          <el-form-item prop="question">
            <span class="svg-container">
              <i class="el-icon-question" />
            </span>
            <el-select v-model="loginForm.securityQuestion" :label="ques" placeholder="请选择密保问题" style="width: 90%">
              <el-option
                v-for="question in securityQuestions"
                :key="question.id"
                :label="question.content"
                :value="question.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item prop="authCode">
            <el-input
              ref="authCode"
              v-model="loginForm.securityAnswer"
              placeholder="答案"
              name="answer"
              tabindex="2"
              auto-complete="on"
            />
          </el-form-item>
        </div>

      </div>
      <el-button
        :loading="loading"
        type="primary"
        style="width:48%;margin-bottom:30px;"
        @click.native.prevent="register"
      >找回密码
      </el-button>
      <el-button
        :loading="loading"
        type="default"
        style="width:48%;margin-bottom:30px;"
        @click.native.prevent="goBackLogin"
      >返回登陆
      </el-button>

    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import { findbackByQues, getAllSecurityQuestion } from '@/api/login'
import { Message } from 'element-ui'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不应该小于六位'))
      } else {
        callback()
      }
    }
    const validateConfirmPassord = (rule, value, callback) => {
      if (value !== this.loginForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    const validateRole = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请选择身份'))
        return false
      } else {
        callback()
      }
    }
    return {
      findByTelePhone: true,
      loginForm: {
        username: '',
        password: '',
        telephone: '',
        securityQuestion: '',
        securityAnswer: '',
        role: ''
      },
      loginRules: {
        role: [{ required: true, trigger: 'blur', validator: validateRole }],
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassord }]
      },
      ques: '',
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      label: '',
      securityQuestions: [],
      roles: [
        {
          value: 1001,
          label: '管理员'
        }, {
          value: 1002,
          label: '租户'
        }, {
          value: 1003,
          label: '用户'
        }, {
          value: 1004,
          label: '答者'
        }
      ]
    }
  },
  mounted() {
    getAllSecurityQuestion().then(
      res => {
        this.securityQuestions = res.data
      }
    )
  },
  methods: {
    changeType(flag) {
      this.findByTelePhone = flag
      console.log(this.findByTelePhone)
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    goBackLogin() {
      this.$router.push('/login/')
    },
    register() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          findbackByQues(this.loginForm).then((res) => {
            Message({
              message: res.msg,
              type: 'success',
              duration: 2000
            })
            this.loading = false
            setTimeout(() => {
              this.$router.push('/login')
            }, 1000)
          }).catch(() => {
            this.loading = false
          })
        } else {
          Message({
            message: '请检查数据格式',
            type: 'warning',
            duration: 2000
          })
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #2d3a4b;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  background-image: url("../background/登录背景.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    background: rgba(255,255,255,0.3);
    top: 50px;
    border-radius: 20px;
    padding: 60px 35px 30px;
    position: relative;
    width: 520px;
    max-width: 100%;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 18px;
    color: #79bbff;
    margin-bottom: 10px;
    display: inline-block;
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
