package com.sipc.xxsc.util.CheckRole.result;

import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import lombok.Data;

@Data
public class JWTCheckResult{
    private Integer userId;
    private String userName;
    private Boolean isDoctor;
    private boolean right;

    public JWTCheckResult(JWTPayloadParam jwtPayloadParam) {
        this.userId = jwtPayloadParam.getUserId();
        this.userName = jwtPayloadParam.getUserName();
        right = true;
    }

    public JWTCheckResult() {
        right = false;
    }
}
