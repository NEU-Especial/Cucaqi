package com.cucaqi.service.impl;

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
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public Object LoginByPassword(String userName, String Password, int role) {
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
    public int Register(String userName, String password, int role, String inviteCode) {
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
}
