import axios from "axios"
import {ElLoading} from "element-ui"
import {ElMessage} from "element-ui"
//使用create创建axios实例
let loadingObj = null;
const UmService = axios.create({
  timeout:8000,
  //这里填后端协议地址
  baseURL:"http://XXX",
  headers:{
    "content-type":"application/json;charset=utf-8"
  }
})
//请求拦截-增加loading，对请求做统一处理
UmService.interceptors.request.use(config=>{
  loadingObj = ElLoading.service({
    lock:true,
    text:'Loading',
    background:'rgba(0,0,0,0.7)',
  })
  return config
})
//响应拦截-对返回值做统一处理
UmService.interceptors.response.use(response=>{
  loadingObj.close()
  return response.data
},error => {
  loadingObj.close()
  ElMessage({
    message:"服务器错误",
    type:"error",
    duration:2000
  })
})
//post请求
export const post=config=>{
  return UmService({
    ...config,
    method:"post",
    data:config.data
  })
}
//get请求
export const get=config=>{
  return UmService({
    ...config,
    method:"get",
    data:config.data
  })
}
//get请求
