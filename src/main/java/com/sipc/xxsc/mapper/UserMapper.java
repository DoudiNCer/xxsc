package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.User;

public interface UserMapper {
    int insert(User user);

    User selectByPrimaryKey(Integer id);

    User selectByUserName(String name);

    int updatePasswordByPrimaryKey(int id, String password);

    int updateByPrimaryKey(User user);
}
