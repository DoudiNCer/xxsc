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

    public GetDoctorDetailResult(DoctorDetailPo po) {
        this.id = po.getId();
        this.communicate = po.getCommunicate();
        this.exp = po.getExp();
        this.msg = po.getMsg();
        this.name = po.getName();
    }
}
