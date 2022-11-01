<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      auto-complete="on"
      class="login-form"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title">Cucaqi</h3>
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
          name="username"
          placeholder="Username"
          tabindex="1"
          type="text"
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
          auto-complete="on"
          name="password"
          placeholder="Password"
          tabindex="2"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <br>
      <el-button
        :loading="loading"
        style="width:48%;margin-bottom:30px;"
        type="primary"
        @click.native.prevent="handleLogin"
      >密码登陆
      </el-button>

      <el-button
        :loading="loading"
        style="width:48%;margin-bottom:30px;float: right"
        type="default"
        @click.native.prevent="handleTelephoneLogin"
      >验证码登陆
      </el-button>

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
import { loginByPassword } from '@/api/login'
import { Message } from 'element-ui'

export default {
  name: 'LoginByPassword',
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
    return {
      loginForm: {
        username: 'admin',
        password: '123456',
        role: 1001
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      label: '管理员',
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
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
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
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) { // 校验成功登陆
          this.loading = true
          loginByPassword(this.loginForm).then(res => {
            this.loading = false
            // 需要把用户信息进行保存，之后随时取用
            this.$store.commit('user/SetUser', res.data)
            Message({
              message: '登陆成功',
              type: 'success',
              duration: 2 * 1000
            })
            setTimeout(() => {
              this.$router.push('/dashboard')
            }, 1 * 1000)
          }).catch(
            this.loading = false
          )
        } else {
          console.log('请输入合法校验信息')
          return false
        }
      })
    },
    handleTelephoneLogin() {
      this.$router.replace({ path: '/login/telephone' })
    }
  }
}
</script>

<style lang="scss">
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
