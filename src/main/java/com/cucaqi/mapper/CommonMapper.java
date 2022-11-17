package com.cucaqi.mapper;

import com.cucaqi.entity.Survey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonMapper {
    @Select("select * from t_survey where createdBy=#{id} and deleted=1")
    List<Survey> findDeletedSurvey(Integer id);

    @Select("select * from t_survey where id=#{id} and deleted=1")
    Survey selectSurveyById(Integer id);

}
