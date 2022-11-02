package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Select("select t_user.* from t_user  join t_lessee on t_user.createdBy=t_lessee.id where t_lessee.id=#{id} and t_user.deleted = 0 and t_lessee.deleted=0 ")
    /**
     * 根据租户id查询用户
     */
    public List<User> getUserlist(int id);
    /**
     * 判断用户表中是否有关联的租户
     * @param id
     * @return
     */
    @Select("select count(*) from t_user where createdBy =#{id} ")
    public Integer tellLessee(int id);

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Update("update t_user set deleted =1 where id=#{id} ")
    public Integer deleteUser(int id);

    /**
     * 通过同户名查找用户
     * @param username
     * @return
     */
    @Select("select * from t_user where username = #{username}")
    public User searchUser(String username);
}
