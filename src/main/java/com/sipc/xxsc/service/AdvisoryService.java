package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.ReserveParam;
import com.sipc.xxsc.pojo.dto.result.advisory.AdvisoryReserveResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorDetailResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorsResult;
import com.sipc.xxsc.pojo.dto.result.advisory.getAdvisoryReserveResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AdvisoryService {
    CommonResult<GetDoctorsResult> getDoctors(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<GetDoctorDetailResult> getDoctor(HttpServletRequest request, HttpServletResponse response, Integer id);

    CommonResult<AdvisoryReserveResult> reserve(HttpServletRequest request, HttpServletResponse response, ReserveParam param);

    CommonResult<List<getAdvisoryReserveResult>> getAdvisoryReserve(HttpServletRequest request, HttpServletResponse response);
}
