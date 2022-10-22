package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Advisory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvisoryMapper {
    Advisory selectByUserIdAndDoctorId(@Param("userId") Integer userId, @Param("doctorId") Integer doctorId);

    int insert(Advisory advisory);

    List<Advisory> selectByDoctorId(Integer DoctorId);
    List<Advisory> selectByUserId(Integer UserId);

    Advisory selectById(Integer advisoryId);
}
