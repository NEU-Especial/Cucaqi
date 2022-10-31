package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.User;
import com.cucaqi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            result.setMsg("查找用户失败");


}
        return result;
    }


}
