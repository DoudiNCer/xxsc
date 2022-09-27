package com.sipc.xxsc.pojo.dto.result.hollow;

import lombok.Data;

@Data
public class CommentResult {
    private Integer id;
    private String user;
    private String comment;
    private String time;
}
