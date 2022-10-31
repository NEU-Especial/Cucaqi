package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select t_user.* from t_user  join t_lessee on t_user.createdBy=t_lessee.id where t_lessee.id=#{id}")
    /**
     * 根据租户id查询用户
     */
    public List<User> getUserlist(int id);
}
