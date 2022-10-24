package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.po.advisory.DoctorSummaryPo;
import lombok.Data;

@Data
public class DoctorSummaryResult {
    private Boolean reserved;
    private Integer id;
    private String avatarUrl;
    private String name;
    private String goodat;

    public DoctorSummaryResult(DoctorSummaryPo po) {
        this.avatarUrl = po.getAvatarUrl();
        this.id = po.getId();
        this.goodat = po.getGoodat();
        this.name = po.getName();
        this.reserved = po.getReserved();
    }
}
