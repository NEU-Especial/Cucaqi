import {post,get} from "./umService"

export const UMApi=data=>{
  return post({
    url:"/user",
    data
  })
}
