package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Mood;

import java.util.List;

public interface MoodMapper {
    int insert(Mood mood);
    List<Mood> selectByUserId(Integer userId);
    Mood selectById(Integer id);
    int updateById(Mood mood);

    Integer selectCount();
}
