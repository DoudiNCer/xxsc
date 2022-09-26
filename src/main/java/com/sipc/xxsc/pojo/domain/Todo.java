package com.sipc.xxsc.pojo.domain;

import lombok.Data;

@Data
public class Todo {
    private Integer id;
    private Integer userId;
    private Integer finish;
    private Long date;
    private String todo;
}
