package com.cucaqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cucaqi.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
public interface IUserService extends IService<User> {
    /**
     * 根据租户id查询用户
     */
    public List<User> getUserlist(int id);

    /**
     * 删除用户
     * @param id
     * @return
     */
    public Integer deleteUser(int id);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public boolean searchUser(String username);

    List<User> getDeletedUserlist(int id);
}
