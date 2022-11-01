package com.cucaqi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.Admin;
import com.cucaqi.entity.Answerer;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.User;
import com.cucaqi.mapper.AdminMapper;
import com.cucaqi.mapper.AnswererMapper;
import com.cucaqi.mapper.LesseeMapper;
import com.cucaqi.mapper.UserMapper;
import com.cucaqi.service.ILoginService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ses.v20201002.SesClient;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.ses.v20201002.models.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;


@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private LesseeMapper lesseeMapper;

    @Autowired
    private AnswererMapper answererMapper;

    @Override
    //不同的角色需要执行不同的登陆方法
    public Object GetUserByUserNameAndPassword(String userName, String Password, int role) {
        switch (role) {
            case ROLE.ADMIN:
                return adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            case ROLE.LESSEE:
                return lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            case ROLE.USER:
                return userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            case ROLE.ANSWERER:
                return answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            default:
                return null;
        }
    }

    @Override
    //注册方法，需要先判断重复用户名，之后再执行登陆操作，管理员不允许注册,0表示重复，-1表示邀请码无效
    public int InsertUser(String userName, String password, int role, String inviteCode) {
        switch (role) {
            case ROLE.LESSEE:
                Lessee lessee = new Lessee();
                Lessee l = lesseeMapper.selectOne(new QueryWrapper<Lessee>().eq("username", userName));
                if (l != null) {
                    return REASON.DUP_USERNAME;
                }
                //检查邀请码
                val admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("inviteCode", inviteCode));
                if (admin == null) {
                    return REASON.WRONG_CODE;
                }
                lessee.setCreatedBy(admin.getId());
                lessee.setUsername(userName);
                lessee.setPassword(password);
                lessee.setRole(role);
                return lesseeMapper.insert(lessee);
            case ROLE.USER:
                User user = new User();
                val u = userMapper.selectOne(new QueryWrapper<User>().eq("username", userName));
                if (u != null) {
                    return REASON.DUP_USERNAME;
                }
                lessee = lesseeMapper.selectOne(new QueryWrapper<Lessee>().eq("inviteCode", inviteCode));
                if (lessee == null) {
                    return REASON.WRONG_CODE;
                }
                user.setUsername(userName);
                user.setPassword(password);
                user.setRole(role);
                user.setCreatedBy(lessee.getId());
                return userMapper.insert(user);
            case ROLE.ANSWERER:
                val a = answererMapper.selectOne(new QueryWrapper<Answerer>().eq("username", userName));
                if (a != null) {
                    return REASON.DUP_USERNAME;
                }
                Answerer answerer = new Answerer();
                answerer.setUsername(userName);
                answerer.setPassword(password);
                answerer.setRole(role);
                return answererMapper.insert(answerer);
        }
        return REASON.UNKNOWN_ROLE;
    }


    @Override
    public int FindBackByQuestion(String userName, String password, int role, int questionId, String answer) {
        switch (role) {
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("username", userName).eq("securityQuestion", questionId).eq("securityAnswer", answer);
                }));
                if (admin == null) {
                    return REASON.WRONG_ANSWER;
                }
                admin.setPassword(password);
                return adminMapper.updateById(admin);

            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("username", userName).eq("securityQuestion", questionId).eq("securityAnswer", answer);
                }));
                if (lessee == null) {
                    return REASON.WRONG_ANSWER;
                }
                lessee.setPassword(password);
                return lesseeMapper.updateById(lessee);

            case ROLE.USER:
                User user = userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("username", userName).eq("securityQuestion", questionId).eq("securityAnswer", answer);
                }));
                if (user == null) {
                    return REASON.WRONG_ANSWER;
                }
                user.setPassword(password);
                return userMapper.updateById(user);

            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("username", userName).eq("securityQuestion", questionId).eq("securityAnswer", answer);
                }));
                answerer.setPassword(password);
                return answererMapper.updateById(answerer);
            default:
                return REASON.UNKNOWN_ROLE;
        }
    }

    //检查是否有绑定该邮箱号，如果绑定了则发送邮件，方法返回值为验证码
    @Override
    public int askAuthCodeByEmail(String email, int role) {
        switch (role) {
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("email", email);
                }));
                if (admin == null) {
                    return REASON.WRONG_EMAIL;
                }
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("email", email);
                }));
                if (lessee == null) {
                    return REASON.WRONG_EMAIL;
                }
            case ROLE.USER:
                User user = userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("email", email);
                }));
                if (user == null) {
                    return REASON.WRONG_EMAIL;
                }
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("email", email);
                }));
                if (answerer == null) {
                    return REASON.WRONG_EMAIL;
                }
        }
        int authCode = 100001 + new Random().nextInt(888888);
        //否则就往该邮箱发送邮件验证码
        boolean success = SenEmail(email, authCode);
        if (success) {
            return authCode;
        }
        return REASON.SEND_FAIL;
    }

    @Override
    public Object GetUserByEmail(String email, int role) {
        switch (role) {
            case ROLE.ADMIN:
                return adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("email", email);
                }));
            case ROLE.LESSEE:
                return lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("email", email);
                }));
            case ROLE.USER:
                return userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("email", email);
                }));
            case ROLE.ANSWERER:
                return answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("email", email);
                }));
            default:
                return null;
        }
    }

    //邮件验证码发送方法
    @Override
    public boolean SenEmail(String EmailAddress, int authCode) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("AKIDymKcePs40f3A0Zrsc5GAnKaZHjtmPlrg", "Zrtf9MIxLSIsBDyTgcHhqiJiqgFXZKnz");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ses.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SesClient client = new SesClient(cred, "ap-hongkong", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendEmailRequest req = new SendEmailRequest();
            req.setFromEmailAddress("cucaqi@xugaosong.xyz");
            String[] destination1 = {EmailAddress};
            req.setDestination(destination1);
            Template template1 = new Template();
            template1.setTemplateID(50027L);
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("authCode", String.valueOf(authCode));
            template1.setTemplateData(JSON.toJSONString(stringStringHashMap));
            req.setTemplate(template1);
            req.setSubject("邮箱验证码");
            // 返回的resp是一个SendEmailResponse的实例，与请求对象对应
            SendEmailResponse resp = client.SendEmail(req);
            // 输出json格式的字符串回包
            System.out.println(SendEmailResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }
}

