package com.cucaqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Group;
import com.cucaqi.entity.Result;
import com.cucaqi.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{userId}")
    public Result getAllGroupByUserId( @PathVariable Integer userId){
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Group::getCreatedBy,userId);
        queryWrapper.orderByDesc(Group::getCreatedTime);
        List<Group> list = groupService.list(queryWrapper);
        return new Result(HTTP.SUCCESS,list);
    }
    @PostMapping
    public Result save(@RequestBody Group group){
        groupService.save(group);
        return new Result(HTTP.SUCCESS,"保存成功");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        //判断群组下是否有关联答者
        if(!groupService.hasAnswerer(id)) {
            groupService.removeById(id);
            return new Result(HTTP.SUCCESS,"删除成功");
        }
        return new Result(HTTP.NOT_FOUND,"该群组不允许删除!");
    }
    @PutMapping
    public Result update(@RequestBody Group group){
        Group group1 = groupService.getById(group.getId());
        if(group1 != null) {
            groupService.updateById(group);
            return new Result(HTTP.SUCCESS,"修改成功");
        }
        return new Result(HTTP.NOT_FOUND,"修改出现未知错误");
    }

}
