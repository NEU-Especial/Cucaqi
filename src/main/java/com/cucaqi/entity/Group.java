package com.cucaqi.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Group {
    private Integer id;
    private String groupName;
    private Integer createdBy;
    private LocalDateTime createdTime;
}
