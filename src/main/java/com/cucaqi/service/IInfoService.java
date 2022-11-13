package com.cucaqi.service;

//个人信息维护服务，提供密保绑定，手机号码绑定
public interface IInfoService {

    //绑定密保
    int BindSecurityQuestion(int id, int role, int questionId, String answer);


    //更新用户信息，不允许更新用户名
    int UpdatePassword(int id, int role, String Password);

    //请求绑定用户信息
    int BindTelephone(int id, int role, String Telephone);

    //请求发送手机验证码
    String AskAuthCodeByTelephone(int id, String Telephone);

    //请求发送邮箱验证码
    int AskAuthCodeByEmail(String email);

    int BindEmail(int id, int role, String Email);


    int updateInviteCode(int id, int role);
}
