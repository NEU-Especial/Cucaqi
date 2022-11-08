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

    /**
     * 找到登录用户的所有已删除群组
     * @param userId
     * @return
     */
    @GetMapping("/deleted/{userId}")
    public Result getAllDeletedGroupByUserId( @PathVariable Integer userId){
        List<Group> list = groupService.listDeleted(userId);
        return new Result(HTTP.SUCCESS,list);
    }
    @GetMapping("/belongs/{answererId}")
    public Result getAllGroupByAnswererId( @PathVariable Integer answererId){
        List<Group> list = groupService.listGroupByAnswererId(answererId);
        return new Result(HTTP.SUCCESS,list);
    }
    @PostMapping
    public Result save(@RequestBody Group group){
        List<Group> groupList = groupService.list();
        for (Group group2 : groupList) {
            if(group2.getGroupName().equals(group.getGroupName())){
                return new Result(HTTP.BAD_REQ,"组名重复");
            }
        }
        groupService.save(group);
        return new Result(HTTP.SUCCESS,"保存成功");
    }
    @DeleteMapping("/deleteGroup")
    public Result delete(@RequestBody Group group){
        //判断群组下是否有关联答者
        if(!groupService.hasAnswerer(group.getId())) {
            groupService.removeById(group.getId());
            return new Result(HTTP.SUCCESS,"删除成功");
        }
        return new Result(HTTP.BAD_REQ,"该群组有关联数据不允许删除!");
    }
    @DeleteMapping("/deleteRelation/{groupId}/{answererId}")
    public Result deleteRelation(@PathVariable Integer groupId,@PathVariable Integer answererId){
        if(groupService.deleteRelation(groupId,answererId)){
            return new Result(HTTP.SUCCESS,"删除关系成功");
        }
        return new Result(HTTP.BAD_REQ,"删除关系失败");
    }
    @PostMapping("/addRelation/{groupId}/{answererId}")
    public Result addRelation(@PathVariable Integer groupId,@PathVariable Integer answererId){
        if(groupService.hasRelationBetweenGroupAnswerer(groupId,answererId)){
            return new Result(HTTP.BAD_REQ,"答者已在群组中，请勿重复添加");
        }
        if(groupService.addRelation(groupId,answererId)){
            return new Result(HTTP.SUCCESS,"添加关系成功");
        }
        return new Result(HTTP.BAD_REQ,"添加关系失败");
    }
    @PutMapping
    public Result update(@RequestBody Group group){
        Group group1 = groupService.getById(group.getId());
        if(group1 != null) {
            List<Group> groupList = groupService.list();
            for (Group group2 : groupList) {
                if(group2.getGroupName().equals(group.getGroupName())){
                    return new Result(HTTP.BAD_REQ,"组名重复");
                }
            }
            groupService.updateById(group);
            return new Result(HTTP.SUCCESS,"修改成功");
        }
        return new Result(HTTP.NOT_FOUND,"修改出现未知错误");
    }
    @PutMapping("/recover/{groupId}")
    public Result updateDeletedStatus(@PathVariable Integer groupId){
        if(groupService.updateDeletedStatus(groupId)) {
            return new Result(HTTP.SUCCESS, "恢复成功");
        }
        return new Result(HTTP.BAD_REQ,"恢复出错");
    }
}
