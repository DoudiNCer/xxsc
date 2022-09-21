package com.sipc.xxsc.pojo.dto.param.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterParam {
    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    private String userName;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    @Email(message = "格式不正确")
    private String email;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    private String password;
}
