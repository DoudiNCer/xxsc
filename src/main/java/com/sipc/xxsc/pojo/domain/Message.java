package com.sipc.xxsc.pojo.domain;

import lombok.Data;

@Data
public class Message {
    private Integer id;
    private Integer from;
    private Integer to;
    private Long date;
    private Integer isRead;
    private String message;
}
