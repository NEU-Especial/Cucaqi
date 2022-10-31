package com.cucaqi.controller;

import com.cucaqi.entity.Result;
import com.cucaqi.mapper.AdminMapper;
import com.cucaqi.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/cucaqi/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @GetMapping
    public Result test(){
        Result result = new Result();

        result.setData(adminService.list());
        return result;
    }
}
