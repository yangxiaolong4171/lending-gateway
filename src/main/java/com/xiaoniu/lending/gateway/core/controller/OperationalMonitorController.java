package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.lending.access.api.business.CreditAccessMonitorDubboBusiness;
import com.xiaoniu.lending.access.api.vo.CreditAccessMonitorVO;
import com.xiaoniu.lending.auth.api.business.AuthMonitorDubboBusiness;
import com.xiaoniu.lending.auth.api.vo.AuthMonitorVO;
import com.xiaoniu.lending.gateway.vo.GatewayMonitorVO;
import com.xiaoniu.lending.order.api.business.MonitorDubboBusiness;
import com.xiaoniu.lending.order.api.vo.MonitorVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *运维监控入口
 * @author liuyinkai
 */
@RestController
@Log4j2
public class OperationalMonitorController {
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    MonitorDubboBusiness monitorDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    AuthMonitorDubboBusiness authMonitorDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    CreditAccessMonitorDubboBusiness creditAccessMonitorDubboBusiness;
    /**
     * order应用运维监控
     * @param
     * @return
     */
    @PostMapping("/order/monitor")
    public MonitorVO monitorResult() {
        log.info("OperationalMonitorController:monitorResult=======>{}");
        MonitorVO monitorVO = monitorDubboBusiness.queryStatus();
        log.info("OperationalMonitorController.monitorResult:resultData======>{}");
        return monitorVO;
    }

    /**
     * auth应用运维监控
     * @param
     * @return
     */
    @PostMapping("/auth/monitor")
    public AuthMonitorVO authMonitorResult() {
        log.info("OperationalMonitorController:authMonitorResult=======>{}");
        AuthMonitorVO monitorVO = authMonitorDubboBusiness.queryStatus();
        log.info("OperationalMonitorController.authMonitorResult:resultData======>{}");
        return monitorVO;
    }

    /**
     * CreditAccess应用运维监控
     * @param
     * @return
     */
    @PostMapping("/credit-access/monitor")
    public CreditAccessMonitorVO creditAccessMonitorResult() {
        log.info("OperationalMonitorController:creditAccessMonitorResult=======>{}");
        CreditAccessMonitorVO creditAccessMonitorVO = creditAccessMonitorDubboBusiness.queryStatus();
        log.info("OperationalMonitorController.creditAccessMonitorResult:resultData======>{}");
        return creditAccessMonitorVO;
    }
    /**
     * gateway应用运维监控
     * @param
     * @return
     */
    @PostMapping("/monitor")
    public GatewayMonitorVO gatewayMonitorResult() {
        log.info("OperationalMonitorController:gatewayMonitorResult=======>{}");

        log.info("OperationalMonitorController.gatewayMonitorResult:resultData======>{}");
        return null;
    }

}
