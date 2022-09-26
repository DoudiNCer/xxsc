package com.sipc.xxsc.util.CheckRole;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class CheckRole {
    public static CommonResult<JWTCheckResult> check(HttpServletRequest request, HttpServletResponse response){
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.length() == 0)
            return CommonResult.userLoginExpired();
        JWTCheckResult checkResult = JWTUtils.checkToken(authorization);
        if (!checkResult.isRight())
            return CommonResult.userAuthError();
        return CommonResult.success(checkResult);
    }
    public static CommonResult<NoData> init(HttpServletRequest request, HttpServletResponse response, JWTPayloadParam param){
        String token = JWTUtils.getToken(param);
        response.addHeader("Authorization", token);
        return CommonResult.success();
    }
}
