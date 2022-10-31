package com.cucaqi.service;


/**
 * 登陆相关的服务方法
 */

public interface ILoginService {

    //使用账号密码登陆
    Object LoginByPassword(String userName, String Password, int role);




}
