package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.po.advisory.DoctorDetailPo;
import lombok.Data;


@Data
public class GetDoctorDetailResult {
    private Integer id;
    private String name;
    private String communicate;
    private String exp;
    private String msg;
    private Integer age;
    private String avatarUrl;

    public GetDoctorDetailResult(DoctorDetailPo po, Integer age) {
        this.id = po.getId();
        this.communicate = po.getCommunicate();
        this.exp = po.getExp();
        this.msg = po.getMsg();
        this.name = po.getName();
        this.age = age;
        this.avatarUrl = po.getAvatarUrl();
    }
}
