package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Lessee;
import org.apache.ibatis.annotations.Insert;
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
public interface LesseeMapper extends BaseMapper<Lessee> {
    @Select("select * from t_lessee where deleted = 0")
    /**
     * 获取所有租户信息
     */
    public List<Lessee> getLesseeList();

    /**
     * 添加租户
     * @param lessee
     * @return
     */
    @Insert("insert into t_lessee values(null,#{username},#{password},#{telephone},#{securityQuestion},#{securityAnswer},#{role},#{payment},#{deleted},#{createdBy},#{inviteCode})")
    public Integer addLessee(Lessee lessee);

    /**
     * 删除租户
     * @param id
     * @return
     */
    @Update("update t_lessee set deleted =1 where id=#{id} ")
    public Integer deleteLessee(int id);

    /**
     * 通过同户名查找租户
     * @param username
     * @return
     */
    @Select("select * from t_lessee where username = #{username}")
    public Lessee searchLessee(String username);




}
