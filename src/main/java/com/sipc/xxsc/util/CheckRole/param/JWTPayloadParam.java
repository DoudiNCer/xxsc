package com.sipc.xxsc.util.CheckRole.param;

import lombok.Data;

@Data
public class JWTPayloadParam {
    private Integer userId;
    private String userName;
}
