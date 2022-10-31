package com.cucaqi.controller;

import com.cucaqi.entity.Lessee;
import com.cucaqi.entity.Result;
import com.cucaqi.service.ILesseeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
            result.setCode(200);
            result.setMsg("查询成功！");
        }
        catch (Exception e){
            result.setCode(404);
            result.setMsg("ERROR");
        }

        return result;

    }

}
