package com.cucaqi.controller;

import com.cucaqi.constants.HTTP;
import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.entity.User;
import com.cucaqi.service.ILesseeService;
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
            //获取数据库中所有租户
            result.setData(lesseeList);
            result.setCode(HTTP.SUCCESS);
            result.setMsg("查询成功！");
        }
        catch (Exception e){
            result.setCode(HTTP.NOT_FOUND);
            result.setMsg("查询租户失败--");
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
        if(lessee==null){
            result.setCode(HTTP.SERVER_ERR);
            result.setMsg("添加失败--添加的租户为空");
        }
        else {
            try {
                if(iLesseeService.searchLessee(lessee.getUsername())) {
                    result.setCode(HTTP.SERVER_ERR);
                    result.setMsg("租户添加失败--该用户名已存在");
                }
                else {
                    boolean number= iLesseeService.save(lessee);
                    result.setCode(HTTP.SUCCESS);
                    result.setMsg("添加成功！");
                }

            }
            catch (Exception e){
                result.setCode(HTTP.NOT_FOUND);
                result.setMsg("添加租户失败--");
            }
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
        int id=lessee.getId();
        if(iLesseeService.getById(id)==null){
            result.setMsg("删除失败--该租户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }
        else{
            try{
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
                result.setMsg("删除租户失败--");

            }
        }

       return result;
    }
    @PutMapping("/updateLessee")
    public Result updateUser(@RequestBody Lessee lessee){
        Result result=new Result();
        int id=lessee.getId();
        if(iLesseeService.getById(id)==null){
            result.setMsg("修改失败--该租户不存在");
            result.setCode(HTTP.SERVER_ERR);
        }
        else {
            try {
                Lessee byId = iLesseeService.getById(id);
             if(byId.getUsername().equals(lessee.getUsername()))  {
                 iLesseeService.updateById(lessee);
                 result.setCode(HTTP.SUCCESS);
                 result.setMsg("修改成功");
             }
             else {
                 if(iLesseeService.searchLessee(lessee.getUsername())) {
                     result.setCode(HTTP.SERVER_ERR);
                     result.setMsg("租户修改失败--该用户名已存在");
                 }
                 else {
                     iLesseeService.updateById(lessee);
                     result.setCode(HTTP.SUCCESS);
                     result.setMsg("修改成功");
                 }
             }
            }
            catch (Exception e){
                result.setCode(HTTP.NOT_FOUND);
                result.setMsg("租户修改失败--");
            }
        }

        return  result;

    }

}
