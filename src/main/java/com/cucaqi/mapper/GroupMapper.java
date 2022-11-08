package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    @Select("select count(*) from t_group_answerer where groupId = #{groupId}")
    public Integer hasData(Integer groupId);
}
