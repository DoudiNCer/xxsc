package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.User;
import com.sipc.xxsc.pojo.domain.UserInfo;

public interface UserInfoMapper {
    int insert(UserInfo userInfo);

    UserInfo selectByUserId(Integer userId);

    int updateByPrimaryKey(UserInfo userInfo);

    User selectByUserName(String name);
}
