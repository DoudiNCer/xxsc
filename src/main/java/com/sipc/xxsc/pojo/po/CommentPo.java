package com.sipc.xxsc.pojo.po;

import lombok.Data;

@Data
public class CommentPo{
    private Integer id;
    private Integer storyId;
    private String user;
    private Long time;
    private String comment;
}
