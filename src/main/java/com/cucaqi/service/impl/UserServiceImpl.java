package com.cucaqi.service.impl;

import com.cucaqi.entity.User;
import com.cucaqi.mapper.UserMapper;
import com.cucaqi.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.cucaqi.entity.Lessee;
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

}
