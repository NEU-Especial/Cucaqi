package com.cucaqi.service;

import com.cucaqi.entity.SecurityQuestion;

import java.util.List;

//个人信息维护服务，提供密保绑定，手机号码绑定
public interface IInfoService {

    //绑定密保
    int BindSecurityQuestion(int id, int role, int questionId, String answer);

    //获取所有密保
    List<SecurityQuestion> GetAllSecurityQuestion();

    //更新用户信息，不允许更新用户名
    int UpdatePassword(int id, int role, String Password);

    //请求绑定用户信息
    int BindTelephone(int id, int role, String Telephone, String authCode);

    //请求发送收集验证码
    String AskAuthCode(int id, String Telephone);

}
