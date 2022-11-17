package com.cucaqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cucaqi.controller.constants.HTTP;
import com.cucaqi.entity.Answerer;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.Survey;
import com.cucaqi.mapper.AnswererMapper;
import com.cucaqi.mapper.CommonMapper;
import com.cucaqi.mapper.GroupMapper;
import com.cucaqi.mapper.SurveyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
    @PutMapping("/updateSurvey")
    public Result updateSurvey(@RequestBody Survey survey) {
        int insert = surveyMapper.updateById(survey);
        if (insert == 0) {
            return new Result(500, "修改失败");
        }
        return new Result(200, "修改成功");
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
        //发布在群组中的问卷 之前没有关系则在关系表中将他们联系起来
        if(!(groupMapper.hasRelationInGroupSurvey(groupId,surveyId) > 0)) {
            groupMapper.addRelationToGroupSurvey(groupId, surveyId);
        }else{
            return new Result(HTTP.BAD_REQ, "该群组中已发布过该问卷");
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
        //需要删除关系
        int count = surveyMapper.deleteRelation(surveyId);
        count = surveyMapper.updateCurCount(surveyId);
        return new Result(200, "删除成功");
    }

    @GetMapping("/findDeletedSurvey")
    public Result findDeletedSurvey(@Param("id") Integer id) {
        List<Survey> createdBy = commonMapper.findDeletedSurvey(id);
        return new Result(200, "", createdBy);
    }

    @PutMapping("/recoverSurvey")
    public Result RecoverSurvey(@Param("surveyId") Integer surveyId) {
        Survey survey = commonMapper.selectSurveyById(surveyId);
        survey.setId(null);
        survey.setCurCount(0);
        survey.setDeleted(0);
        survey.setState(0);
        surveyMapper.insert(survey);//重新插入
        return new Result(200, "恢复成功");
    }

    @GetMapping("/allSurveyToAnswer")
    public Result allSurveyToAnswer(@Param("id") Integer id) {
        //根据答者id拿到所有代答问卷
        List<Survey> needToAnswer = surveyMapper.allSurveyToAnswer(id);
        ArrayList<String> answers = new ArrayList<>();
        for (Survey survey : needToAnswer) {
            //填充答卷结果
            String answer = surveyMapper.getAnswer(survey.getId(), id);
            answers.add(answer);
        }
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("surveyList", needToAnswer);
        stringObjectHashMap.put("answers", answers);
        return new Result(200, "", stringObjectHashMap);
    }

    @GetMapping("/getSurveyById/{id}")
    public Result getSurveyById(@PathVariable("id") Integer id) {
        Survey survey = surveyMapper.selectById(id);
        return new Result(200, "", survey);
    }


    @PostMapping("/saveAnswerResult/{surveyId}/{answererId}")
    public Result saveAnswerResult(@PathVariable("answererId") Integer answererId, @PathVariable("surveyId") Integer surveyId, @RequestBody String answer) throws UnsupportedEncodingException {
        System.out.println(answer);
        if (Strings.isBlank(answer)) {
            return new Result(400, "请勿提交空白问卷");
        }
        answer = URLDecoder.decode(answer, "UTF-8");
        answer = answer.substring(0, answer.length() - 1);
        //先判断以下当前的问卷状态
        if (answererId == -1) {
            answererId = null;
        }
        Survey survey = surveyMapper.selectById(surveyId);
        if (survey == null) {
            return new Result(400, "问卷已被删除");
        }
        //判断当前提交的人数
        if (survey.getLimitCount() != 0 && survey.getCurCount() >= survey.getLimitCount()) {
            return new Result(400, "问卷回答人数已满");
        }
        //判断时间是否合法
        if (survey.getEndTime() != null) {
            if (survey.getEndTime().isBefore(LocalDateTime.now())) {
                return new Result(400, "问卷已到期");
            }
        }
        //接下来需要判断用户用户ID，首先是用户为随机用户即可以插入
        if (answererId == null) {
            surveyMapper.insertRelationWithoutAnswer(surveyId, answer, LocalDateTime.now());
            surveyMapper.updateCurCount(surveyId);
            return new Result(200, "提交成功，感谢作答");
        }

        if (surveyMapper.getCountBySurveyIdAndAnswerId(surveyId, answererId) > 0) {
            return new Result(400, "请勿重复提交");
        }

        surveyMapper.updateAnswer(surveyId, answererId, answer);
        surveyMapper.updateCurCount(surveyId);

        return new Result(200, "提交成功，感谢作答");

    }


}
