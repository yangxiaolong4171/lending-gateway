package com.xiaoniu.lending.gateway.core.interceptor;

import com.xiaoniu.architecture.commons.api.Header;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.BusinessException;
import com.xiaoniu.architecture.commons.core.signature.SignatureUtils;
import com.xiaoniu.architecture.commons.core.util.StringUtils;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.architecture.commons.web.util.PathMatchUtils;
import com.xiaoniu.architecture.spring.boot.autoconfigure.security.access.AccessProperties;
import com.xiaoniu.architecture.spring.boot.autoconfigure.security.app.AppAccount;
import com.xiaoniu.architecture.spring.boot.autoconfigure.security.app.AppAccountProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 签名拦截器
 *
 * @author guoqiang
 * @date 2019-01-05 5:21 PM
 */
@Log4j2
@Component
public class SignatureInterceptor extends HandlerInterceptorAdapter {

    private static final List<String> IGNORE_PROPERTIES;
    static {
        IGNORE_PROPERTIES = new ArrayList<>();
        IGNORE_PROPERTIES.add(StringUtils.convertDelimited2Hump(Header.NameEnum.CUSTOMER_ID.getName(), "-"));
        IGNORE_PROPERTIES.add(StringUtils.convertDelimited2Hump(Header.NameEnum.ACCESS_TOKEN.getName(), "-"));
    }

    @Value("${xiaoniu.gateway.signature.enabled:true}")
    private Boolean gatewaySignatureEnabled;

    @Autowired
    private AccessProperties accessProperties;
    @Autowired
    private AppAccountProperties appAccountProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!gatewaySignatureEnabled) {
            log.info("网关的签名验证开关已关闭");
            return true;
        }

        String servletPath = request.getServletPath();
        if(PathMatchUtils.anyMatch(servletPath, accessProperties.getNoneCheckHeaderUris())) {
            log.info("SignatureFilter --> 对servletPath:{}不校验签名", servletPath);
            return true;
        }
        try {
            HeaderHelper.checkHeader();
        } catch (Exception e) {
            log.error("请求头参数异常：{}", e.getMessage());
            throw new BusinessException(ResultCodeEnum.ILLEGAL_PARAM);
        }
        Integer requestAgent = HeaderHelper.getRequestAgent();
        Header.RequestAgentEnum requestAgentEnum = Header.RequestAgentEnum.resolve(requestAgent);
        if (null == requestAgentEnum) {
            log.error("APP传进来的requestAgent:{}不在允许服务端允许范围内", HeaderHelper.getRequestAgent());
            throw new BusinessException(ResultCodeEnum.ILLEGAL_REQUEST_AGENT);
        }
        String key = requestAgentEnum.name().toLowerCase() + "-" + HeaderHelper.getAppName() + "-" + HeaderHelper.getAppVersion();
        AppAccount appAccount = appAccountProperties.getAppAcount(key);
        if (null == appAccount || StringUtils.isBlank(appAccount.getAppId())
                || StringUtils.isBlank(appAccount.getAppSecret())) {
            throw new BusinessException(ResultCodeEnum.NONE_APP_ACCOUNT_DATA);
        }
        if (!appAccount.getAppId().equals(HeaderHelper.getAppId())) {
            log.error("APP传进来的appId：{}与服务端appId：{}不一致", HeaderHelper.getAppId(), appAccount.getAppId());
            throw new BusinessException(ResultCodeEnum.ILLEGAL_APP_ID);
        }
        String stringToSign = HeaderHelper.getAppId() + HeaderHelper.getTimestamp();
        String targetSign = SignatureUtils.hashByHmacSHA256(stringToSign, appAccount.getAppSecret());
        if (!targetSign.equals(HeaderHelper.getSign())) {
            log.error("校验AppId：{}，时间戳：{}的签名：{}失败，服务器生成的签名是：{}", HeaderHelper.getAppId(),
                    HeaderHelper.getTimestamp(), HeaderHelper.getSign(), targetSign);
            throw new BusinessException(ResultCodeEnum.ILLEGAL_SIGNATURE);
        }
        return true;
    }



}
