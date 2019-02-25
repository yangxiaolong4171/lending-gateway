package com.xiaoniu.lending.gateway.core.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public enum BussinesResultCodeEnum {

    OTHER_DEVICE_LOGGING_IN("1102", "您的账号已在其他地方登录");

    private String code;
    private String description;
    private static Map<String, String> mappings = new HashMap();

     BussinesResultCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public boolean matches(String code) {
        return Objects.equals(this.getCode(), code);
    }

    static {
        Arrays.stream(values()).forEach((resultCodeEnum) -> {
            String var10000 = (String)mappings.put(resultCodeEnum.getCode(), resultCodeEnum.getDescription());
        });
    }
}
