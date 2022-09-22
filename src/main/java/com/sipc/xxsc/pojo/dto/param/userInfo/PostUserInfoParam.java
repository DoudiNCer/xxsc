package com.sipc.xxsc.pojo.dto.param.userInfo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PostUserInfoParam {
    @NotNull(message = "不能为空")
    private Integer userId;

    private String userName;

    @Range(min = 0, max = 1, message = "性别代码格式错误")
    private Integer gender;

    private String email;
}
