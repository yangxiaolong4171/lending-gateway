/*
package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.lending.access.api.business.CreditAccessMonitorDubboBusiness;
import com.xiaoniu.lending.access.api.vo.CreditAccessMonitorVO;
import com.xiaoniu.lending.auth.api.business.AuthMonitorDubboBusiness;
import com.xiaoniu.lending.auth.api.vo.AuthMonitorVO;
import com.xiaoniu.lending.order.api.business.MonitorDubboBusiness;
import com.xiaoniu.lending.order.api.vo.MonitorVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 *钉钉监控入口
 *
 * @author liuyinkai
 *//*

@RestController
@Log4j2
public class DingdingMonitorController {
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    MonitorDubboBusiness monitorDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    AuthMonitorDubboBusiness authMonitorDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    CreditAccessMonitorDubboBusiness creditAccessMonitorDubboBusiness;
    */
/**
     * order应用钉钉监控
     * @param
     * @return
     *//*

    @PostMapping("/lending-order/monitor")
    public MonitorVO monitorResult() {
        log.info("DingdingMonitorController:monitorResult=======>{}");
        MonitorVO monitorVO = monitorDubboBusiness.queryStatus();
        log.info("DingdingMonitorController.monitorResult:resultData======>{}");
        return monitorVO;
    }

    */
/**
     * auth应用钉钉监控
     * @param
     * @return
     *//*

    @PostMapping("/lending-auth/monitor")
    public AuthMonitorVO authMonitorResult() {
        log.info("DingdingMonitorController:authMonitorResult=======>{}");
        AuthMonitorVO monitorVO = authMonitorDubboBusiness.queryStatus();
        log.info("DingdingMonitorController.authMonitorResult:resultData======>{}");
        return monitorVO;
    }

    */
/**
     * CreditAccess应用钉钉监控
     * @param
     * @return
     *//*

    @PostMapping("/lending-credit-access/monitor")
    public CreditAccessMonitorVO creditAccessMonitorResult() {
        log.info("DingdingMonitorController:creditAccessMonitorResult=======>{}");
        CreditAccessMonitorVO creditAccessMonitorVO = creditAccessMonitorDubboBusiness.queryStatus();
        log.info("DingdingMonitorController.creditAccessMonitorResult:resultData======>{}");
        return creditAccessMonitorVO;
    }

}
*/
