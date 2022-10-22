package com.sipc.xxsc.pojo.dto.param.talk;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendTalkParam {
    @NotBlank(message = "不能为空")
    private String message;
}
