package com.sipc.xxsc.util.WebSocketUtils.result;

import lombok.Data;

@Data
public class ParseAttributesResult {
    private Boolean docIsAi;
    private Boolean isDoctor;
    private Integer advisoryId;
    private Integer userId;
    private Integer doctorId;
}
