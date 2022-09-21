package com.sipc.xxsc.pojo.dto.param.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordParam {
    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    private String oldPassword;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    private String newPassword;
}
