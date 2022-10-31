package com.cucaqi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cucaqi.entity.Group;
import com.cucaqi.mapper.GroupMapper;
import com.cucaqi.service.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
}
