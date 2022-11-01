package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.User;
import com.cucaqi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;

import static com.cucaqi.controller.LesseeController.exist;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/cucaqi/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 根据租户id获取该租户下所有的用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result listUser(@PathVariable int id){
        Result result = new Result();
        try {
            List<User> userlist = iUserService.getUserlist(id);
            result.setData(userlist);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("查询成功");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("查找用户失败--"+e.getLocalizedMessage());


}
        return result;
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        Result result =new Result();

        try {
            boolean save = iUserService.save(user);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("添加成功");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("添加用户失败--"+e.getLocalizedMessage());
        }

        return result;


    }

    /**
     * 删除用户
     * @param user
     * @return
     */
    @DeleteMapping("/deleteUser")
    public Result deleteUser(@RequestBody User user){
        Result result=new Result();
        try{
            int id=user.getId();
            Integer res = iUserService.deleteUser(id);
            if (res==exist){
                result.setMsg("删除失败--该用户不是无数据关联用户");
                result.setCode(HTTP.SERVER_ERR);
            }
            else {
                result.setCode(HTTP.SUCCESS);
                result.setMsg("删除成功");
            }
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("删除用户失败--"+e.getLocalizedMessage());

        }
        return  result;
    }

}
