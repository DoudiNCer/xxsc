package com.sipc.xxsc.pojo.dto.result;

import lombok.Data;

@Data
public class GetUserInfoResult {
    private Integer userId;
    private String userName;
    private String email;
    private Integer gender;
    private String avatarUrl;
}
