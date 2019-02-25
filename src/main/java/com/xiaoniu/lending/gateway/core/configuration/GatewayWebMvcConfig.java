package com.xiaoniu.lending.gateway.core.configuration;

import com.xiaoniu.lending.gateway.core.interceptor.AuthenticationInterceptor;
import com.xiaoniu.lending.gateway.core.interceptor.SignatureInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 网关mvc配置
 *
 * @author guoqiang
 * @date 2019-01-05 5:18 PM
 */
@Configuration
public class GatewayWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SignatureInterceptor signatureInterceptor;
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signatureInterceptor).order(3);
        registry.addInterceptor(authenticationInterceptor).order(4);
    }


}
