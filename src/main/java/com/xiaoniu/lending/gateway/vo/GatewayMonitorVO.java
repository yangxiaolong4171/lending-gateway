package com.xiaoniu.lending.gateway.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 运维监控
 * @author liuyinkai
 */
@Getter
@Setter
@ToString
public class GatewayMonitorVO implements Serializable {

    private String status;

}
