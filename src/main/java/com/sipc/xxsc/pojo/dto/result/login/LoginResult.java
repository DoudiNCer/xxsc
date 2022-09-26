package com.sipc.xxsc.pojo.dto.result.login;

import lombok.Data;

@Data
public class LoginResult {
    private Integer userId;
    private String email;
    private String token;
}
