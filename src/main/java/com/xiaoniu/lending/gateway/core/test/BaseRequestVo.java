package com.xiaoniu.lending.gateway.core.test;

import lombok.Data;
import java.io.Serializable;

@Data
public class BaseRequestVo implements Serializable {
     private String ua;
     private String call;
     private String sign;
     private String timestamp;
     private Object args;

}
