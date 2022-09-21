package com.sipc.xxsc.util.CheckRole;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class CheckRole {
    public static CommonResult<JWTCheckResult> check(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = null;
        for (Cookie requestCookie : request.getCookies()) {
            if (Objects.equals(requestCookie.getName(), "token"))
                cookie = requestCookie;
        }
        if (cookie == null)
            return CommonResult.userLoginExpired();
        JWTCheckResult checkResult = JWTUtils.checkToken(cookie.getValue());
        if (!checkResult.isRight())
            return CommonResult.userAuthError();
        return CommonResult.success(checkResult);
    }
    public static CommonResult<NoData> init(HttpServletRequest request, HttpServletResponse response, JWTPayloadParam param){
        String token = JWTUtils.getToken(param);
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
        return CommonResult.success();
    }
}
