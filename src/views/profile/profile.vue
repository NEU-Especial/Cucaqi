
<!--  创建问卷表单-->
<template>

  <div>
    <transition name="fade">

      <el-container style="height: 1200px">
        <el-aside width="50%" style="height:100%;margin: 40px">

          <el-form ref="form" :model="form" label-width="80px" >
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
              <el-input :disabled="true" :value="form.username"/>
            </el-form-item>
<!--            性别-->
            <el-form-item label="性别" label-width="120px"  align="left">
              <el-select v-model="genderType" placeholder="请选择" >
                <el-option v-for="item in genderTypes" :key="item.id" :label="item.value" :value="item.value">
<!--                  <span style="float: left;" v-if="item.key=='1'">-->
<!--                       <i class="el-icon-male"></i>  &lt;!&ndash; 如果动态显示图标，这里添加判断 &ndash;&gt;-->
<!--                  </span>-->
<!--                  <span style="padding-left: 6px;">{{ item.label }}</span>-->
                </el-option>
              </el-select>
            </el-form-item>
<!--职业-->
            <el-form-item label="职业" label-width="120px">
              <el-col :span="8" align="center">
                <el-input placeholder="请输入您的职业"
                          clearable
                          v-model="form.j"
                          prefix-icon="el-icon-user-solid"
                          type="primary"/>
              </el-col>
            </el-form-item>
<!--            年龄-->
            <el-form-item label="年龄" label-width="120px">
              <el-col :span="8" align="center">
              <el-input placeholder="请输入年龄"
                        clearable
                        v-model="form.age"
                        prefix-icon="el-icon-position"
                        type="primary"/>
              </el-col>
            </el-form-item>
<!--            下面利用折叠面板进行页面简化-->
            <el-collapse v-model="activeName" accordion>
          <!--修改密码区域-->
              <el-collapse-item name="1" >
                <template slot="title">
                  <i class="icon el-icon-lock"></i>
                  <span class="title">密码修改</span>
                </template>
            <div style="border: solid 1px #e3e3e9;border-radius: 10px;
            margin-left: 40px;box-shadow: 5px 5px 5px rgba(0.1,0.1,0.1,0.1)">
              <el-form-item label="新密码" label-width="120px">
                <el-input placeholder="请输入8-16位，且由数字，字母构成的密码"
                          v-model="form.password" show-password
                          prefix-icon="el-icon-key"
                          type="password"/>
              </el-form-item>

              <el-form-item label="确认密码" label-width="120px">
                <el-input placeholder="确认密码与上述一致"
                          v-model="form.confirmPassword" show-password type="password"
                          prefix-icon="el-icon-key"/>
              </el-form-item>

              <el-form-item style="margin-top: 20px">
                <el-button type="primary" style="margin-left: 30px" @click="updatePassword">更新密码</el-button>
              </el-form-item>
            </div>
              </el-collapse-item>
<!--            邮箱区域-->
              <el-collapse-item name="2">
                <template slot="title">
                  <i class="icon el-icon-message"></i>
                  <span class="title">邮箱修改</span>
                </template>
            <div style="border: solid 1px #e3e3e9;border-radius: 10px;
            margin-left: 40px;box-shadow: 5px 5px 5px rgba(0,0,0,0.1)">
            <el-form-item label="邮箱地址" label-width="120px">
              <el-col :span="16" align="center">
                <el-input v-model="form.email"
                          placeholder="输入新的邮箱地址"
                          prefix-icon="el-icon-s-promotion"
                          clearable/>
              </el-col>
              <el-col :span="8" align="right">
                <el-button type="primary" @click="getAuthCodeByEmail">发送验证码</el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="邮箱验证码" label-width="120px">
              <el-col :span="8" align="center">
                <el-input v-model="authCode"
                          placeholder="验证码"
                          prefix-icon="el-icon-s-help"
                          clearable/>
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right" @click="updateEmail">更新邮箱地址</el-button>
            </el-form-item>
            </div>
              </el-collapse-item>
<!--            更改手机号-->
              <el-collapse-item  name="3">
                <template slot="title">
                  <i class="icon el-icon-mobile-phone"></i>
                  <span class="title">手机号修改</span>
                </template>
            <div style="border: solid 1px #e3e3e9;border-radius: 10px;
            margin-left: 40px;box-shadow: 5px 5px 5px rgba(0.1,0.1,0.1,0.1)">
            <el-form-item label="绑定手机号码" label-width="120px" >
              <el-col :span="16" align="center">
                <el-input v-model="form.telephone"
                          placeholder="输入电话号码"
                          prefix-icon="el-icon-phone"
                          clearable/>
              </el-col>
              <el-col :span="8" align="right">
                <el-button type="primary">发送验证码</el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="短信验证码" label-width="120px">
              <el-col :span="8" align="center">
                <el-input v-model="authCode"
                          placeholder="短信验证码"
                          prefix-icon="el-icon-s-comment"
                          clearable/>
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right">更新电话号码</el-button>
            </el-form-item>
            </div>
              </el-collapse-item>
            <!--            密保区域-->
              <el-collapse-item  name="4">
                <template slot="title">
                  <i class="icon el-icon-copy-document"></i>
                  <span class="title">密保修改</span>
                </template>
            <div style="border: solid 1px #e3e3e9;border-radius: 10px;
            margin-left: 40px;box-shadow: 5px 5px 5px rgba(0.1,0.1,0.1,0.1)">
            <el-form-item label="选择密保问题" label-width="120px"  align="left">
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
                <el-input v-model="form.securityAnswer"  :disabled="''==form.securityQuestion"
                          prefix-icon="el-icon-s-opportunity"
                          clearable/>
              </el-col>
              <el-button type="primary" style="margin-left: 30px;float: right" @click="bindSecurityQuestion">更新密保
              </el-button>
            </el-form-item>
            </div>
              </el-collapse-item>
            </el-collapse>
            <!--            折叠面板末尾-->
            <el-form-item v-show="show" label="邀请码" label-width="120px">
              <span style="font-size: 14px">{{ form.inviteCode }}</span>
            </el-form-item>

            <el-form-item v-show="show" style="margin-top: 20px">
              <el-button type="primary" style="margin-left: 30px" @click="CreateInviteCode">生成新邀请码</el-button>
            </el-form-item>

          </el-form>
        </el-aside>
        <!--    这里放预览表单-->
      </el-container>
    </transition>
  </div>
</template>

<script>

import { askAuthCodeByEmail, bindEmail, BindSecurityQuestion, inviteCode, updatePassword } from '@/api/info'
import { Message } from 'element-ui'
import { Border} from 'element-ui'
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
        inviteCode: '',
        age:'',

      },
      activeName: '1',
      genderType: '',
      genderTypes: [{
        value: '男',
        id: '1',
        icon:'el-icon-male'
      }, {
        value: '女',
        id: '2',
        icon:'el-icon-male'
      }],
      authCode: '',
      teleCode: '',
      loginRules: {
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassord }]
      },
      securityQuestions: '',
      ques: '',
      note: {
        backgroundImage: "url(" + require("../background/个人信息修改背景.jpg") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "contain",
        marginTop: "5px",
      },

    }
  },
  computed: {
    show() {
      return this.form.role !== 1004
    }
  },
  created() {
    this.form = { ...this.$store.getters.user }
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
        this.$store.commit('user/UpdateEmail', this.form.email)
      })
    },
    bindSecurityQuestion() {
      BindSecurityQuestion(this.form).then((res) => {
        Message({
          message: res.msg,
          type: 'success',
          duration: 2000
        })
        this.$store.commit('user/UpdateQuestion', this.form.securityQuestion)
      })
    },
    CreateInviteCode: function() {
      inviteCode({ id: this.form.id, role: this.form.role }).then((res) => {
        Message({
          message: res.msg,
          type: 'success',
          duration: 2000
        })
        this.form.inviteCode = res.data
        this.$store.commit('user/UpdateInviteCode', res.data)
      })
    }
  }
}
</script>

<style scoped>
.title {
  font-size: 17px;
  /*font-weight: bold;*/
  color: #000000;
  padding-bottom: 17px;
  padding-top: 17px;
}
.icon{
  /*font-weight: bold;*/
  font-size: 19px;
  padding-bottom: 17px;
  padding-top: 17px;
}
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

