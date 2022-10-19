package com.sipc.xxsc.util.redis;

import lombok.Getter;

@Getter
public enum RedisEnum {
    BOOKPAGES("bookPages", 6),
    HOLLOWPAGES("hollowPages",5),
    HOLLOWCOMMENTPAGES("commentPages", 10),
    MOODPAGES("moodPages", 7),
    TESTPAGES("testPages", 8),
    DOCTORPAGES("doctorPage", 6);

    private String varName;
    private Integer pageSize;

    RedisEnum(String varName, Integer pageSize) {
        this.varName = varName;
        this.pageSize = pageSize;
    }
}
