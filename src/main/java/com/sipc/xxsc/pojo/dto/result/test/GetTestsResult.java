package com.sipc.xxsc.pojo.dto.result.test;

import com.sipc.xxsc.pojo.domain.Test;
import lombok.Data;

@Data
public class GetTestsResult {
    private Integer id;
    private String name;
    private String photoUrl;
    private String introduce;
    String resourceUrl;

    public GetTestsResult(Test test) {
        id = test.getId();
        name = test.getName();
        photoUrl = test.getPhotoUrl();
        introduce = test.getIntroduce();
        resourceUrl = test.getResourceUrl();
    }
}
