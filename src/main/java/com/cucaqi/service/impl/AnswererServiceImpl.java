package com.cucaqi.service.impl;

import com.cucaqi.mapper.AnswererMapper;
import com.cucaqi.service.IAnswererService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cucaqi.entity.Answerer;

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
public class AnswererServiceImpl extends ServiceImpl<AnswererMapper, Answerer> implements IAnswererService {
    @Autowired
    private AnswererMapper answererMapper;
    @Override
    public List<Answerer> listAnswererByGroupId(Integer groupId) {
        return answererMapper.getByGroupId(groupId);
    }

    @Override
    public List<Answerer> listDeletedAnswerer(Integer userId) {
        return answererMapper.getDeleted(userId);
    }

    @Override
    public boolean updateDeletedStatus(Integer answererId) {
        return answererMapper.updateDeletedStatus(answererId) > 0;
    }
}
