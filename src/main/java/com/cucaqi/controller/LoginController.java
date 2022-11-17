package com.cucaqi.controller;

import com.cucaqi.controller.constants.HTTP;
import com.cucaqi.controller.constants.REASON;
import com.cucaqi.controller.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.service.ILoginService;
import com.cucaqi.service.ISecurityQuesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.cucaqi.controller.constants.REASON.*;


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
                admin.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, admin);
            case ROLE.LESSEE:
                Lessee lessee = (Lessee) o;
                lessee.setPassword("");
                lessee.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, lessee);
            case ROLE.USER:
                User user = (User) o;
                user.setPassword("");
                user.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, user);
            case ROLE.ANSWERER:
                Answerer answerer = (Answerer) o;
                answerer.setPassword("");
                answerer.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, answerer);
        }
        return new Result(HTTP.NOT_FOUND, "登陆失败,未知身份", null);
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
    //手机号找回密码，检查用户名密码
    @PostMapping("/findbackByTel/{code}")
    @ResponseBody
    public Result findBackByTel(@RequestBody BaseUser baseUser,@PathVariable Integer code,HttpSession session) {

        Object o = session.getAttribute(baseUser.getTelephone() + baseUser.getRole());
        if (o == null) {
            return new Result(HTTP.NOT_FOUND, "验证码无效");
        }
        Integer rightCode = (Integer) o;
        if (!Objects.equals(code, rightCode)) {
            return new Result(HTTP.NOT_FOUND, "验证码错误");
        }
        session.removeAttribute(baseUser.getTelephone() + baseUser.getRole());
        //此时需要查询用户
        o = loginService.GetUserByTelephone(baseUser.getTelephone(), baseUser.getRole());
        if (o == null) {
            //说明查不到该用户
            return new Result(HTTP.NOT_FOUND, "找回失败,手机号不存在", null);
        }

        String username = baseUser.getUsername();
        String password = baseUser.getPassword();
        int role = baseUser.getRole();
        int i = loginService.FindBackByTelephone(username, password, role, o);
        if (i == UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "密码找回成功");
        } else {
            return new Result(HTTP.NOT_FOUND, "用户名或手机号错误，找回失败");
        }
    }
    //获取邮箱验证码
//    @GetMapping("/authCode/{role}")
//    @ResponseBody
//    public Result getAuthCodeByEmail(@Param("email") String email, @PathVariable int role, HttpSession session) {
//
//        int code = loginService.askAuthCodeByEmail(email, role);
//        if (code != SEND_FAIL && code != WRONG_EMAIL) {
//            //表示发送成功
//            session.setAttribute(email + role, code);
//            System.out.println("set  " + email + role);
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    //删除email
//                    System.out.println("remove");
//                    session.removeAttribute(email + role);
//                }
//            };
//            //实例化这个task任务
//            Timer timer = new Timer();
//            timer.schedule(task, 180000);//三分钟之后执行task任务
//            return new Result(HTTP.SUCCESS, "验证码发送成功");
//        } else if (code == WRONG_EMAIL) {
//            return new Result(HTTP.NOT_FOUND, "邮箱号码不存在");
//        }
//        return new Result(HTTP.SERVER_ERR, "发送失败");
//    }
//
//
//    //使用邮箱验证码登陆，只需要填写邮箱以及选择角色，找到以后会去找到对应的角色信息
//    @PostMapping("/email/{code}")
//    @ResponseBody
//    public Result LoginByEmail(@RequestBody BaseUser baseUser, @PathVariable int code, HttpSession session) {
//
//        Object o = session.getAttribute(baseUser.getEmail() + baseUser.getRole());
//
//
//        if (o == null) {
//            return new Result(HTTP.NOT_FOUND, "验证码无效");
//        }
//        Integer rightCode = (Integer) o;
//
//        if (code!=rightCode) {
//            return new Result(HTTP.NOT_FOUND, "验证码无效");
//        }
//        session.removeAttribute(baseUser.getEmail() + baseUser.getRole());
//        //此时需要查询用户
//        o = loginService.GetUserByEmail(baseUser.getEmail(), baseUser.getRole());
//        if (o == null) {
//            //说明查不到
//            return new Result(HTTP.NOT_FOUND, "登陆失败,未知错误", null);
//        }
//        switch (baseUser.getRole()) {
//            case ROLE.ADMIN:
//                Admin admin = (Admin) o;
//                admin.setPassword("");
//                admin.setSecurityAnswer("");
//                return new Result(HTTP.SUCCESS, admin);
//            case ROLE.LESSEE:
//                Lessee lessee = (Lessee) o;
//                lessee.setPassword("");
//                lessee.setSecurityAnswer("");
//
//                return new Result(HTTP.SUCCESS, lessee);
//            case ROLE.USER:
//                User user = (User) o;
//                user.setPassword("");
//                user.setSecurityAnswer("");
//                return new Result(HTTP.SUCCESS, user);
//            case ROLE.ANSWERER:
//                Answerer answerer = (Answerer) o;
//                answerer.setPassword("");
//                answerer.setSecurityAnswer("");
//                return new Result(HTTP.SUCCESS, answerer);
//        }
//        return new Result(HTTP.BAD_REQ, "登陆失败，未知角色");
//    }

    //获取短信验证码
    @GetMapping("/authCode/{role}")
    @ResponseBody
    public Result getAuthCodeByTelephone(@Param("telephone") String telephone, @PathVariable int role, HttpSession session) {

        int code = loginService.askAuthCodeByTelephone(telephone, role);
        if (code != SEND_FAIL && code != WRONG_EMAIL) {
            //表示发送成功
            session.setAttribute(telephone + role, code);
            System.out.println("set  " + telephone + role);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    //删除telephone
                    System.out.println("remove");
                    session.removeAttribute(telephone + role);
                }
            };
            //实例化这个task任务
            Timer timer = new Timer();
            timer.schedule(task, 180000);//三分钟之后执行task任务
            return new Result(HTTP.SUCCESS, "验证码发送成功");
        } else if (code == WRONG_EMAIL) {
            return new Result(HTTP.NOT_FOUND, "电话号码不存在");
        }
        return new Result(HTTP.SERVER_ERR, "发送失败");
    }


    //使用短信验证码登陆，只需要填写手机号以及选择角色，找到以后会去找到对应的角色信息
    @PostMapping("/telephone/{code}")
    @ResponseBody
    public Result LoginByTelephone(@RequestBody BaseUser baseUser, @PathVariable int code, HttpSession session) {

        Object o = session.getAttribute(baseUser.getTelephone() + baseUser.getRole());
        if (o == null) {
            return new Result(HTTP.NOT_FOUND, "验证码无效");
        }
        Integer rightCode = (Integer) o;

        if (code!=rightCode) {
            return new Result(HTTP.NOT_FOUND, "验证码无效");
        }
        session.removeAttribute(baseUser.getTelephone() + baseUser.getRole());
        //此时需要查询用户
        o = loginService.GetUserByTelephone(baseUser.getTelephone(), baseUser.getRole());
        if (o == null) {
            //说明查不到
            return new Result(HTTP.NOT_FOUND, "登陆失败,未知错误", null);
        }
        switch (baseUser.getRole()) {
            case ROLE.ADMIN:
                Admin admin = (Admin) o;
                admin.setPassword("");
                admin.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, admin);
            case ROLE.LESSEE:
                Lessee lessee = (Lessee) o;
                lessee.setPassword("");
                lessee.setSecurityAnswer("");

                return new Result(HTTP.SUCCESS, lessee);
            case ROLE.USER:
                User user = (User) o;
                user.setPassword("");
                user.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, user);
            case ROLE.ANSWERER:
                Answerer answerer = (Answerer) o;
                answerer.setPassword("");
                answerer.setSecurityAnswer("");
                return new Result(HTTP.SUCCESS, answerer);
        }
        return new Result(HTTP.BAD_REQ, "登陆失败，未知角色");
    }
}
