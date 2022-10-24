package com.sipc.xxsc.pojo.dto.param.advisory;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendMessageParam {
    @NotBlank(message = "不能为空")
    private String message;
}
