package com.sipc.xxsc.util.WebSocketUtils;

import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import lombok.Getter;

@Getter
public enum AttributesKeys {
    ISDOCTOR("ISDOCTOR", Boolean.class),
    DOCISAI("DOCISAI", Boolean.class),
    USER("USER", Integer.class),
    ADV("ADVISORYID", Integer.class),
    DOCTOR("DOCTORID", Integer.class);
    private String name;
    private Class type;

    AttributesKeys(String name, Class type) {
        this.name = name;
        this.type = type;
    }
}
