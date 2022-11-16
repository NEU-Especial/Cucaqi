package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Survey;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
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


    //找到所有需要回答的问卷，问卷的状态只能是已发布
    @Select("select * from t_survey where t_survey.state=1 and t_survey.id in(select surveyId from t_answerer_survey where answererId=#{id})")
    List<Survey> allSurveyToAnswer(Integer id);


    @Delete("delete from t_answerer_survey where surveyId=#{surveyId} and answer is NULL")
    int deleteRelation(Integer surveyId);

    //插入空答者记录
    @Insert("insert into t_answerer_survey (surveyId, createdTime, answer)value (#{surveyId},#{now},#{answer})")
    int insertRelationWithoutAnswer(Integer surveyId, String answer, LocalDateTime now);

    //避免重复回答，查询答者是否有记录，有记录就提醒不能重复提交
    @Select("select count(*) from t_answerer_survey where surveyId=#{surveyId} and answererId=#{answerId} and answer IS NOT NULL")
    int getCountBySurveyIdAndAnswerId(Integer surveyId, Integer answerId);

    //代答答者的提交
    @Update("update t_answerer_survey set answer=#{answer} where surveyId=#{surveyId} and answererId=#{answerId}")
    int updateAnswer(Integer surveyId, Integer answerId, String answer);

    @Update("update t_survey set curCount=(curCount+1) where id=#{surveyId}")
    int updateCurCount(Integer surveyId);
}
