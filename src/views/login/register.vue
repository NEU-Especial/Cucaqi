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
        <h3 class="title">注册账号</h3>
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
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
        </span>
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.confirmPassword"
          :type="passwordType"
          placeholder="确认密码"
          name="confirmPassword"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
        </span>
      </el-form-item>

      <el-form-item prop="inviteCode">
        <span class="svg-container">
          <svg-icon icon-class="star"/>
        </span>
        <el-input
          ref="password"
          v-model="loginForm.inviteCode"
          :placeholder="placeholder"
          name="inviteCode"
          tabindex="2"
          auto-complete="on"
        />
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width:48%;margin-bottom:30px;"
        @click.native.prevent="register"
      >注册
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
import { Message } from 'element-ui'
import { register } from '@/api/login'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      callback()
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
    const validateInviteCode = (rule, value, callback) => {
      if (value === '' && this.loginForm.role !== 1004) {
        callback(new Error('验证码必填'))
        return false
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 1002,
        inviteCode: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassord }],
        inviteCode: [{ required: false, trigger: 'blur', validator: validateInviteCode }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      label: '租户',
      roles: [
        {
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
  computed: {
    placeholder() {
      if (this.loginForm.role !== 1004) {
        return '邀请码(必填)'
      } else {
        return '邀请码(选填)'
      }
    }
  },
  methods: {
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
          register(this.loginForm).then((res) => {
            Message({
              message: res.msg,
              type: 'success',
              duration: 1000
            })
            this.loading = false
            this.$store.commit('user/SetUser', { username: this.loginForm.username, role: this.loginForm.role })
            setTimeout(() => {
              this.$router.push('/login')
            }, 2 * 1000)
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
    top: 120px;
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
