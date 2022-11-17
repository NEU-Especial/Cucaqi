package com.cucaqi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cucaqi.controller.constants.HTTP;
import com.cucaqi.controller.constants.REASON;
import com.cucaqi.controller.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.mapper.AdminMapper;
import com.cucaqi.mapper.AnswererMapper;
import com.cucaqi.mapper.LesseeMapper;
import com.cucaqi.mapper.UserMapper;
import com.cucaqi.service.ILoginService;
import com.cucaqi.utils.HttpUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ses.v20201002.SesClient;
import lombok.val;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.ses.v20201002.models.*;

import java.util.HashMap;
import java.util.Map;
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
                if (answerer == null) {
                    return REASON.WRONG_ANSWER;
                }
                answerer.setPassword(password);
                return answererMapper.updateById(answerer);
            default:
                return REASON.UNKNOWN_ROLE;
        }
    }

    @Override
    public int FindBackByTelephone(String username, String password, int role, Object o) {
        switch (role) {
            case ROLE.ADMIN:
                Admin admin = (Admin) o;
                if(admin.getUsername().equals(username)){
                    admin.setPassword(password);
                    return adminMapper.updateById(admin);
                }
            case ROLE.LESSEE:
                Lessee lessee = (Lessee) o;
                if(lessee.getUsername().equals(username)){
                    lessee.setPassword(password);
                    return lesseeMapper.updateById(lessee);
                }
            case ROLE.USER:
                User user = (User) o;
                if(user.getUsername().equals(username)){
                    user.setPassword(password);
                    return userMapper.updateById(user);
                }
            case ROLE.ANSWERER:
                Answerer answerer = (Answerer) o;
                if(answerer.getUsername().equals(username)){
                    answerer.setPassword(password);
                    return answererMapper.updateById(answerer);
                }
            default:
                return REASON.NOT_FOUNT;
        }
    }

    //检查是否有绑定该邮箱号，如果绑定了则发送邮件，方法返回值为验证码
    @Override
    public int askAuthCodeByEmail(String email, int role) {
        System.out.println(email + " " + role);
        switch (role) {
            case ROLE.ADMIN:
                QueryWrapper<Admin> sql = new QueryWrapper<Admin>().eq("email", email);
                Admin admin = adminMapper.selectOne(sql);
                if (admin == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("email", email);
                }));
                if (lessee == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.USER:
                User user = userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("email", email);
                }));
                if (user == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("email", email);
                }));
                if (answerer == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
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



    //检查是否有绑定该电话号，如果绑定了则发送短信，方法返回值为验证码
    @Override
    public int askAuthCodeByTelephone(String telephone, int role) {
        switch (role) {
            case ROLE.ADMIN:
                QueryWrapper<Admin> sql = new QueryWrapper<Admin>().eq("telephone", telephone);
                Admin admin = adminMapper.selectOne(sql);
                if (admin == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("telephone", telephone);
                }));
                if (lessee == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.USER:
                User user = userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("telephone", telephone);
                }));
                if (user == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("telephone", telephone);
                }));
                if (answerer == null) {
                    return REASON.WRONG_EMAIL;
                }
                break;
        }
        int authCode = 100001 + new Random().nextInt(888888);
        //否则就往该邮箱发送邮件验证码
        boolean success = SenTelephone(telephone, authCode);
        if (success) {
            return authCode;
        }
        return REASON.SEND_FAIL;
    }

    @Override
    public Object GetUserByTelephone(String telephone, int role) {
        switch (role) {
            case ROLE.ADMIN:
                return adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("telephone", telephone);
                }));
            case ROLE.LESSEE:
                return lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
                    i.eq("telephone", telephone);
                }));
            case ROLE.USER:
                return userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("telephone", telephone);
                }));
            case ROLE.ANSWERER:
                return answererMapper.selectOne(new QueryWrapper<Answerer>().and(i -> {
                    i.eq("telephone", telephone);
                }));
            default:
                return null;
        }
    }

    //短信验证码发送方法
    @Override
    public boolean SenTelephone(String Telephone, int authCode){
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "69b62881e9e244329018ab1d5056c0e9";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", Telephone);
        querys.put("param", "**code**:"+authCode);
        querys.put("smsSignId", "4f94ee878bdf4a3db155ac9c78f4e816");
        querys.put("templateId", "cd0602e40caa4f32bdf73d9491d487ea");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

