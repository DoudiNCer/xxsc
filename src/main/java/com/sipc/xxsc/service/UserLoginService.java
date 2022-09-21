package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.login.ChangePasswordParam;
import com.sipc.xxsc.pojo.dto.param.login.LoginParam;
import com.sipc.xxsc.pojo.dto.param.login.RegisterParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.login.LoginResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserLoginService {
    CommonResult<NoData> register(HttpServletRequest request, HttpServletResponse response, RegisterParam param);

    CommonResult<LoginResult> login(HttpServletRequest request, HttpServletResponse response, LoginParam param);

    CommonResult<NoData> changePassword(HttpServletRequest request, HttpServletResponse response, ChangePasswordParam param);
}
