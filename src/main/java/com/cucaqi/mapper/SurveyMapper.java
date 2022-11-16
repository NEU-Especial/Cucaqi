package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Survey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */

@Mapper
public interface SurveyMapper extends BaseMapper<Survey> {


    @Update("update t_survey  set state=#{state} where id=#{surveyId}")
    public int UpdateState(int surveyId, int state);

    @Update("update t_survey  set state=0,deleted=0 where id=#{surveyId} and deleted=1 ")
    int recoverSurvey(Integer surveyId);



    //找到所有需要回答得问卷，问卷的状态只能是已发布
    @Select("select * from t_survey ")
    List<Survey> allSurveyToAnswer(Integer id);



}
