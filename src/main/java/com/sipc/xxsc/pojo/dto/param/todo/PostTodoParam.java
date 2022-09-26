package com.sipc.xxsc.pojo.dto.param.todo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PostTodoParam {
    @NotNull(message ="不能为空")
    @Min(value = 0, message = "Todo ID 不正确")
    private Integer id;
    @NotNull(message = "不能为空")
    @Range(min = 0, max = 1, message = "完成状态格式不正确")
    private Integer finish;
}