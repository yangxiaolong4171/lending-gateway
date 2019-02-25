package com.xiaoniu.lending.gateway.core.test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.SystemException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.springframework.util.Assert;

public class JSONUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JSONUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        Assert.hasLength(json, "JSON字符串不能为空");
        Assert.notNull(clazz, "转换的类不能为空");

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception var3) {
            throw new SystemException(ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), var3.getMessage());
        }
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        Assert.hasLength(json, "JSON字符串不能为空");
        Assert.notNull(typeReference, "泛型不能为空");

        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (Exception var3) {
            throw new SystemException(ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), var3.getMessage());
        }
    }

    public static String toJSONString(Object object) {
        Assert.notNull(object, "转换的对象不能为空");

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception var2) {
            throw new SystemException(ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), var2.getMessage());
        }
    }

    public static String toJSONStringWithPrettyPrinter(Object object) {
        Assert.notNull(object, "转换的对象不能为空");

        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception var2) {
            throw new SystemException(ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), var2.getMessage());
        }
    }

    public static JsonNode toJSONNode(String json) {
        Assert.hasLength(json, "JSON字符串不能为空");

        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException var2) {
            throw new SystemException(ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), var2.getMessage());
        }
    }

    public static String getStringValue(String json, String fieldName) {
        Assert.hasLength(fieldName, "指定JSON字符串中的属性名字不能为空");
        JsonNode jsonNode = toJSONNode(json);
        if (null != jsonNode) {
            JsonNode value = jsonNode.findValue(fieldName);
            if (null != value) {
                return value.asText();
            }
        }

        return "";
    }

    public static Integer getIntegerValue(String json, String fieldName) {
        Assert.hasLength(fieldName, "指定JSON字符串中的属性名字不能为空");
        JsonNode jsonNode = toJSONNode(json);
        if (null != jsonNode) {
            JsonNode value = jsonNode.findValue(fieldName);
            if (null != value) {
                return value.asInt();
            }
        }

        return null;
    }

    static {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        OBJECT_MAPPER.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OBJECT_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
}
