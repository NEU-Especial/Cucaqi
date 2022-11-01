package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.service.ILesseeService;
import jdk.vm.ci.meta.Constant;
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
@RequestMapping("/cucaqi/lessee")
public class LesseeController {
    public static final int exist =-100;
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
            result.setMsg("查询租户失败--"+e.getLocalizedMessage());
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
            boolean number= iLesseeService.save(lessee);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("添加成功！");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("添加租户失败--"+e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 删除租户
     * @param lessee
     * @return
     */
    @DeleteMapping("/deleteLessee")
    public Result deleteLessee(@RequestBody Lessee lessee){
        Result result=new Result();
        try{
            int id=lessee.getId();
            Integer res = iLesseeService.deleteLessee(id);
            if(res==exist){
                result.setMsg("删除失败--该租户不是无数据关联租户");
                result.setCode(HTTP.SERVER_ERR);
            }
            else {
                result.setCode(HTTP.SUCCESS);
                result.setMsg("删除成功");
            }
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("删除租户失败--"+e.getLocalizedMessage());

        }
       return result;
    }


}
