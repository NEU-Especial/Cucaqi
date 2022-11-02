package com.cucaqi.service.impl;

import com.cucaqi.entity.Survey;
import com.cucaqi.mapper.SurveyMapper;
import com.cucaqi.service.ISurveyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cucaqi.entity.Lessee;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Service
public class SurveyServiceImpl extends ServiceImpl<SurveyMapper, Survey> implements ISurveyService {
    @Autowired
    private SurveyMapper surveyMapper;


}
