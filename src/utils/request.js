import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 构造axios实例
const service = axios.create({
  timeout: 5000 // 默认超时时间
})

// 请求拦截器，自动添加token字段
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // 请求头携带token
      config.headers['X-TOKEN'] = getToken()
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    //  自动报错
    if (res.code !== 200) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 2 * 1000
      })
      // 过期信息，对于非法jwt或者多用户登陆
      // 50008: 权限异常需要登出
      if (res.code === 50008) {
        // 尝试重新获取token
        MessageBox.confirm('你的权限异常，请重新尝试', '权限异常', {
          confirmButtonText: '重新尝试',
          cancelButtonText: '登出',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    }
    return res
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
