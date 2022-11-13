package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Answerer;
import com.cucaqi.entity.Group;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
public interface GroupMapper extends BaseMapper<Group> {
    @Select("select count(*) from t_group_answerer where groupId = #{groupId}")
    public Integer hasData(Integer groupId);
    @Select("select * from t_group join t_group_answerer on " +
            "t_group.id = t_group_answerer.groupId join t_answerer on " +
            "t_group_answerer.answererId = t_answerer.id where t_answerer.id = #{answererId}")
    public List<Group> getByAnswererId(Integer answererId);
    @Delete("delete from t_group_answerer where groupId = #{groupId} and answererId = #{answererId}")
    public Integer deleteRelationFromGroupAnswerer(Integer groupId,Integer answererId);
    @Insert("insert into t_group_answerer values(#{groupId},#{answererId})")
    public Integer addRelationFromGroupAnswerer(Integer groupId, Integer answererId);
    @Select("select count(*) from t_group_answerer where groupId = #{groupId} and answererId = #{answererId}")
    public Integer hasRelation(Integer groupId,Integer answererId);
}
