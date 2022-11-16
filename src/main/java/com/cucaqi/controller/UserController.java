package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.User;
import com.cucaqi.service.ILesseeService;
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
    @Autowired
    private ILesseeService iLesseeService;

    /**
     * 根据租户id获取该租户下所有的用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result listUser(@PathVariable int id){
        Result result = new Result();
        if(iLesseeService.getById(id)==null){
            result.setMsg("查询失败--该租户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }
        else {
            try {
                List<User> userlist = iUserService.getUserlist(id);
                result.setData(userlist);
                result.setCode(HTTP.SUCCESS);
                result.setMsg("查询成功");
            }
            catch (Exception e){
                result.setCode(HTTP.NOT_FOUND);
                result.setMsg("查找用户失败--");
            }
        }
        return result;
    }
    @GetMapping("/deleted/{id}")
    public Result listDeletedUser(@PathVariable int id){
        Result result = new Result();
        if(iLesseeService.getById(id)==null){
            result.setMsg("查询失败--该租户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }
        else {
            try {
                List<User> userlist = iUserService.getDeletedUserlist(id);
                result.setData(userlist);
                result.setCode(HTTP.SUCCESS);
                result.setMsg("查询成功");
            }
            catch (Exception e){
                result.setCode(HTTP.NOT_FOUND);
                result.setMsg("查找用户失败--");
            }
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
        if(user==null){
            result.setCode(HTTP.SERVER_ERR);
            result.setMsg("添加失败--添加的用户为空");
        }
        try {
            if(iUserService.searchUser(user.getUsername())) {
                result.setCode(HTTP.SERVER_ERR);
                result.setMsg("用户添加失败--该用户名已存在");
            }
            else {
                boolean save = iUserService.save(user);
                result.setCode(HTTP.SUCCESS);
                result.setMsg("添加成功");
            }

        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("添加用户失败--");
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
        int id=user.getId();
        if(iUserService.getById(id)==null){
            result.setMsg("删除失败--该用户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }
        else {
            try{

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
                result.setMsg("删除用户失败--");

            }
        }

        return  result;
    }
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        Result result=new Result();
        int id=user.getId();
        if(iUserService.getById(id)==null){
            result.setMsg("修改失败--该用户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }

        else {
            try {
                User byId = iUserService.getById(id);
                if(byId.getUsername().equals(user.getUsername()))  {
                    iUserService.updateById(user);
                    result.setCode(HTTP.SUCCESS);
                }
                else {
                    if(iUserService.searchUser(user.getUsername())) {
                        result.setCode(HTTP.SERVER_ERR);
                        result.setMsg("用户修改失败--该用户名已存在");
                    }
                    else {
                        iUserService.updateById(user);
                        result.setCode(HTTP.SUCCESS);
                    }
                }


            }
            catch (Exception e){
                result.setCode(HTTP.NOT_FOUND);
                result.setMsg("用户修改失败--");
            }
        }

        return  result;

    }

}
