package com.cucaqi.entity;


import lombok.Data;

@Data
public class Result {
    private Integer code; //HTTP状态响应码

    private String msg; //错误信息

    private Object data; //数据
}
