package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorDetailResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorsResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdvisoryService {
    CommonResult<GetDoctorsResult> getDoctors(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<GetDoctorDetailResult> getDoctor(HttpServletRequest request, HttpServletResponse response, Integer id);
}
