package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Advisory;
import com.sipc.xxsc.pojo.po.advisory.AdvisoryPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvisoryMapper {
    Advisory selectByUserIdAndDoctorId(@Param("userId") Integer userId, @Param("doctorId") Integer doctorId);

    int insert(Advisory advisory);

    List<AdvisoryPo> selectByDoctorId(Integer DoctorId);
    List<AdvisoryPo> selectByUserId(Integer UserId);

    Advisory selectById(Integer advisoryId);
}
