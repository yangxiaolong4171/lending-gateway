package com.xiaoniu.lending.gateway.core.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 网关DTO
 *
 * @author guoqiang
 * @date 2019-01-02 6:17 PM
 */
@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class GatewayDTO implements Serializable {

    private static final long serialVersionUID = -661566525516745602L;

    private String name;

    private Integer age;

}
