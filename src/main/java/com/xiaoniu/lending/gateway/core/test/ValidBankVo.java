package com.xiaoniu.lending.gateway.core.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ValidBankVo implements Serializable {
    @JsonProperty(value = "bank_name")
    private String bankName;
    @JsonProperty(value = "bank_code")
    private String bankCode;
    @JsonProperty(value = "bank_title")
    private String bankTitle;
    @JsonProperty(value = "bank_icon")
    private String bankIcon;

}
