package com.cucaqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Answerer;
import com.cucaqi.entity.Group;
import com.cucaqi.entity.Result;
import com.cucaqi.service.IAnswererService;
import com.cucaqi.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/cucaqi/answerer")
public class AnswererController {
    @Autowired
    private IAnswererService answererService;
    @GetMapping("/{id}")
    public Result getAllAnswererByUserId( @PathVariable Integer id){
        LambdaQueryWrapper<Answerer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Answerer::getCreatedBy,id);
        List<Answerer> list = answererService.list(queryWrapper);
        return new Result(HTTP.SUCCESS,list);
    }
    @PostMapping
    public Result save(@RequestBody Answerer answerer){
        answererService.save(answerer);
        return new Result(HTTP.SUCCESS,"保存成功");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Answerer answerer = answererService.getById(id);
        if(answerer!=null) {
            answererService.removeById(id);
            return new Result(HTTP.SUCCESS,"删除成功");
        }
        return new Result(HTTP.NOT_FOUND,"删除出现错误");
    }
    @PutMapping
    public Result update(@RequestBody Answerer answerer){
        Answerer answerer1 = answererService.getById(answerer.getId());
        if(answerer1 != null) {
            answererService.updateById(answerer);
            return new Result(HTTP.SUCCESS,"修改成功");
        }
        return new Result(HTTP.NOT_FOUND,"修改出现未知错误");
    }
}
