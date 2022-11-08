package com.cucaqi.service;


/**
 * 登陆相关的服务方法
 */

public interface ILoginService {


    //使用账号密码登陆
    Object LoginByPassword(String userName, String Password, int role);


    //邀请码注册
    int  Register(String userName, String Password, int role,String inviteCode);

    //密保找回密码
    int FindBackByQuestion(String userName, String Password, int role,int questionId,String answer);

}
