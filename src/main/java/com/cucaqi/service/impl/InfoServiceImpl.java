package com.cucaqi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.mapper.*;
import com.cucaqi.service.IInfoService;
import com.cucaqi.service.ILoginService;
import lombok.val;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 个人信息服务，提供密码更新，更改密码，绑定密保，绑定手机号服务
 */

@Service
public class InfoServiceImpl implements IInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private LesseeMapper lesseeMapper;

    @Autowired
    private AnswererMapper answererMapper;

    @Autowired
    private ILoginService loginService;

    @Override
    public int BindSecurityQuestion(int id, int role, int questionId, String answer) {
        switch (role) {
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectById(id);
                lessee.setSecurityQuestion(questionId);
                lessee.setSecurityAnswer(answer);
                return lesseeMapper.updateById(lessee);
            case ROLE.USER:
                User user = new User();
                user.setSecurityQuestion(questionId);
                user.setSecurityAnswer(answer);
                return userMapper.updateById(user);
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectById(id);
                answerer.setSecurityQuestion(questionId);
                answerer.setSecurityAnswer(answer);
                return answererMapper.updateById(answerer);
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectById(id);
                admin.setSecurityQuestion(questionId);
                admin.setSecurityAnswer(answer);
                return adminMapper.updateById(admin);
        }
        return REASON.UNKNOWN_ROLE;
    }

    @Override
    public int UpdatePassword(int id, int role, String password) {
        switch (role) {
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectById(id);
                lessee.setPassword(password);
                return lesseeMapper.updateById(lessee);
            case ROLE.USER:
                User user = new User();
                user.setPassword(password);
                return userMapper.updateById(user);
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectById(id);
                answerer.setPassword(password);
                return answererMapper.updateById(answerer);
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectById(id);
                admin.setPassword(password);
                return adminMapper.updateById(admin);
        }
        return REASON.UNKNOWN_ROLE;
    }

    @Override
    public int BindTelephone(int id, int role, String Telephone) {
        return 0;
    }

    @Override
    public String AskAuthCodeByTelephone(int id, String Telephone) {
        return null;
    }

    //请求发送邮件
    @Override
    public int AskAuthCodeByEmail(String email) {
        int authCode = 100001 + new Random().nextInt(888888);
        //否则就往该邮箱发送邮件验证码
        boolean success = loginService.SenEmail(email, authCode);
        if (success) {
            return authCode;
        }
        return REASON.SEND_FAIL;
    }

    @Override
    public int BindEmail(int id, int role, String Email) {
        switch (role) {
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectById(id);
                lessee.setEmail(Email);
                return lesseeMapper.updateById(lessee);
            case ROLE.USER:
                User user = new User();
                user.setEmail(Email);
                return userMapper.updateById(user);
            case ROLE.ANSWERER:
                Answerer answerer = answererMapper.selectById(id);
                answerer.setEmail(Email);
                return answererMapper.updateById(answerer);
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectById(id);
                admin.setEmail(Email);
                return adminMapper.updateById(admin);
        }
        return REASON.UNKNOWN_ROLE;
    }

    @Override
    public int updateInviteCode(int id, int role) {
        Random random = new Random();
        String inviteCode = String.valueOf(100001 + random.nextInt(888888));
        switch (role) {
            case ROLE.LESSEE:
                Lessee lessee = lesseeMapper.selectById(id);
                if (lessee == null) {
                    return REASON.NOT_FOUNT;
                }
                lessee.setInviteCode(inviteCode);
                lesseeMapper.update(lessee, new UpdateWrapper<Lessee>().set("inviteCode", inviteCode));
                break;
            case ROLE.USER:
                User user = userMapper.selectById(id);
                if (user == null) {
                    return REASON.NOT_FOUNT;
                }
                user.setInviteCode(inviteCode);
                userMapper.update(user, new UpdateWrapper<User>().set("inviteCode", inviteCode));
                break;
            case ROLE.ADMIN:
                Admin admin = adminMapper.selectById(id);
                if (admin == null) {
                    return REASON.NOT_FOUNT;
                }
                admin.setInviteCode(inviteCode);
                adminMapper.update(admin, new UpdateWrapper<Admin>().set("inviteCode", inviteCode));
                break;
        }
        return Integer.parseInt(inviteCode);
    }

}
