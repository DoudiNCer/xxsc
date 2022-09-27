package com.sipc.xxsc.pojo.dto.param.hollow;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PutCommentsParam {
    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    @Size(max = 50, message = "过长")
    private String comment;
    @NotNull(message = "不能为空")
    private Integer id;
}
