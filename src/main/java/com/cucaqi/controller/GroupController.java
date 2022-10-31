package com.cucaqi.controller;

import com.cucaqi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;
    @GetMapping
    public String test(){
        return "hello cacuqi";
    }
}
