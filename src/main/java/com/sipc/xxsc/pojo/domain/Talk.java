package com.sipc.xxsc.pojo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Talk implements Serializable {
    private static final long serialVersionUID = 130903199301110624L;
    private Integer id;
    private Integer mood;
    private Integer type;
    private String key;
    private String value;
}
