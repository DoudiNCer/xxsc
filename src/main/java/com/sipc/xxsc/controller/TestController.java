package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.test.GetTestsResult;
import com.sipc.xxsc.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    TestService testService;

    @GetMapping("/tests")
    public CommonResult<List<GetTestsResult>> getTests(
        @RequestParam(value = "page", defaultValue = "1") Integer page
    ){
        return testService.getTests(request, response, page);
    }

    @GetMapping("/pages")
    public CommonResult<Pages> getTestPages(){
        return testService.getTestPages(request, response);
    }

}
