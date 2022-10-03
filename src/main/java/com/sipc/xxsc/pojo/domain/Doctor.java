package com.sipc.xxsc.pojo.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Doctor {
    private Integer id;
    private String name;
    private String goodat;
    private String avatarUrl;
    private String communicate;
    private String exp;
    private String msg;
    private Date birthday;
}
