package com.cucaqi.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_security_question")
public class SecurityQuestion {
    @TableId("id")
    int id;

    String content;

}
