package com.cucaqi;

import com.cucaqi.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;

public class emailTest {

    @Test
    public void TestSenEmail(){
        LoginServiceImpl loginService = new LoginServiceImpl();
        //loginService.SenEmail("1150840779@qq.com",123);
        loginService.SenTelephone("18732038506",123);
    }

}
