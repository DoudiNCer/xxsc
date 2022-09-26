package com.sipc.xxsc.pojo.dto.param.todo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PutTodoParam {
    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    @Size(max = 36, message = "Todo过长")
    private String todo;
}
