package com.sipc.xxsc.pojo.domain;

import lombok.Data;

@Data
public class Book {
    private Integer id;
    private String author;
    private String resourceUrl;
    private String photoUrl;
    private String name;
    private String introduce;
}
