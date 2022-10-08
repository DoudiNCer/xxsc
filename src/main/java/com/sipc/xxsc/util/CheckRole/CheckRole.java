package com.sipc.xxsc.util.CheckRole;

import com.sipc.xxsc.mapper.UserMapper;
import com.sipc.xxsc.pojo.domain.User;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class CheckRole {
    @Resource
    private UserMapper userMapper;
    public static CheckRole checkRole;

    @PostConstruct
    public void init() {
        checkRole = this;
        checkRole.userMapper = this.userMapper;
    }

    public static CommonResult<JWTCheckResult> check(HttpServletRequest request, HttpServletResponse response){
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.length() == 0)
            return CommonResult.userLoginExpired();
        JWTCheckResult checkResult = JWTUtils.checkToken(authorization);
        if (!checkResult.isRight())
            return CommonResult.userAuthError();
        User user = checkRole.userMapper.selectByPrimaryKey(checkResult.getUserId());
        if (user == null)
            return CommonResult.userAuthError("用户不存在");
        return CommonResult.success(checkResult);
    }
    public static CommonResult<NoData> init(HttpServletRequest request, HttpServletResponse response, JWTPayloadParam param){
        String token = JWTUtils.getToken(param);
        response.addHeader("Authorization", token);
        return CommonResult.success();
    }
}
