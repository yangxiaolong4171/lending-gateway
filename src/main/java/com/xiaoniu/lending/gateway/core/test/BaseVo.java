package com.xiaoniu.lending.gateway.core.test;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseVo implements Serializable {
    private Integer status;
    private String message;

}
