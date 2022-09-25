package com.sipc.xxsc.service.Impl;

import com.sipc.xxsc.mapper.UserInfoMapper;
import com.sipc.xxsc.mapper.UserMapper;
import com.sipc.xxsc.pojo.domain.User;
import com.sipc.xxsc.pojo.domain.UserInfo;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.login.ChangePasswordParam;
import com.sipc.xxsc.pojo.dto.param.login.LoginParam;
import com.sipc.xxsc.pojo.dto.param.login.RegisterParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.login.LoginResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.UserLoginService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.PasswordUtils;
import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Resource
    UserMapper userMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    /**
     * @param request HTTP请求
     * @param response HTTP响应
     * @param param 用户名，密码，电子邮件信箱
     * @return 注册结果
     */
    @Override
    public CommonResult<NoData> register(HttpServletRequest request, HttpServletResponse response, RegisterParam param) {
        if (userMapper.selectByUserName(param.getUserName()) != null) {
            return CommonResult.fail("用户已存在");
        }
        User user = new User();
        user.setEmail(param.getEmail());
        user.setName(param.getUserName());
        user.setEmail(param.getEmail());
        user.setPassword(PasswordUtils.getsPasswd(param.getPassword()));
        userMapper.insert(user);
        user = userMapper.selectByUserName(param.getUserName());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfoMapper.insert(userInfo);
        return CommonResult.success();
    }

    /**
     * @param request HTTP请求
     * @param response HTTP响应
     * @param param 用户名与密码
     * @return 用户ID、电子邮件地址
     */
    @Override
    public CommonResult<LoginResult> login(HttpServletRequest request, HttpServletResponse response, LoginParam param) {
        User user = userMapper.selectByUserName(param.getUserName());
        if (user == null)
            return CommonResult.userResourceException("用户不存在");
        if (!PasswordUtils.checkPasswd(param.getPassword(), user.getPassword()))
            return CommonResult.userPasswordWrong();
        LoginResult result = new LoginResult();
        result.setUserId(user.getId());
        result.setEmail(user.getEmail());
        JWTPayloadParam payload = new JWTPayloadParam();
        payload.setUserId(user.getId());
        payload.setUserName(user.getName());
        CommonResult<NoData> init = CheckRole.init(request, response, payload);
        if (!Objects.equals(init.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(init.getCode(), init.getMessage());
        return CommonResult.success(result);
    }

    /**
     * @param request HTTP请求
     * @param response HTTP响应
     * @param param 旧密码、新密码
     * @return 修改结果
     */
    @Override
    public CommonResult<NoData> changePassword(HttpServletRequest request, HttpServletResponse response, ChangePasswordParam param) {
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        User user = userMapper.selectByPrimaryKey(check.getData().getUserId());
        if (user == null)
            return CommonResult.serverError();
        if (!PasswordUtils.checkPasswd(param.getOldPassword(), user.getPassword()))
            return CommonResult.userPasswordWrong();
        userMapper.updatePasswordByPrimaryKey(user.getId(), PasswordUtils.getsPasswd(param.getNewPassword()));
        return CommonResult.success();
    }
}
