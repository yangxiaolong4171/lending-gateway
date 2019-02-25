package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.lending.order.api.bo.*;
import com.xiaoniu.lending.order.api.business.OrderDubboBusiness;
import com.xiaoniu.lending.order.api.vo.ApiOrderInfoVO;
import com.xiaoniu.lending.order.api.vo.OrderInfoVo;
import com.xiaoniu.lending.order.api.vo.ProductOrderDetailVo;
import com.xiaoniu.lending.order.api.vo.PushUserInfoResultVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单相关接口入口
 *
 * @author shenguoqing
 */
@RestController
@Log4j2
public class OrderInfoController {
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private OrderDubboBusiness orderDubboBusiness;

    /***
     * 查询订单列表
     * @param orderInfoBo
     * @return List<OrderInfoVo> 订单列表
     */

    @PostMapping("/order/list")
    public List<OrderInfoVo> listOrder(@RequestBody OrderInfoBo orderInfoBo) {
        //	APP名称 1:来这记、2:贷款管家
        Integer appName = HeaderHelper.getAppName();
        //用户编号
        String customerId = HeaderHelper.getCustomerId();
        Assert.hasLength(customerId, "http请求头中的customer-id不能为空");
        //查询参数封装
        orderInfoBo.setAppName(appName);
        orderInfoBo.setCustomerId(customerId);
        log.info("OrderInfoController:listOrder=======>{}", orderInfoBo);
        List<OrderInfoVo> orderInfoVoList = orderDubboBusiness.listOrderInfo(orderInfoBo);
        log.info("OrderInfoController.listOrderInfo:resultData======>{}", orderInfoVoList);
        return orderInfoVoList;
    }

    /**
     *查询订单详情（提现还/款页面）
     * @param queryOrderInfoBo
     * @return 订单详情
     */
    @PostMapping("/order/apiOrderInfo")
    public ApiOrderInfoVO apiOrderInfo(@RequestBody QueryOrderInfoBo queryOrderInfoBo) {
        Assert.hasLength(queryOrderInfoBo.getOrderSn(), "参数orderSn不能为空");
        Assert.notNull(queryOrderInfoBo.getOrderStatus(), "参数orderStatus不能为空");
        log.info("OrderInfoController:apiOrderInfo=======>{}", queryOrderInfoBo);
        ApiOrderInfoVO apiOrderInfoVO = orderDubboBusiness.getOrderInfo(queryOrderInfoBo);
        log.info("OrderInfoController:apiOrderInfo=======>{}", apiOrderInfoVO);
        return apiOrderInfoVO;
    }
    /***
     * 查询商户详情、订单详情
     * @au
     * @param productOrderDetailBo
     * @return
     */
    @PostMapping("/order/productOrderDetail")
    public ProductOrderDetailVo productOrderDetail(@RequestBody ProductOrderDetailBo productOrderDetailBo) {
        //	APP名称 1:来这记、2:贷款管家
        Integer appName = HeaderHelper.getAppName();
        //用户编号
        String customerId = HeaderHelper.getCustomerId();
        Assert.hasLength(customerId, "http请求头中的customer-id不能为空");
        //查询参数封装
        productOrderDetailBo.setAppName(appName);
        productOrderDetailBo.setCustomerId(customerId);
        log.info("OrderInfoController:productOrderDetail=======>{}", productOrderDetailBo);
        ProductOrderDetailVo productOrderDetailVo = orderDubboBusiness.queryProductOrderDetail(productOrderDetailBo);
        log.info("OrderInfoController.productOrderDetail:resultData======>{}", productOrderDetailVo);
        return productOrderDetailVo;
    }

    /**
     * 订单生成，推送用户信息
     *
     * @param apiOrderInfoBO 用户订单信息
     * @return 订单生成状态以及资料推送结果
     */
    @PostMapping(value = "/order/pushUserInfoResult", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public PushUserInfoResultVO pushUserInfoResult(@RequestBody ApiOrderInfoBO apiOrderInfoBO) {
        Assert.notNull(apiOrderInfoBO, "请求对象不能为空");
        Assert.notNull(apiOrderInfoBO.getApplyAmount(), "申请借款金额不能为空");
        Assert.notNull(apiOrderInfoBO.getApplyTerms(), "申请借款期限不能为空");
        Assert.notNull(apiOrderInfoBO.getApplyTermType(), "申请借款期限类型不能为空");
        Assert.notNull(apiOrderInfoBO.getProductId(), "申请商户编号不能为空");
        Assert.notNull(apiOrderInfoBO.getProductName(), "申请商户名称不能为空");
        apiOrderInfoBO.setCustomerId(HeaderHelper.getCustomerId());
        apiOrderInfoBO.setAppName(HeaderHelper.getAppName());
        return orderDubboBusiness.pushUserInfo(apiOrderInfoBO);
    }

    /**
     * 用户提现申请
     *
     * @param withdrawOrderBO 提现信息
     * @return true-提现申请成功，提现申请失败
     */
    @PostMapping(value = "/order/pushCreditOrder", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public boolean pushCreditOrder(@RequestBody WithdrawOrderBO withdrawOrderBO) {
        Assert.notNull(withdrawOrderBO, "提现申请参数不能为空");
        Assert.notNull(withdrawOrderBO.getLoan_amount(), "提现申请金额不能为空");
        Assert.notNull(withdrawOrderBO.getLoan_term(), "提现申请期限不能为空");
        Assert.notNull(withdrawOrderBO.getOrder_sn(), "提现申请订单编号不能为空");
        Assert.notNull(withdrawOrderBO.getTerm_type(), "提现申请期限类型不能为空");
        boolean result = orderDubboBusiness.pushCreditOrder(withdrawOrderBO, HeaderHelper.getCustomerId(), HeaderHelper.getAppName());
        return result;
    }

    /**
     * 还款申请
     *
     * @param applyRepayBO 还款请求参数
     * @return 还款申请状态
     */
    @PostMapping(value = "/order/applyRepay", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String applyRepay(@RequestBody ApplyRepayBO applyRepayBO) {
        Assert.notNull(applyRepayBO, "还款请求参数不能为空");
        Assert.notNull(applyRepayBO.getOrder_sn(), "还款订单编号不能为空");
        Assert.notNull(applyRepayBO.getRepay_type(), "还款类型不能为空");
        String result = orderDubboBusiness.applyRepay(applyRepayBO);
        return result;
    }
}
