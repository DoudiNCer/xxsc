package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.userInfo.PostUserInfoParam;
import com.sipc.xxsc.pojo.dto.result.GetUserInfoResult;
import com.sipc.xxsc.pojo.dto.result.NoData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserInfoService {
    CommonResult<GetUserInfoResult> getUserInfo(HttpServletRequest request, HttpServletResponse response);

    CommonResult<NoData> postUserInfo(HttpServletRequest request, HttpServletResponse response, PostUserInfoParam param);
}
