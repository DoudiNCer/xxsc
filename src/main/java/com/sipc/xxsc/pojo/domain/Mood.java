package com.sipc.xxsc.pojo.domain;

import lombok.Data;

@Data
public class Mood {
    private Integer id;
    private Integer userId;
    private Integer mood;
    private Long date;
    private String message;
}
