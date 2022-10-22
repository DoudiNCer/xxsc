package com.sipc.xxsc.pojo.dto.param.advisory;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReserveParam {
    @NotNull(message = "不能为空")
    private Integer doctorId;
}
