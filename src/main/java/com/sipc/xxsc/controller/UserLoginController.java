package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.login.ChangePasswordParam;
import com.sipc.xxsc.pojo.dto.param.login.LoginParam;
import com.sipc.xxsc.pojo.dto.param.login.RegisterParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.login.LoginResult;
import com.sipc.xxsc.service.UserLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
public class UserLoginController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    UserLoginService userLoginService;

    /**
     * @apiNote 用户注册
     * @param param 用户名、邮箱与密码
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public CommonResult<NoData> register(@Validated @RequestBody RegisterParam param){
        return userLoginService.register(request, response, param);
    }

    /**
     * @apiNote 用户登录
     * @param param 用户名、密码
     * @return 用户ID、电子邮件地址
     */
    @PostMapping("/login")
    public CommonResult<LoginResult> login(@Validated @RequestBody LoginParam param){
        return userLoginService.login(request, response, param);
    }

    /**
     * @apiNote 修改密码
     * @param param 旧密码、新密码
     * @return 是否修改成功
     */
    @PostMapping("/password")
    public CommonResult<NoData> changePassword(ChangePasswordParam param){
        return userLoginService.changePassword(request, response, param);
    }
}
