package com.cucaqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cucaqi.entity.Group;
import com.cucaqi.entity.Result;
import com.cucaqi.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/cucaqi/group")
public class GroupController {
    @Autowired
    private IGroupService groupService;
    @GetMapping("/{id}")
    public Result getAllGroupById( @PathVariable Integer id){
        Result result = new Result();
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Group::getCreatedBy,id);
        queryWrapper.orderByDesc(Group::getCreatedTime);
        List<Group> list = groupService.list(queryWrapper);
        result.setCode(200);
        result.setData(list);
        return result;
    }


}
