package com.xiaoniu.lending.gateway.core.support;

import com.xiaoniu.architecture.commons.api.ApiResultBean;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.BusinessException;
import com.xiaoniu.architecture.commons.core.util.JSONUtils;
import com.xiaoniu.lending.gateway.core.enums.BussinesResultCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应帮助类
 *
 * @author guoqiang
 * @date 2019-01-02 11:48 AM
 */
@Log4j2
public class ResponseHelper {

    /**
     * 写入JSON到响应体里面
     * @param response
     * @param resultCodeEnum
     */
    public static void writeJSONResponseBody(HttpServletResponse response, ResultCodeEnum resultCodeEnum)  {
        ApiResultBean apiResultBean = ApiResultBean.failure(System.currentTimeMillis() + "",
                resultCodeEnum.getCode(), resultCodeEnum.getDescription());
        String json = JSONUtils.toJSONString(apiResultBean);
        writeJSONResponseBody(response, json);
    }

    /**
     * 写入JSON到响应体里面(业务权限)
     * @param response
     * @param bussinesResultCodeEnum
     */
    public static void writeJSONResponseBodyForBussniess(HttpServletResponse response, BussinesResultCodeEnum bussinesResultCodeEnum)  {
        ApiResultBean apiResultBean = ApiResultBean.failure(System.currentTimeMillis() + "",
                bussinesResultCodeEnum.getCode(), bussinesResultCodeEnum.getDescription());
        String json = JSONUtils.toJSONString(apiResultBean);
        writeJSONResponseBody(response, json);
    }

    /**
     * 写入JSON到响应体里面
     * @param response
     * @param businessException
     */
    public static void writeJSONResponseBody(HttpServletResponse response, BusinessException businessException)  {
        ApiResultBean apiResultBean = ApiResultBean.failure(System.currentTimeMillis() + "", businessException);
        String json = JSONUtils.toJSONString(apiResultBean);
        writeJSONResponseBody(response, json);
    }

    /**
     * 写入JSON到响应体里面
     * @param response
     * @param json
     */
    public static void writeJSONResponseBody(HttpServletResponse response, String json)  {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(json.getBytes("UTF-8"));
        }catch (Exception e) {
            log.error("写入JSON响应体异常:{}", e.getMessage());
        }
    }

}
