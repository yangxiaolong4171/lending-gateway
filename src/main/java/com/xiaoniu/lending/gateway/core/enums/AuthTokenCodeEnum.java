package com.xiaoniu.lending.gateway.core.enums;

import com.xiaoniu.architecture.commons.api.IBusinessEnum;
import com.xiaoniu.architecture.commons.api.IEnum;
import com.xiaoniu.architecture.commons.api.IEnum;
import com.xiaoniu.architecture.commons.api.IEnum;
import com.xiaoniu.architecture.commons.api.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public enum AuthTokenCodeEnum implements IBusinessEnum<String> {

    AUTH_TOKEN_LENDINGAUTH("1108", "LENDING-AUTH-TOKEN", "lending-auth的token打开"),
    AUTH_TOKEN_LENDINGUSERCENTER("1109", "LENDING-USER-CENTER", "lending-user的token打开");

    private String code;
    private String description;
    private String desc;
    private static Map<String, AuthTokenCodeEnum> mappings = new HashMap();

    AuthTokenCodeEnum(String code, String description, String desc) {
        this.code = code;
        this.description = description;
        this.desc = desc;
    }


    static {
        Arrays.stream(AuthTokenCodeEnum.values())
                .forEach(authTokenCodeEnum -> mappings.put(authTokenCodeEnum.getValue(), authTokenCodeEnum));
    }

    public static AuthTokenCodeEnum resolve(String authdCode) {
        return mappings.get(authdCode);
    }

    @Override
    public boolean matches(String value) {
        if (null == value) {
            return false;
        }
        return Objects.equals(value, getValue());
    }

    @Override
    public boolean matches(IEnum<String> valueBean) {
        if (null == valueBean) {
            return false;
        }
        return matches(valueBean.getValue());
    }

    @Override
    public String getBusinessDesc() {
        return getDescription();
    }

    @Override
    public String getValue() {
        return getCode();
    }
}
