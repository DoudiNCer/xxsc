package com.sipc.xxsc.pojo.dto.param.mood;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PutMoodParam {
    @NotNull(message = "不能为空")
    @Range(min = 0, max = 2, message = "心情代号格式不正确")
    private Integer mood;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空")
    @Size(max = 100, message = "Message 过长")
    private String message;
}
