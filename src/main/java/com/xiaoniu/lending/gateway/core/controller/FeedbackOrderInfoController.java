package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.architecture.commons.api.ResultCodeEnum;
import com.xiaoniu.architecture.commons.api.exception.BusinessException;
import com.xiaoniu.architecture.commons.core.signature.SignatureUtils;
import com.xiaoniu.architecture.commons.core.util.JSONUtils;
import com.xiaoniu.lending.order.api.bo.*;
import com.xiaoniu.lending.order.api.business.OrderDubboBusiness;
import com.xiaoniu.lending.order.api.business.PlatformRequestBusiness;
import com.xiaoniu.lending.order.api.enums.HttpResponseEnum;
import com.xiaoniu.lending.order.api.enums.OrderMethodEnum;
import com.xiaoniu.lending.order.api.response.HttpCommonResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单回调
 *
 * @author luoyanchong
 * @date 2019/01/15
 */
@RestController
@Log4j2
public class FeedbackOrderInfoController {
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private OrderDubboBusiness orderDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private PlatformRequestBusiness platformRequestBusiness;

    /**
     * 商户回调
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    @PostMapping(value = "/order/feedb" +
            "" +
            "" +
            "" +
            "" +
            "ack")
    public HttpCommonResponse feedback(HttpCommonRequestBO requestBO) {
        try {
            log.info("回调接口标识 ====>> ：{}", requestBO.getCall());
            Assert.notNull(requestBO, "回调请求不能为空");
            Assert.isTrue(StringUtils.isNotBlank(requestBO.getCall()), "回调请求标识不能为空");
            Assert.notNull(requestBO.getArgs(), "回调请求参数不能为空");
            //签证校验
            RequestParameterBO requestParameterBO = new RequestParameterBO();
            requestParameterBO.setUa(requestBO.getUa());
            String gatewayAppSecret = platformRequestBusiness.externalAppSecret(requestParameterBO);
            String targetSign = SignatureUtils.hashByHmacSHA256(requestBO.getUa() + requestBO.getTimestamp(), gatewayAppSecret);
            if (!targetSign.equals(requestBO.getSign())) {
                log.error("校验时间戳：{}和签名：{},失败，服务器生成的签名是：{}",
                        requestBO.getTimestamp(), requestBO.getSign(), targetSign);
                throw new IllegalArgumentException(ResultCodeEnum.ILLEGAL_SIGNATURE.getDescription());
            } else {
                log.info("签名验证通过");
            }
            if (OrderMethodEnum.PARTNER_ORDER_CREDIT_FEEDBACK.getBusinessDesc().equals(requestBO.getCall())) {
                log.info("授信结果回调");
                // 授信结果回调
                return creditFeedback(requestBO);
            } else if (OrderMethodEnum.PARTNER_ORDER_DRAWING_FEEDBACK.getBusinessDesc().equals(requestBO.getCall())) {
                // 提现审批回调
                log.info("提现审批回调");
                return approveFeedback(requestBO);
            } else if (OrderMethodEnum.PARTNER_ORDER_LENDING_FEEDBACK.getBusinessDesc().equals(requestBO.getCall())) {
                // 放款结果回调
                log.info("放款结果回调");
                return lendingFeedback(requestBO);
            } else if (OrderMethodEnum.PARTNER_ORDER_REPAY_PLAN_FEEDBACK.getBusinessDesc().equals(requestBO.getCall())) {
                // 还款计划推送
                log.info("还款计划推送");
                return repayPlanFeedback(requestBO);
            } else if (OrderMethodEnum.PARTNER_ORDER_REPAY_STATUS_FEEDBACK.getBusinessDesc().equals(requestBO.getCall())) {
                // 还款回调
                log.info("还款回调");
                return repayStatusFeedback(requestBO);
            }
            return new HttpCommonResponse(HttpResponseEnum.HTTP_RESPONSE_FAILURE_STATUS.getValue(), "call is not found", false);
        } catch (IllegalArgumentException e) {
            log.error("商户回调异常，异常信息：{}", e.getMessage());
            return new HttpCommonResponse(HttpResponseEnum.HTTP_RESPONSE_FAILURE_STATUS.getValue(), e.getMessage(), false);
        }
    }

    /**
     * 授信结果回调
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    private HttpCommonResponse creditFeedback(HttpCommonRequestBO requestBO) {
        CreditFeedbackBO creditFeedbackBO = JSONUtils.parseObject(requestBO.getArgs(), CreditFeedbackBO.class);
        Assert.notNull(creditFeedbackBO, "授信回调参数不能为空");
        Assert.isTrue(StringUtils.isNotBlank(creditFeedbackBO.getOrder_sn()), "订单编号不能为空");
        Assert.isTrue(StringUtils.isNotBlank(creditFeedbackBO.getUser_name()), "用户姓名不能为空");
        Assert.isTrue(StringUtils.isNotBlank(creditFeedbackBO.getUser_mobile()), "用户手机号不能为空");
        Assert.isTrue(StringUtils.isNotBlank(creditFeedbackBO.getUser_idcard()), "用户身份证号码不能为空");
        Assert.notNull(creditFeedbackBO.getTotal_amount(), "授信总额度不能为空");
        // Assert.notNull(creditFeedbackBO.getCan_use_amount(), "剩余可用额度不能为空");
        Assert.notNull(creditFeedbackBO.getMin_use_amount(), "最小借款金额不能为空");
        Assert.notNull(creditFeedbackBO.getRange_amount(), "金额粒度不能为空");
        Assert.notNull(creditFeedbackBO.getCredit_terms(), "可借期限不能为空");
        Assert.notNull(creditFeedbackBO.getTerm_type(), "可借期限类型不能为空");
        Assert.notNull(creditFeedbackBO.getCredit_status(), "返回授信状态值不能为空");
        // Assert.notNull(creditFeedbackBO.getCan_loan_time(), "授信拒绝可再次授信时间不能为空");
        Assert.notNull(creditFeedbackBO.getCredit_type(), "授信类型不能为空");
        // Assert.notNull(creditFeedbackBO.getCredit_void_time(), "授信失效时间不能为空");
        // Assert.notNull(creditFeedbackBO.getUpdated_at(), "授信状态改变时间不能为空");
        // Assert.notNull(creditFeedbackBO.getCredit_void_type(), "授信过期类型不能为空");
        Assert.isTrue(StringUtils.isNotBlank(creditFeedbackBO.getRemark()), "授信失败返回原因");
        log.info("creditFeedback 授信回调参数校验通过");
        return orderDubboBusiness.creditFeedback(creditFeedbackBO);
    }

    /**
     * 提现审批回调
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    private HttpCommonResponse approveFeedback(HttpCommonRequestBO requestBO) {
        Assert.notNull(requestBO, "请求参数不能为空");
        ApproveFeedbackBO approveFeedbackBO = JSONUtils.parseObject(requestBO.getArgs(), ApproveFeedbackBO.class);
        Assert.notNull(approveFeedbackBO, "提现审批回调参数不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getOrder_sn()), "订单编号不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getApprove_status()), "审批状态不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getApprove_amount()), "审批后的可借金额不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getApprove_term()), "审批后的可借周期不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getTerm_type()), "审批后的可借周期类型不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getApprove_remark()), "审批状态备注不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getCan_loan_time()), "可以再次申请借款的时间不能为空");
        Assert.isTrue(StringUtils.isNotBlank(approveFeedbackBO.getUpdated_at()), "订单状态变更时间不能为空");

        return orderDubboBusiness.approveFeedback(approveFeedbackBO);
    }

    /**
     * 放款结果回调
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    private HttpCommonResponse lendingFeedback(HttpCommonRequestBO requestBO) {
        LendingFeedbackBO lendingFeedbackBO = JSONUtils.parseObject(requestBO.getArgs(), LendingFeedbackBO.class);
        Assert.notNull(lendingFeedbackBO, "放款结果回调参数不能为空");
        Assert.isTrue(StringUtils.isNotBlank(lendingFeedbackBO.getOrder_sn()), "订单编号不能为空");
        Assert.isTrue(StringUtils.isNotBlank(lendingFeedbackBO.getLending_status()), "放款状态不能为空");
        Assert.isTrue(StringUtils.isNotBlank(lendingFeedbackBO.getFail_reason()), "放款失败的说明信息不能为空");
        Assert.isTrue(StringUtils.isNotBlank(lendingFeedbackBO.getUpdated_at()), "放款时间不能为空");

        return orderDubboBusiness.lendingFeedback(lendingFeedbackBO);
    }

    /**
     * 还款计划推送（回调方式）
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    private HttpCommonResponse repayPlanFeedback(HttpCommonRequestBO requestBO) {
        Assert.notNull(requestBO, "请求参数不能为空");
        RepayPlanFeedbackBO repayPlanFeedbackBO = JSONUtils.parseObject(requestBO.getArgs(), RepayPlanFeedbackBO.class);
        Assert.notNull(repayPlanFeedbackBO, "还款计划推送参数不能为空");
        Assert.isTrue(StringUtils.isNotBlank(repayPlanFeedbackBO.getOrder_sn()), "订单编号不能为空");
        Assert.notNull(repayPlanFeedbackBO.getTotal_amount(), "还款总额不能为空");
        Assert.notNull(repayPlanFeedbackBO.getTotal_svc_fee(), "总服务费不能为空");
        Assert.notNull(repayPlanFeedbackBO.getReceived_amount(), "到账金额不能为空");
        Assert.notNull(repayPlanFeedbackBO.getAlready_paid(), "已还金额不能为空");
        Assert.notNull(repayPlanFeedbackBO.getTotal_period(), "总期数不能为空");
        Assert.notNull(repayPlanFeedbackBO.getFinish_period(), "已还期数不能为空");
        Assert.notNull(repayPlanFeedbackBO.getRepayment_plan(), "还款计划推送参数不能为空");

        return orderDubboBusiness.repayPlanFeedback(repayPlanFeedbackBO);
    }

    /**
     * 还款结果回调
     *
     * @param requestBO 通用请求参数
     * @return 回调执行结果
     */
    private HttpCommonResponse repayStatusFeedback(HttpCommonRequestBO requestBO) {
        RepayStatusFeedbackBO repayStatusFeedbackBO = JSONUtils.parseObject(requestBO.getArgs(), RepayStatusFeedbackBO.class);
        Assert.notNull(repayStatusFeedbackBO, "还款结果回调参数不能为空");
        Assert.isTrue(StringUtils.isNotBlank(repayStatusFeedbackBO.getOrder_sn()), "订单编号不能为空");
        Assert.isTrue(StringUtils.isNotBlank(repayStatusFeedbackBO.getRepay_result()), "还款结果状态不能为空");
        Assert.isTrue(StringUtils.isNotBlank(repayStatusFeedbackBO.getUpdated_at()), "状态变更时间不能为空");

        return orderDubboBusiness.repayStatusFeedback(repayStatusFeedbackBO);
    }


}
