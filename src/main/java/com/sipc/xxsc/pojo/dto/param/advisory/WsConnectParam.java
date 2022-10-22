package com.sipc.xxsc.pojo.dto.param.advisory;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WsConnectParam {
    @NotBlank(message = "不能为空")
    private String token;
    @NotNull(message = "不能为空")
    @Min(value = 1, message = "格式不正确")
    private Integer advisoryId;
}
