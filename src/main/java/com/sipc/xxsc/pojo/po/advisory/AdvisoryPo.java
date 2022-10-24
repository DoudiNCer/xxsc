package com.sipc.xxsc.pojo.po.advisory;

import lombok.Data;

@Data
public class AdvisoryPo {
    private Integer id;
    private Integer doctorId;
    private Integer userId;
    private String name;
    private String avatarUrl;
}
