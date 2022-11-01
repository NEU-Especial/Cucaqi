package com.cucaqi.service;


/**
 * 登陆相关的服务方法
 */

public interface ILoginService {


    //使用账号密码登陆
    Object GetUserByUserNameAndPassword(String userName, String Password, int role);


    //邀请码注册
    int InsertUser(String userName, String Password, int role, String inviteCode);

    //密保找回密码
    int FindBackByQuestion(String userName, String Password, int role,int questionId,String answer);


    int askAuthCodeByEmail(String email,int role);


     boolean SenEmail(String EmailAddress, int authCode);

    Object GetUserByEmail(String email, int role);

}
