package com.sipc.xxsc.pojo.po.advisory;

import lombok.Data;

import java.sql.Date;

@Data
public class DoctorDetailPo {
    private Integer id;
    private String name;
    private String communicate;
    private String exp;
    private String msg;
    private String avatarUrl;
    private Date birthday;
}
