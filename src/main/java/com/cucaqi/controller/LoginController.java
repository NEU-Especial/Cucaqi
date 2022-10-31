package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.service.ILoginService;
import com.cucaqi.service.ISecurityQuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cucaqi.constants.REASON.UPDATE_SUCCESS;


/**
 * 手机号码登陆，密码登陆，注册，
 * 手机号密码找回
 * 密保密码找回
 */
@Controller
@RequestMapping("/cucaqi/login")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @Autowired
    private ISecurityQuesService securityQuesService;

    @PostMapping("/")
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

    //获取所有的密保问题
    @GetMapping("/securityQuestions")
    @ResponseBody
    public Result getAllSecurityQuestions() {
        List<SecurityQuestion> list = securityQuesService.list();
        return new Result(HTTP.SUCCESS, list);
    }

    //密保找回密码，检查用户名密码
    @PostMapping("/findbackByQues")
    @ResponseBody
    public Result findBackByQues(@RequestBody String username, String password, int questionId, String answer, int role) {
        int i = loginService.FindBackByQuestion(username, password, role, questionId, answer);
        if (i == UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "密码找回成功");
        } else {
            return new Result(HTTP.NOT_FOUND, "密保错误，找回失败");
        }
    }


}
