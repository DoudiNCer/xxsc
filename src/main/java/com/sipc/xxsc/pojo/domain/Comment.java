package com.sipc.xxsc.pojo.domain;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer storyId;
    private Integer userId;
    private Long time;
    private String comment;
}
