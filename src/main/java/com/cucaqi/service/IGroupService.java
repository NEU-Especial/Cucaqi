package com.cucaqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cucaqi.entity.Group;

import java.util.List;

public interface IGroupService extends IService<Group> {
    public Boolean hasAnswerer(Integer id);

    public List<Group> listGroupByAnswererId(Integer answererId);

    public boolean deleteRelation(Integer groupId,Integer answererId);

    boolean addRelation(Integer groupId, Integer answererId);
}
