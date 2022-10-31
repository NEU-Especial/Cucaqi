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
        int id =baseUser.getId();
        int role=baseUser.getRole();
        int questionId=baseUser.getSecurityQuestion();
        String answer=baseUser.getSecurityAnswer();

        int i = infoService.BindSecurityQuestion(id, role, questionId, answer);
        if (i == REASON.UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "更新新密保成功");
        } else {
            return new Result(HTTP.BAD_REQ, "更新新密保失败");
        }
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody BaseUser baseUser) {

        int id =baseUser.getId();
        int role=baseUser.getRole();
        String Password = baseUser.getPassword();
        int i = infoService.UpdatePassword(id, role, Password);
        if (i == REASON.UPDATE_SUCCESS) {
            return new Result(HTTP.SUCCESS, "更新密码成功");
        } else {
            return new Result(HTTP.BAD_REQ, "更新密保失败");
        }
    }



}
