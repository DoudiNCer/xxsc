package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.test.GetTestsResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TestService {
    CommonResult<List<GetTestsResult>> getTests(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<Pages> getTestPages(HttpServletRequest request, HttpServletResponse response);
}
