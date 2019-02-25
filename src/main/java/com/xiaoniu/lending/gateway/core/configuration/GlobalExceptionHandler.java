package com.xiaoniu.lending.gateway.core.configuration;

import com.alibaba.dubbo.rpc.RpcException;
import com.fasterxml.jackson.core.JsonParseException;
import com.xiaoniu.architecture.commons.api.ApiResultBean;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.BusinessException;
import com.xiaoniu.architecture.commons.api.exception.PlatformException;
import com.xiaoniu.architecture.commons.api.exception.SystemException;
import com.xiaoniu.architecture.commons.core.util.StringUtils;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.architecture.commons.web.util.BindingResultUtils;
import com.xiaoniu.architecture.commons.web.util.ConstraintViolationsUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * SpringMVC全局异常处理器
 *
 * @author SteveGuo
 * @date 2019-01-02 12:02 PM
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(com.xiaoniu.architecture.commons.web.advice.GlobalExceptionHandler.class);

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ApiResultBean handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        LOGGER.error("HttpRequestMethodNotSupportedException:{}", ex);
        String errorMessage = String.format("请求的HTTP方法应该为 %1$s ，不支持 %2$s ",
                org.springframework.util.StringUtils.arrayToCommaDelimitedString(ex.getSupportedMethods()), ex.getMethod());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ApiResultBean handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        LOGGER.error("HttpMediaTypeNotSupportedException:{}", ex);
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().parallelStream()
                .forEach(mediaType -> builder.append(mediaType.getType()).append("/").append(mediaType.getSubtype()));
        MediaType mediaType = ex.getContentType();
        String errorMessage = String.format("请求的Content-Type应该为 %1$s ，不支持 %2$s/%3$s", builder.toString(), mediaType.getType(),
                mediaType.getSubtype());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ApiResultBean handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        LOGGER.error("HttpMediaTypeNotAcceptableException:{}", ex);
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().parallelStream()
                .forEach(mediaType -> builder.append(mediaType.getType()).append("/").append(mediaType.getSubtype()));
        String errorMessage = String.format("请求的MediaType应该为 %1$s ，不支持 %2$s", builder.toString(), ex.getMessage());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ApiResultBean handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.error("HttpMessageNotReadableException：{} ", ex);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), "http消息不可读");
    }

    @ExceptionHandler(value = HttpMessageNotWritableException.class)
    public ApiResultBean handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        LOGGER.error("HttpMessageNotWritableException:{}", ex);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), "Http消息不可写");
    }

    @ExceptionHandler(value = MissingPathVariableException.class)
    public ApiResultBean handleMissingPathVariableException(MissingPathVariableException ex) {
        LOGGER.error("MissingPathVariableException:{}", ex);
        LOGGER.error("{}方法的{}参数的值为空", ex.getParameter().getMethod().getName(), ex.getVariableName());
        String errorMessage = String.format("请求参数%1$s的值为空", ex.getVariableName());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiResultBean handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        LOGGER.error("MissingServletRequestParameterException:{}", ex);
        LOGGER.error("{}类型的参数{}的值为空", ex.getParameterType(), ex.getParameterName());
        String errorMessage = String.format("请求参数%1$s的值为空", ex.getParameterName());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), errorMessage);
    }

    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public ApiResultBean handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        LOGGER.error("MissingServletRequestPartException:{}", ex);
        String errorMessage = String.format("文件上传丢失参数%1$s异常：%2$s", ex.getRequestPartName(), ex.getMessage());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), errorMessage);
    }

    @ExceptionHandler(value = ServletRequestBindingException.class)
    public ApiResultBean handleServletRequestBindingException(ServletRequestBindingException ex) {
        LOGGER.error("ServletRequestBindingException:{}", ex);
        String errorMessage = String.format("请求绑定异常:%1$s", ex.getMessage());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = ConversionNotSupportedException.class)
    public ApiResultBean handleConversionNotSupportedException(ConversionNotSupportedException ex) {
        LOGGER.error("ConversionNotSupportedException:{}", ex);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), "属性请求转换异常");
    }

    @ExceptionHandler(value = TypeMismatchException.class)
    public ApiResultBean handleTypeMismatchException(TypeMismatchException ex) {
        LOGGER.error("TypeMismatchException:{}", ex);
        String errorMessage = String.format("请求参数类型不匹配异常:%1$s", ex.getMessage());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResultBean handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        LOGGER.error("MethodArgumentTypeMismatchException：", exception);
        String errorMessage = exception.getMessage();
        if(StringUtils.isNotBlank(errorMessage)) {
            String placeHolder = "BusinessException:";
            errorMessage = errorMessage.substring(errorMessage.indexOf(placeHolder) + placeHolder.length()).replace
                    ("BusinessException:", "");
        }
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), "非法参数：" + errorMessage);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResultBean handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error("MethodArgumentNotValidException:{}", ex);
        LOGGER.error("{}方法的{}参数的未通过校验：{}", ex.getParameter().getMethod().getName(), ex.getParameter()
                .getParameterType().getSimpleName(), BindingResultUtils.getAllFieldErrorMessage(ex.getBindingResult()));
        String errorMessage = String.format("参数未通过校验：%1$s", BindingResultUtils.getAllFieldErrorMessage(ex.getBindingResult()));
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), errorMessage);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResultBean handleConstraintViolationException(ConstraintViolationException ex) {
        LOGGER.error("ConstraintViolationException:{}", ex);
        String errorMessage = String.format("输入的参数未通过校验：%1$s", ConstraintViolationsUtils.getAllErrorMessage(ex));
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.CONSTRAINT_VIOLATIONS_EXCEPTION.getCode(),
                errorMessage);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ApiResultBean handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.error("ConstraintViolationException:{}", ex);
        String errorMessage = String.format("非法参数：%1$s", ex.getMessage());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), errorMessage);
    }

    @ExceptionHandler(value = BindException.class)
    public ApiResultBean handleBindException(BindException ex) {
        LOGGER.error("BindException:{}", ex);
        String errorMessage = String.format("参数的未通过校验：%1$s", BindingResultUtils.getAllFieldErrorMessage(ex.getBindingResult()));
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_PARAM.getCode(), errorMessage);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ApiResultBean handleNoHandlerFoundException(NoHandlerFoundException ex) {
        LOGGER.error("NoHandlerFoundException:{}", ex);
        LOGGER.error("请求URL：{}，请求方法：{}不存在", ex.getRequestURL(), ex.getHttpMethod());
        String errorMessage = String.format("请求URL：%1$s不存在", ex.getRequestURL());
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.ILLEGAL_REQUEST.getCode(), errorMessage);
    }

    @ExceptionHandler(value = JsonParseException.class)
    public ApiResultBean handleJsonParseException(JsonParseException ex) {
        LOGGER.error("JsonParseException:{}", ex);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.JSON_PARSE_EXCEPTION.getCode(), "JSON解析异常");
    }

    @ExceptionHandler(value = IOException.class)
    public ApiResultBean handleIOException(IOException ex) {
        LOGGER.error("IOException:{}", ex);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.SYSTEM_EXCEPTION.getCode(), "IO异常");
    }

    @ExceptionHandler(value = RpcException.class)
    public ApiResultBean handleRpcException(RpcException rpcException) {
        String errorMessage = String.format("RpcException：code:%1$s，message: %2$s",
                rpcException.getCode(), rpcException.getMessage());
        LOGGER.error(errorMessage, rpcException);
        SystemException systemException;
        Throwable cause = rpcException.getCause();
        if (null != cause && cause instanceof ConstraintViolationException) {
            String message = ConstraintViolationsUtils.getAllErrorMessage((ConstraintViolationException) cause);
            systemException = new BusinessException(ResultCodeEnum.CONSTRAINT_VIOLATIONS_EXCEPTION.getCode(), message);
        } else {
            systemException = new SystemException(ResultCodeEnum.RPC_EXCEPTION);
        }
        return ApiResultBean.failure(HeaderHelper.getRequestId(), systemException);
    }

    @ExceptionHandler(value = PlatformException.class)
    public ApiResultBean handlePlatformException(PlatformException platformException) {
        String errorMessage = String.format("PlatformException：code:%1$s，message: %2$s",
                platformException.getCode(), platformException.getMessage());
        LOGGER.error(errorMessage, platformException);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), platformException);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ApiResultBean handleBusinessException(BusinessException businessException) {
        String errorMessage = String.format("BusinessException：code:%1$s，message: %2$s",
                businessException.getCode(), businessException.getMessage());
        LOGGER.error(errorMessage, businessException);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), businessException);
    }

    @ExceptionHandler(value = SystemException.class)
    public ApiResultBean handleSystemException(SystemException systemException) {
        String errorMessage = String.format("SystemException：code:%1$s，message: %2$s",
                systemException.getCode(), systemException.getMessage());
        LOGGER.error(errorMessage, systemException);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), systemException);
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResultBean handleException(Exception exception) {
        LOGGER.error("exception：", exception);
        return ApiResultBean.failure(HeaderHelper.getRequestId(), ResultCodeEnum.SYSTEM_EXCEPTION.getCode(),
                "系统内部错误");
    }
}
