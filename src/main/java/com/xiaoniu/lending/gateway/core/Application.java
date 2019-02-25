package com.xiaoniu.lending.gateway.core;

import com.xiaoniu.architecture.commons.core.configuration.XiaoNiuCommonsCoreConfiguration;
import com.xiaoniu.architecture.commons.web.configuration.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 应用程序启动类
 *
 * @author SteveGuo
 * @date 2019-01-02 14:28 AM
 */
@SpringBootApplication(
        scanBasePackages = "com.xiaoniu.lending.gateway.core",
        exclude = {ErrorMvcAutoConfiguration.class}
)
@Import({WebMvcConfig.class, XiaoNiuCommonsCoreConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
