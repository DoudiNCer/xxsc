package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.userInfo.PostUserInfoParam;
import com.sipc.xxsc.pojo.dto.result.GetUserInfoResult;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    UserInfoService userInfoService;

    @PostMapping("/info")
    public CommonResult<NoData> postUserInfo(@Validated @RequestBody PostUserInfoParam param){
        return userInfoService.postUserInfo(request, response, param);
    }

    @GetMapping(".info")
    public CommonResult<GetUserInfoResult> getUserInfo(){
        return userInfoService.getUserInfo(request, response);
    }
}
