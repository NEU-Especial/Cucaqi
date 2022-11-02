package com.cucaqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cucaqi.entity.Group;

public interface IGroupService extends IService<Group> {
    public Boolean hasAnswerer(Integer id);
}
