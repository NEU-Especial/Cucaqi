package com.cucaqi.service.impl;

import com.cucaqi.entity.User;
import com.cucaqi.mapper.AnswererMapper;
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
    @Autowired
    private AnswererMapper answererMapper;
    @Override
    /**
     * 根据租户id查询用户
     */
    public List<User> getUserlist(int id) {

        return userMapper.getUserlist(id);
    }
    public Integer deleteUser(int id){
        Integer count =answererMapper.tellAnswerer(id);
        if(count==0){
            return userMapper.deleteUser(id);
            //该用户为无数据关联用户,即可删除
        }
        else{
            return -100;
            //该用户为数据关联用户,不可删除
        }

    }

    /**
     * 通过用户名查询用户,当查询到时返回true
     * @param username
     * @return
     */
    @Override
    public boolean searchUser(String username){
        User searchUser = userMapper.searchUser(username);
        if (searchUser==null){
            return  false;
        }
            return true;
    }

    @Override
    public List<User> getDeletedUserlist(int id) {
        return userMapper.getDeletedUserlist(id);
    }
}
