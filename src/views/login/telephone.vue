<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">验证码登陆</h3>
      </div>

      <el-form-item prop="telephone">
        <span class="svg-container">
          <i class="el-icon-phone"></i>
        </span>
        <el-input
          ref="telephone"
          v-model="loginForm.telephone"
          placeholder="Telephone Number"
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
          placeholder="AuthCode"
          name="authCode"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <el-button :loading="loading" type="primary" style="width:30%;float: right;margin-top: 10px" @click.native.prevent="sendAuthCode">发送验证码</el-button>
      <br>
      <el-button :loading="loading" type="primary" style="width:48%;margin-bottom:30px;" @click.native.prevent="handleLogin">短信登陆</el-button>
      <el-button :loading="loading" type="default" style="width:48%;margin-bottom:30px;float: right" @click.native.prevent="handleLoginBypassword">密码登陆</el-button>
      <div class="tips" style="margin-left: 10px">
        <router-link to="/login/register">注册</router-link>
      </div>
      <div class="tips" style="float:right;margin-right: 10px">
        <router-link to="/login/findback">找回密码</router-link>
      </div>

    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    // 校验电话号码
    const validateTelephone = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback()
      } else {
        callback()
      }
    }
    // 校验验证码
    const validateAuthCode = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('验证码不应小于六位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        telephone: 'admin',
        authCode: '111111'
      },
      loginRules: {
        telephone: [{ required: true, trigger: 'blur', validator: validateTelephone }],
        authCode: [{ required: true, trigger: 'blur', validator: validateAuthCode }]
      },
      loading: false,
      redirect: undefined
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    handleLoginBypassword() {
      this.$router.replace({ path: '/login/' })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:pink;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
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
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
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
