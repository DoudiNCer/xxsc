package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Test;

import java.util.List;

public interface TestMapper {
    Integer selectCount();

    List<Test> selectTests();
}
