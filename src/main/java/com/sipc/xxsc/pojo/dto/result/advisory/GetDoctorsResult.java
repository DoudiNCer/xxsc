package com.sipc.xxsc.pojo.dto.result.advisory;

import lombok.Data;

import java.util.List;

@Data
public class GetDoctorsResult {
    private Integer pages;
    private List<DoctorSummaryResult> doctors;
}
