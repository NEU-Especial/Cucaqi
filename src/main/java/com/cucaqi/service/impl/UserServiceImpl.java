package com.cucaqi.service.impl;

import com.cucaqi.entity.User;
import com.cucaqi.mapper.UserMapper;
import com.cucaqi.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cucaqi.entity.Lessee;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    /**
     * 根据租户id查询用户
     */
    public List<User> getUserlist(int id) {
        return userMapper.getUserlist(id);
    }
}
