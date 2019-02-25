package com.xiaoniu.lending.gateway.core.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.BusinessException;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.architecture.commons.web.util.PathMatchUtils;
import com.xiaoniu.architecture.spring.boot.autoconfigure.security.access.AccessProperties;
import com.xiaoniu.lending.auth.api.business.UserBaseAuthBusiness;
//import com.xiaoniu.lending.auth.api.enums.AuthBusinessCodeEnum;
//import com.xulu.usercenter.ifc.UserAuthorizeIfc;
import com.xiaoniu.lending.auth.api.enums.AuthBusinessCodeEnum;
import com.xiaoniu.lending.gateway.core.enums.AuthTokenCodeEnum;
import com.xulu.usercenter.ifc.UserAuthorizeIfc;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 *
 * @author guoqiang
 * @date 2019-01-05 5:21 PM
 */
@Log4j2
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Value("${xiaoniu.gateway.authentication.enabled:true}")
    private boolean gatewayAuthenticationEnabled;
    @Value("${xiaoniu.gateway.authentication.token}")
    private String gatewayAuthenticationToken;

    @Autowired
    private AccessProperties accessProperties;
    @Reference(version = "1.0.0", retries = -1)
    private UserBaseAuthBusiness userBaseAuthBusiness;
    @Reference(group = "lendingUserCenter", version = "1.0.0", retries = -1)
    private UserAuthorizeIfc userAuthorizeIfc;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!gatewayAuthenticationEnabled) {
            log.info("网关的认证验证开关已关闭");
            return true;
        }

        String servletPath = request.getServletPath();
        if (PathMatchUtils.anyMatch(servletPath, accessProperties.getIgnoreHeaderAndTokenUrls())) {
            log.info("AuthenticationFilter --> 对servletPath:{}不鉴权", servletPath);
            return true;
        }
        try {
            HeaderHelper.checkCustomerIdAndAccessToken();
        } catch (Exception e) {
            log.error("请求头参数异常：{}", e.getMessage());
            throw new BusinessException(ResultCodeEnum.ILLEGAL_PARAM);
        }
        if (PathMatchUtils.anyMatch(servletPath, accessProperties.getIgnoreTokenUris())) {
            log.info("AuthenticationFilter --> 对servletPath:{}不鉴权", servletPath);
            return true;
        }

        try {
            //调用认证接口
            String accessToken = HeaderHelper.getAccessToken();
            String customerId = HeaderHelper.getCustomerId();
            log.info("请求头参数accessToken：{},customerId：{}", accessToken, customerId);
            String authType = "";
            if (AuthTokenCodeEnum.AUTH_TOKEN_LENDINGAUTH.getBusinessDesc().equals(gatewayAuthenticationToken)) {
                authType = userBaseAuthBusiness.AuthorizeService(customerId, accessToken);
            }
            else if (AuthTokenCodeEnum.AUTH_TOKEN_LENDINGUSERCENTER.getBusinessDesc().equals(gatewayAuthenticationToken)) {
                authType = userAuthorizeIfc.AuthorizeService(accessToken, customerId);
            }

            if (AuthBusinessCodeEnum.TOKEN_NULL_LOGGING_IN.getValue().equals(authType)) {
                throw new BusinessException(AuthBusinessCodeEnum.TOKEN_NULL_LOGGING_IN);
            }else if (AuthBusinessCodeEnum.OTHER_DEVICE_LOGGING_IN.getValue().equals(authType)) {
                throw new BusinessException(ResultCodeEnum.ACCESS_TOKEN_EXPIRED);
            }

        } catch (BusinessException be) {
            log.error("校验AccessToken异常：{}", be.getMessage());
            throw new BusinessException(ResultCodeEnum.ILLEGAL_ACCESS_TOKEN);
        }
        return true;
    }
}
