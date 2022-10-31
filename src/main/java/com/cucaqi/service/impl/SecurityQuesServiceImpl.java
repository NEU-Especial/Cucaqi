package com.cucaqi.service.impl;

import com.cucaqi.entity.SecurityQuestion;
import com.cucaqi.mapper.SecurityQuesMapper;
import com.cucaqi.service.ISecurityQuesService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SecurityQuesServiceImpl extends ServiceImpl<SecurityQuesMapper, SecurityQuestion> implements ISecurityQuesService {

}
