package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Answerer;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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
public interface AnswererMapper extends BaseMapper<Answerer> {
    /**
     * 判断答者表中是否有关联的用户
     * @param id
     * @return
     */
    @Select("select count(*) from t_answerer where createdBy =#{id} ")
    public Integer tellAnswerer(int id);

    @Select("select * from t_answerer join t_group_answerer on " +
            "t_answerer.id = t_group_answerer.answererId join t_group on " +
            "t_group_answerer.groupId = t_group.id where t_group.id = #{groupId}")
    public List<Answerer> getByGroupId(Integer groupId);
    @Select("select * from t_answerer where createdBy =#{userId} and deleted = 1 ")
    List<Answerer> getDeleted(Integer userId);
    @Update("update t_answerer set deleted = 0 where id = #{answererId}")
    Integer updateDeletedStatus(Integer answererId);
}
