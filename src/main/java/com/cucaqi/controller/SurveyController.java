package com.cucaqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cucaqi.entity.Answerer;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.Survey;
import com.cucaqi.mapper.AnswererMapper;
import com.cucaqi.mapper.CommonMapper;
import com.cucaqi.mapper.GroupMapper;
import com.cucaqi.mapper.SurveyMapper;
import com.cucaqi.service.impl.SurveyServiceImpl;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.sun.mail.util.QEncoderStream;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/cucaqi/survey")
public class SurveyController {
    @Autowired
    SurveyMapper surveyMapper;
    @Autowired
    AnswererMapper answererMapper;

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    CommonMapper commonMapper;


    @PostMapping("/addSurvey")
    public Result addSurvey(@RequestBody Survey survey) {
        survey.setCreatedTime(LocalDateTime.now());
        int insert = surveyMapper.insert(survey);
        if (insert == 0) {
            return new Result(500, "创建失败");
        }
        return new Result(200, "创建成功");
    }

    @GetMapping("/findAllSurvey")
    public Result findAllSurvey(@Param("id") Integer id) {
        List<Survey> createdBy = surveyMapper.selectList(new QueryWrapper<Survey>().eq("createdBy", id));
        return new Result(200, "", createdBy);
    }

    @PostMapping("/postToPublic")
    public Result postToPublic(@Param("userId") Integer userId, @Param("surveyId") Integer surveyId) {
        List<Answerer> answerers = answererMapper.selectList(new QueryWrapper<Answerer>().eq("createdBy", userId));
        for (Answerer answerer : answerers) {
            Integer integer = answererMapper.insertAnswerSurvey(surveyId, answerer.getId(), LocalDateTime.now());
        }
        return new Result(200, "发布成功");
    }

    @PostMapping("/postToGroup")
    public Result postToGroup(@Param("groupId") Integer groupId, @Param("surveyId") Integer surveyId) {
        List<Answerer> answerers = answererMapper.getByGroupId(groupId);
        for (Answerer answerer : answerers) {
            int count = answererMapper.getCountByGroupIDAndAnswerId(answerer.getId(), surveyId);
            if (count != 0) {
                continue;
            }
            answererMapper.insertAnswerSurvey(surveyId, answerer.getId(), LocalDateTime.now());
        }
        return new Result(200, "发布成功");
    }

    @PutMapping("/updateSurveyState")
    public Result updateSurveyState(@Param("state") Integer state, @Param("surveyId") Integer surveyId) {
        surveyMapper.UpdateState(surveyId, state);
        return new Result(200, "更新成功");
    }

    @DeleteMapping("/softDeleteSurvey")
    public Result softDeleteSurvey(@Param("surveyId") Integer surveyId) {
        surveyMapper.deleteById(surveyId);
        return new Result(200, "删除成功");
    }

    @GetMapping("/findDeletedSurvey")
    public Result findDeletedSurvey(@Param("id") Integer id) {
        List<Survey> createdBy = commonMapper.findDeletedSurvey(id);
        return new Result(200, "", createdBy);
    }
}
