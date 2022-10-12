package com.sipc.xxsc.pojo.dto.param.advisory;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendMessageParam {
    @NotNull(message = "不能为空")
    @Min(value = 1, message = "对象ID格式不正确")
    private Integer objectId;
    @NotBlank(message = "不能为空")
    private String message;
}
