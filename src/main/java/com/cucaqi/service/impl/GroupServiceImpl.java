package com.cucaqi.service.impl;

import com.cucaqi.mapper.GroupMapper;
import com.cucaqi.service.IGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cucaqi.entity.Group;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Override
    public Boolean hasAnswerer(Integer id) {
        return groupMapper.hasData(id) > 0;
    }

    @Override
    public List<Group> listGroupByAnswererId(Integer answererId) {
        return groupMapper.getByAnswererId(answererId);
    }

    @Override
    public boolean deleteRelation(Integer groupId, Integer answererId) {
        return groupMapper.deleteRelationFromGroupAnswerer(groupId,answererId) > 0;
    }

    @Override
    public boolean addRelation(Integer groupId, Integer answererId) {
        return groupMapper.addRelationFromGroupAnswerer(groupId,answererId) > 0;
    }
}
