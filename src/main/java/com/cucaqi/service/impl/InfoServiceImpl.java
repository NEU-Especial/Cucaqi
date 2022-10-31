package com.cucaqi.service.impl;

import com.cucaqi.entity.SecurityQuestion;
import com.cucaqi.service.IInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人信息服务，提供密码更新，更改密码，绑定密保，绑定手机号服务
 */

@Service
public class InfoServiceImpl implements IInfoService {


    @Override
    public int BindSecurityQuestion(int id, int role, int questionId, String answer) {
        return 0;
    }

    @Override
    public List<SecurityQuestion> GetAllSecurityQuestion() {

        return null;
    }

    @Override
    public int UpdatePassword(int id, int role, String Password) {
        return 0;
    }

    @Override
    public int BindTelephone(int id, int role, String Telephone, String authCode) {
        return 0;
    }

    @Override
    public String AskAuthCode(int id, String Telephone) {
        return null;
    }
}
