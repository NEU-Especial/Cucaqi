package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Answerer;
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
public interface AnswererMapper extends BaseMapper<Answerer> {
    @Select("select * from t_answerer join t_group_answerer on " +
            "t_answerer.id = t_group_answerer.answererId join t_group on " +
            "t_group_answerer.groupId = t_group.id where t_group.id = #{groupId}")
    public List<Answerer> getByGroupId(Integer groupId);
}
