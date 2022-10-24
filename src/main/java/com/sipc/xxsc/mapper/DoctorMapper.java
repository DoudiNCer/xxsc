package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.po.advisory.DoctorDetailPo;
import com.sipc.xxsc.pojo.po.advisory.DoctorSummaryPo;

import java.util.List;

public interface DoctorMapper {
    List<DoctorSummaryPo> selectDoctors(Integer userId);

    Integer selectCount();

    DoctorDetailPo selectById(Integer id);
}
