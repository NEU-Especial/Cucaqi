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

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cucaqi.constants.REASON.*;


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

    @PostMapping("/password")
    @ResponseBody
    public Result LoginByPassword(@RequestBody BaseUser baseUser) {
        String userName = baseUser.getUsername();
        String password = baseUser.getPassword();
        int role = baseUser.getRole();

        //最终返回jwt字段，标识用户的身份
        Object o = loginService.GetUserByUserNameAndPassword(userName, password, role);
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
    public Result Register(@RequestBody BaseUser user) {
        String userName = user.getUsername();
        String password = user.getPassword();
        int role = user.getRole();

        String inviteCode = user.getInviteCode();
        int res = loginService.InsertUser(userName, password, role, inviteCode);
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
    public Result findBackByQues(@RequestBody BaseUser baseUser) {

        String username = baseUser.getUsername();
        String password = baseUser.getPassword();
        int role = baseUser.getRole();
        int questionId = baseUser.getSecurityQuestion();
        String answer = baseUser.getSecurityAnswer();

        int i = loginService.FindBackByQuestion(username, password, role, questionId, answer);
        if (i == UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "密码找回成功");
        } else {
            return new Result(HTTP.NOT_FOUND, "密保错误，找回失败");
        }
    }

    //获取邮箱验证码
    @GetMapping("/authCode/{email}/{role}")
    @ResponseBody
    public Result getAuthCodeByEmail(@PathVariable String email, @PathVariable int role, HttpSession session) {
        int code = loginService.askAuthCodeByEmail(email, role);
        if (code != SEND_FAIL) {
            //表示发送成功
            session.setAttribute(email + role, code);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    //删除email
                    session.removeAttribute(email + role);
                }
            };
            //实例化这个task任务
            Timer timer = new Timer();
            timer.schedule(task, 180000);//三分钟之后执行task任务
            return new Result(HTTP.SUCCESS, "验证码发送成功");
        }
        return new Result(HTTP.SERVER_ERR, "发送失败");
    }


    //使用邮箱验证码登陆，只需要填写邮箱以及选择角色，找到以后会去找到对应的角色信息
    @PostMapping("/email/{code}")
    @ResponseBody
    public Result LoginByEmail(@RequestBody BaseUser baseUser, @PathVariable String code, HttpSession session) {
        //只提交role，以及email信息
        Object o = session.getAttribute(baseUser.getEmail() + baseUser.getRole());
        if (o == null) {
            return new Result(HTTP.NOT_FOUND, "验证码已失效");
        }
        String rightCode = (String) o;
        if (!rightCode.equals(code)) {
            return new Result(HTTP.NOT_FOUND, "验证码错误");
        }
        //此时需要查询用户
        o = loginService.GetUserByEmail(baseUser.getEmail(), baseUser.getRole());
        if (o == null) {
            //说明查不到
            return new Result(HTTP.NOT_FOUND, "登陆失败,未知错误", null);
        }
        switch (baseUser.getRole()) {
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
        return new Result(HTTP.BAD_REQ, "登陆失败，未知角色");
    }
}
