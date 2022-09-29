package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorDetailResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorsResult;
import com.sipc.xxsc.service.AdvisoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
