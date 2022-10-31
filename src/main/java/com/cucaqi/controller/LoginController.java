package com.cucaqi.controller;


import com.cucaqi.constants.HTTP;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cucaqi/login")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/byPassword")
    @ResponseBody
    public Result LoginByPassword(@RequestBody String userName, String password, int role) {
        //最终返回jwt字段，标识用户的身份
        Object o = loginService.LoginByPassword(userName, password, role);
        if (o == null) {
            //说明查不到
            return new Result(HTTP.NOT_FOUND, "登陆失败,用户名密码错误", null);
        }
        switch (role) {
            case ROLE.ADMIN:
                Admin admin = (Admin) o;
                admin.setPassword("");
                return new Result(HTTP.SUCCESS, admin);
            case ROLE.LESSEE:
                Lessee lessee = (Lessee) o;
                lessee.setPassword("");
                return new Result(HTTP.SUCCESS, lessee);
            case ROLE.USER:
                User user = (User) o;
                user.setPassword("");
                return new Result(HTTP.SUCCESS, user);
            case ROLE.ANSWERER:
                Answerer answerer = (Answerer) o;
                answerer.setPassword("");
                return new Result(HTTP.SUCCESS, answerer);
        }
        return null;
    }

    @PostMapping("/register")
    @ResponseBody
    public Result Register(@RequestBody String userName, String password, int role, String inviteCode) {
        int res = loginService.Register(userName, password, role, inviteCode);
        switch (res) {
            case 1:
                return new Result(HTTP.SUCCESS, "注册成功");
            case REASON.DUP_USERNAME:
                return new Result(HTTP.BAD_REQ, "注册失败,用户名重复");
            case REASON.WRONG_CODE:
                return new Result(HTTP.BAD_REQ, "注册失败,邀请码无效");
            case REASON.UNKNOWN_ROLE:
                return new Result(HTTP.BAD_REQ, "注册失败,未知角色");
        }
        return new Result(HTTP.BAD_REQ, "注册失败,未知错误");
    }



}
