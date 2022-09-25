package com.sipc.xxsc.service.Impl;

import com.sipc.xxsc.mapper.UserInfoMapper;
import com.sipc.xxsc.mapper.UserMapper;
import com.sipc.xxsc.pojo.domain.User;
import com.sipc.xxsc.pojo.domain.UserInfo;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.userInfo.PostUserInfoParam;
import com.sipc.xxsc.pojo.dto.result.GetUserInfoResult;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.UserInfoService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    UserMapper userMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    /**
     * @apiNote 获取用户信息
     * @return 用户ID、姓名、电子邮件地址、性别、头像
     */
    @Override
    public CommonResult<GetUserInfoResult> getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        User user = userMapper.selectByPrimaryKey(check.getData().getUserId());
        if (user == null)
            return CommonResult.userResourceException("用户不存在");
        UserInfo userInfo = userInfoMapper.selectByUserId(user.getId());
        if (userInfo == null)
            return CommonResult.serverError();
        GetUserInfoResult result = new GetUserInfoResult();
        result.setUserId(user.getId());
        result.setUserName(user.getName());
        result.setAvatarUrl(userInfo.getAvatarUrl());
        result.setEmail(user.getEmail());
        result.setGender(userInfo.getGender());
        return CommonResult.success(result);
    }

    /**
     * @apiNote 修改用户信息
     * @param param 用户ID与要修改的新信息
     * @return 修改结果
     */
    @Override
    public CommonResult<NoData> postUserInfo(HttpServletRequest request, HttpServletResponse response, PostUserInfoParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        User user = userMapper.selectByPrimaryKey(check.getData().getUserId());
        if (user == null)
            return CommonResult.userResourceException("用户不存在");
        UserInfo userInfo = userInfoMapper.selectByUserId(user.getId());
        if (userInfo == null)
            return CommonResult.serverError();
        User newUser = new User();
        newUser.setId(param.getUserId());
        newUser.setEmail(param.getEmail());
        newUser.setName(param.getUserName());
        if(userInfoMapper.selectByUserName(param.getUserName()) != null)
            return CommonResult.fail("用户已存在");
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUserId(param.getUserId());
        newUserInfo.setGender(param.getGender());
        userMapper.updateByPrimaryKey(newUser);
        userInfoMapper.updateByPrimaryKey(newUserInfo);
        return null;
    }
}
