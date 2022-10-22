package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.domain.Advisory;
import lombok.Data;

@Data
public class getAdvisoryReserveResult {
    private Integer id;
    private Integer doctorId;
    private Integer userId;

    public getAdvisoryReserveResult(Advisory advisory) {
        id = advisory.getId();
        doctorId = advisory.getDoctorId();
        userId = advisory.getUserId();
    }
}
