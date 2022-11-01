package com.cucaqi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cucaqi.constants.REASON;
import com.cucaqi.constants.ROLE;
import com.cucaqi.entity.*;
import com.cucaqi.mapper.*;
import com.cucaqi.service.IInfoService;
import lombok.val;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人信息服务，提供密码更新，更改密码，绑定密保，绑定手机号服务
 */

@Service
public class InfoServiceImpl implements IInfoService {

    @Autowired
    private SecurityQuesMapper securityQuesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private LesseeMapper lesseeMapper;

    @Autowired
    private AnswererMapper answererMapper;

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
    public int BindTelephone(int id, int role, String Telephone, String authCode) {
        return 0;
    }

    @Override
    public String AskAuthCode(int id, String Telephone) {
        return null;
    }
}
