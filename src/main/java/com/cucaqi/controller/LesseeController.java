package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.service.ILesseeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/cucaqi/lessee")
public class LesseeController {
    @Autowired
    private ILesseeService iLesseeService;

    /**
     * 查询所有的租户
     * @return
     */
    @GetMapping
    public Result getLesseeList(){

        Result result=new Result();
        try {
            List<Lessee> lesseeList = iLesseeService.getLesseeList();
            result.setData(lesseeList);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("查询成功！");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("查询租户失败");
        }

        return result;

    }

    /**
     *添加租户
     * @return
     */
    @PostMapping("/addLessee")
    public Result addLessee(@RequestBody Lessee lessee){
        Result result=new Result();
        try {
            Integer number= iLesseeService.addLessee(lessee);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("添加成功！");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("添加租户失败");
        }

        return result;
    }
    @DeleteMapping("/deleteLessee")
    public Result deleteLessee(@RequestBody Lessee lessee){
        Result result=new Result();
        try{
            int id=lessee.getId();
            iLesseeService.deleteLessee(id);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("删除成功");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("删除租户失败");

        }

       return result;
    }


}
