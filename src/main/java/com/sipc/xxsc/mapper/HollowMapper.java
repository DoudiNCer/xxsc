package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Hollow;
import com.sipc.xxsc.pojo.po.HollowPo;

import java.util.List;

public interface HollowMapper {
    Integer selectCount();

    int insert(Hollow hollow);

    List<HollowPo> selectHollows();

    Hollow selectById(Integer id);
}
