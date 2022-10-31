package com.cucaqi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public Object LoginByPassword(String userName, String Password, int role) {
        //不同的角色需要执行不同的登陆方法
        switch (role) {
            case ROLE.ADMIN:
                return userMapper.selectOne(new QueryWrapper<User>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            case ROLE.LESSEE:
                return adminMapper.selectOne(new QueryWrapper<Admin>().and(i -> {
                    i.eq("username", userName).eq("password", Password);
                }));
            case ROLE.USER:
                return lesseeMapper.selectOne(new QueryWrapper<Lessee>().and(i -> {
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





}
