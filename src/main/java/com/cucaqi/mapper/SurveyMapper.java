package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Survey;
import org.apache.ibatis.annotations.Mapper;

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
public interface SurveyMapper extends BaseMapper<Survey> {
    //查找问卷
    Survey findSurveyById(Integer id);

    //添加问卷
    int addSurvey(Survey survey);

    //删除问卷
    int deleteSurvey(Integer id);

    //软删除问卷
    int softDeleteSurvey(Integer id);

    //修改问卷
    int updateSurvey(Survey survey);

    //查找所有问卷
    List<Survey> findAllSurvey();

    //根据createdBy查找问卷
    List<Survey> findSurveyByCreatedBy(Integer createdBy);

}
