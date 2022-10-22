package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.ReserveParam;
import com.sipc.xxsc.pojo.dto.result.advisory.AdvisoryReserveResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorDetailResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorsResult;
import com.sipc.xxsc.pojo.dto.result.advisory.getAdvisoryReserveResult;
import com.sipc.xxsc.service.AdvisoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 心理咨询Controller
 */
@RestController
@RequestMapping("/advisory")
public class AdvisoryController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    AdvisoryService advisoryService;

    @GetMapping("/doctors")
    CommonResult<GetDoctorsResult> getDoctors(
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ){
        return advisoryService.getDoctors(request, response, page);
    }

    @GetMapping("/doctor")
    CommonResult<GetDoctorDetailResult> getDoctor(
            @RequestParam(value = "id") Integer id
    ){
        return advisoryService.getDoctor(request, response, id);
    }

    @PutMapping("/reserve")
    CommonResult<AdvisoryReserveResult> reserve(@Validated @RequestBody ReserveParam param){
        return advisoryService.reserve(request, response, param);
    }
    @GetMapping("/reserve")
    CommonResult<List<getAdvisoryReserveResult>> getAdvisoryReserve(){
        return advisoryService.getAdvisoryReserve(request, response);
    }

}
