package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.service.IInfoService;
import com.cucaqi.service.impl.InfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;

import static com.cucaqi.constants.REASON.SEND_FAIL;

/**
 * 密保绑定，信息修改，手机号码绑定
 */
@RestController
@RequestMapping("/cucaqi/info")
public class InfoController {

    @Autowired
    private IInfoService infoService;

    @PostMapping("/bindSecurityQuestion")
    public Result BindSecurityQuestion(@RequestBody BaseUser baseUser) {
        int id = baseUser.getId();
        int role = baseUser.getRole();
        int questionId = baseUser.getSecurityQuestion();
        String answer = baseUser.getSecurityAnswer();

        int i = infoService.BindSecurityQuestion(id, role, questionId, answer);
        if (i == REASON.UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "更新新密保成功");
        } else {
            return new Result(HTTP.BAD_REQ, "更新新密保失败");
        }
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody BaseUser baseUser) {

        int id = baseUser.getId();
        int role = baseUser.getRole();
        String Password = baseUser.getPassword();
        int i = infoService.UpdatePassword(id, role, Password);
        if (i == REASON.UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "更新密码成功");
        } else {
            return new Result(HTTP.BAD_REQ, "更新密保失败");
        }
    }

    //获取验证码信息
    @GetMapping("/email/{email}/{id}")
    public Result getAuthCodeByEmail(@PathVariable String email, HttpSession session, @PathVariable String id) {
        int code = infoService.AskAuthCodeByEmail(email);
        if (code != SEND_FAIL) {
            session.setAttribute(email+"-"+id, code);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    //删除email
                    session.removeAttribute(email);
                }
            };
            //实例化这个task任务
            Timer timer = new Timer();
            timer.schedule(task, 180000);//三分钟之后执行task任务
            return new Result(HTTP.SUCCESS, "验证码发送成功");
        }
        return new Result(HTTP.SERVER_ERR, "发送失败");
    }

    @PostMapping("/email/{code}")
    public Result bindEmail(@RequestBody BaseUser baseUser, HttpSession session, @PathVariable String code) {
        //获取当前的角色信息
        int id = baseUser.getId();
        int role = baseUser.getRole();
        String email=baseUser.getEmail();

        Object o = session.getAttribute(email+"-"+id);
        if (o == null) {
            return new Result(HTTP.NOT_FOUND, "验证码已失效");
        }
        String rightCode = (String) o;
        if (!rightCode.equals(code)) {
            return new Result(HTTP.NOT_FOUND, "验证码错误");
        }
        int res=infoService.BindEmail(id,role,baseUser.getEmail());

        if (res == REASON.UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "更新新邮箱成功");
        } else {
            return new Result(HTTP.BAD_REQ, "更新新邮箱失败");
        }
    }


}
