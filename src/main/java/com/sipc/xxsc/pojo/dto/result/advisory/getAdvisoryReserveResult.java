package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.po.advisory.AdvisoryPo;
import lombok.Data;

@Data
public class getAdvisoryReserveResult {
    private Integer id;
    private Integer doctorId;
    private Integer userId;
    private String name;
    private String avatarUrl;

    public getAdvisoryReserveResult(AdvisoryPo advisoryPo) {
        id = advisoryPo.getId();
        doctorId = advisoryPo.getDoctorId();
        userId = advisoryPo.getUserId();
        name = advisoryPo.getName();
        avatarUrl = advisoryPo.getAvatarUrl();
    }
}
