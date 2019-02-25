package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.lending.auth.api.bo.*;
import com.xiaoniu.lending.auth.api.business.UserAuthDubboBusiness;
import com.xiaoniu.lending.auth.api.business.UserBankCardBusiness;
import com.xiaoniu.lending.auth.api.business.UserBaseAuthBusiness;
import com.xiaoniu.lending.auth.api.business.UserFilterBusiness;
import com.xiaoniu.lending.auth.api.vo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Log4j2
public class AuthController {
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private UserBaseAuthBusiness userBaseAuthBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private UserAuthDubboBusiness userAuthDubboBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private UserBankCardBusiness userBankCardBusiness;
    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private UserFilterBusiness userFilterBusiness;
    /***
     * 绑卡信息保存
     * @param bankCardDto
     * @return
     */
    @PostMapping("/auth/saveBindBankCard")
    public ApplyBindCardResultVO saveBindBankCard(@RequestBody @Valid BindBankCardBO bankCardDto) {
        List<BindBankCardBO> bankCardDtoList = Lists.newArrayList();
        bankCardDto.setAppName(HeaderHelper.getAppName());
        bankCardDto.setCustomerId(HeaderHelper.getCustomerId());
        bankCardDtoList.add(bankCardDto);
        return userBankCardBusiness.saveBindBankCard(bankCardDtoList);
    }

    /***
     * 绑卡信息查询
     * @return
     */
    @PostMapping("/auth/listBindBankCard")
    public List<BindBankCardVo> listBindBankCard(@RequestBody @Valid BindBankCardBO bindBankCardBO) {
        bindBankCardBO.setAppName(HeaderHelper.getAppName());
        bindBankCardBO.setCustomerId(HeaderHelper.getCustomerId());
        return userBankCardBusiness.listBindBankCard(bindBankCardBO);
    }

    /***
     * 保存银行卡信息
     * @param
     * @return
     */
    @PostMapping("/auth/saveBankCard")
    public void saveBankCard(@RequestBody @Valid List<BankCardBO> bankCardDtoList) {
        if (!CollectionUtils.isEmpty(bankCardDtoList)) {
            for (BankCardBO bankCardDto : bankCardDtoList) {
                bankCardDto.setAppName(HeaderHelper.getAppName());
                bankCardDto.setCustomerId(HeaderHelper.getCustomerId());
            }
            userBankCardBusiness.saveBankCard(bankCardDtoList);
        }
    }

    /***
     * 查询银行卡信息
     * @return
     */
    @PostMapping("/auth/listBankCard")
    public List<BankCardVo> listBankCard(@RequestBody @Valid BankCardBO bankCardBO) {
        bankCardBO.setAppName(HeaderHelper.getAppName());
        bankCardBO.setCustomerId(HeaderHelper.getCustomerId());
        return userBankCardBusiness.listBankCard(bankCardBO);
    }

    /***
     * 查询我的银行卡信息
     * @return
     */
    @GetMapping("/auth/listMineBankCard")
    public List<MineBankInfoVO> listMineBankCard() {
        BaseBO baseBO = new BaseBO();
        baseBO.setAppName(HeaderHelper.getAppName());
        baseBO.setCustomerId(HeaderHelper.getCustomerId());
        return userBankCardBusiness.listMineBankCard(baseBO);
    }

    /**
     * 人脸识别认证
     *
     * @param identityCertificationBO
     * @return
     */
    @PostMapping("/auth/face")
    public void face(@RequestBody @Valid IdentityCertificationBO identityCertificationBO) {
        identityCertificationBO.setRequestId(HeaderHelper.getRequestId());
        identityCertificationBO.setAppName(HeaderHelper.getAppName());
        identityCertificationBO.setCustomerId(HeaderHelper.getCustomerId());
        identityCertificationBO.setAppVersion(HeaderHelper.getAppVersion());
        identityCertificationBO.setMarket(HeaderHelper.getMarket());
        identityCertificationBO.setOsVersion(HeaderHelper.getOsVersion());
        userAuthDubboBusiness.faceCertification(identityCertificationBO);
    }

    /**
     * 身份证正面识别认证
     *
     * @param identityCertificationBO
     * @return
     */
    @PostMapping("/auth/ocr-front")
    public void ocrFront(@RequestBody @Valid IdentityCertificationBO identityCertificationBO) {
        identityCertificationBO.setRequestId(HeaderHelper.getRequestId());
        identityCertificationBO.setAppName(HeaderHelper.getAppName());
        identityCertificationBO.setCustomerId(HeaderHelper.getCustomerId());
        identityCertificationBO.setAppVersion(HeaderHelper.getAppVersion());
        identityCertificationBO.setMarket(HeaderHelper.getMarket());
        identityCertificationBO.setOsVersion(HeaderHelper.getOsVersion());
        userAuthDubboBusiness.ocrFrontCertification(identityCertificationBO);
    }

    /**
     * 身份证反面识别认证
     *
     * @param identityCertificationBO
     * @return
     */
    @PostMapping("/auth/ocr-back")
    public void ocrBack(@RequestBody @Valid IdentityCertificationBO identityCertificationBO) {
        identityCertificationBO.setRequestId(HeaderHelper.getRequestId());
        identityCertificationBO.setAppName(HeaderHelper.getAppName());
        identityCertificationBO.setCustomerId(HeaderHelper.getCustomerId());
        identityCertificationBO.setAppVersion(HeaderHelper.getAppVersion());
        identityCertificationBO.setMarket(HeaderHelper.getMarket());
        identityCertificationBO.setOsVersion(HeaderHelper.getOsVersion());
        userAuthDubboBusiness.ocrBackCertification(identityCertificationBO);
    }

    /**
     * 获取运营商token
     *
     * @param operatorTokenBO
     * @return
     */
    @PostMapping("/auth/operator-token")
    public OperatorTokenVO operatorToken(@RequestBody @Valid OperatorTokenBO operatorTokenBO) {
        operatorTokenBO.setRequestId(HeaderHelper.getRequestId());
        operatorTokenBO.setAppName(HeaderHelper.getAppName());
        operatorTokenBO.setCustomerId(HeaderHelper.getCustomerId());
        return userAuthDubboBusiness.operatorToken(operatorTokenBO);
    }

    /**
     * 保存运营商认证成功信息
     *
     * @param operatorSuccessInfoBO
     */
    @PostMapping("/auth/operator-success-info")
    public void saveOperatorSuccessInfo(@RequestBody @Valid OperatorSuccessInfoBO operatorSuccessInfoBO) {
        operatorSuccessInfoBO.setRequestId(HeaderHelper.getRequestId());
        operatorSuccessInfoBO.setAppName(HeaderHelper.getAppName());
        operatorSuccessInfoBO.setCustomerId(HeaderHelper.getCustomerId());
        userAuthDubboBusiness.saveOperatorSuccessInfo(operatorSuccessInfoBO);
    }

    /**
     * 保存运营商认证失败信息
     *
     * @param operatorFailInfoBO
     */
    @PostMapping("/auth/operator-fail-info")
    public void saveOperatorFailInfo(@RequestBody @Valid OperatorFailInfoBO operatorFailInfoBO) {
        operatorFailInfoBO.setRequestId(HeaderHelper.getRequestId());
        operatorFailInfoBO.setAppName(HeaderHelper.getAppName());
        operatorFailInfoBO.setCustomerId(HeaderHelper.getCustomerId());
        userAuthDubboBusiness.saveOperatorFailInfo(operatorFailInfoBO);
    }

    /**
     * 保存紧急联系人
     *
     * @param saveIcePersonBO
     */
    @PostMapping("/auth/ice-persons")
    public void saveIcePerson(@Valid @RequestBody SaveIcePersonBO saveIcePersonBO) {
        saveIcePersonBO.setAppName(HeaderHelper.getAppName());
        saveIcePersonBO.setCustomerId(HeaderHelper.getCustomerId());
        saveIcePersonBO.setRequestId(HeaderHelper.getRequestId());
        saveIcePersonBO.setAppVersion(HeaderHelper.getAppVersion());
        saveIcePersonBO.setMarket(HeaderHelper.getMarket());
        saveIcePersonBO.setOsVersion(HeaderHelper.getOsVersion());
        userAuthDubboBusiness.saveIcePerson(saveIcePersonBO);
    }

    /**
     * 获取紧急联系人列表
     */
    @GetMapping("/auth/ice-persons")
    public List<IcePersonVO> getIcePersonList() {
        IcePersonListBO icePersonListBO = new IcePersonListBO();
        icePersonListBO.setAppName(HeaderHelper.getAppName());
        icePersonListBO.setCustomerId(HeaderHelper.getCustomerId());
        icePersonListBO.setRequestId(HeaderHelper.getRequestId());
        return userAuthDubboBusiness.getIcePersonList(icePersonListBO);
    }

    /**
     * 获取公信宝淘宝认证接口
     *
     * @return
     */
    @GetMapping("/auth/gxb-commerce")
    public GxbCommerceAuthVO gxbCommerceAuth() {
        GxbCommerceAuthBO gxbCommerceAuthBO = new GxbCommerceAuthBO();
        gxbCommerceAuthBO.setRequestId(HeaderHelper.getRequestId());
        gxbCommerceAuthBO.setAppName(HeaderHelper.getAppName());
        gxbCommerceAuthBO.setCustomerId(HeaderHelper.getCustomerId());
        return userAuthDubboBusiness.gxbCommerceAuth(gxbCommerceAuthBO);
    }

    /**
     * 保持公信宝淘宝认证结果
     *
     * @param gxbCommerceAuthResultBO
     */
    @PostMapping("/auth/gxb-commerce-result")
    public void gxbCommerceResult(@RequestBody @Valid GxbCommerceAuthResultBO gxbCommerceAuthResultBO) {
        gxbCommerceAuthResultBO.setAppName(HeaderHelper.getAppName());
        gxbCommerceAuthResultBO.setCustomerId(HeaderHelper.getCustomerId());
        gxbCommerceAuthResultBO.setRequestId(HeaderHelper.getRequestId());
        userAuthDubboBusiness.gxbCommerceAuthResult(gxbCommerceAuthResultBO);
    }

    /**
     * 商户详情页获取客户认证列表
     *
     * @param apiProductCertificationBO
     */
    @PostMapping("/auth/certificationStatuses")
    public CertificationStatusesVO certificationStatuses(@RequestBody @Valid ApiProductCertificationBO apiProductCertificationBO) {
        apiProductCertificationBO.setRequestId(HeaderHelper.getRequestId());
        apiProductCertificationBO.setAppName(HeaderHelper.getAppName());
        apiProductCertificationBO.setCustomerId(HeaderHelper.getCustomerId());
        return userAuthDubboBusiness.certificationStatuses(apiProductCertificationBO);
    }

    /****
     * 补充认证 - 查询
     * @return
     */
    @GetMapping("/auth/getSupplementaryCertification")
    public SupplementCertificationVO getSupplementaryCertification() {
        BaseBO baseBO = new BaseBO();
        baseBO.setRequestId(HeaderHelper.getRequestId());
        baseBO.setAppName(HeaderHelper.getAppName());
        baseBO.setCustomerId(HeaderHelper.getCustomerId());
        return userAuthDubboBusiness.getSupplementaryCertification(baseBO);
    }

    /****
     * 补充认证 - 保存
     * @param supplementCertificationBO
     * @return
     */
    @PostMapping("/auth/saveSupplementaryCertification")
    public void saveSupplementaryCertification(@RequestBody @Valid SupplementCertificationBO supplementCertificationBO) {
        supplementCertificationBO.setRequestId(HeaderHelper.getRequestId());
        supplementCertificationBO.setAppName(HeaderHelper.getAppName());
        supplementCertificationBO.setCustomerId(HeaderHelper.getCustomerId());
        userAuthDubboBusiness.saveSupplementaryCertification(supplementCertificationBO);
    }

    /**
     * 保存客户扩展信息
     *
     * @param apiCustomerInfoExpansionBO
     */
    @PostMapping("/auth/customer-info-expansion")
    public void customerInfoExpansion(@RequestBody @Valid ApiCustomerInfoExpansionBO apiCustomerInfoExpansionBO) {
        apiCustomerInfoExpansionBO.setRequestId(HeaderHelper.getRequestId());
        apiCustomerInfoExpansionBO.setAppName(HeaderHelper.getAppName());
        apiCustomerInfoExpansionBO.setCustomerId(HeaderHelper.getCustomerId());
        userAuthDubboBusiness.customerInfoExpansion(apiCustomerInfoExpansionBO);
    }

    /**
     * 获取客户信息
     *
     * @return 客户信息
     */
    @PostMapping("/auth/getCustomerInfo")
    public ApiCustomerInfoVO getCustomerInfo() {
        //	APP名称 1:来这记、2:贷款管家
        Integer appName = HeaderHelper.getAppName();
        //用户编号
        String customerId = HeaderHelper.getCustomerId();
        Assert.hasLength(customerId, "http请求头中的customer-id不能为空");
        log.info("AuthController:getCustomerInfo=======>appName={}、customerId={}", appName, customerId);
        ApiCustomerInfoVO customerInfoVO = userAuthDubboBusiness.getCustomerInfo(appName, customerId);
        log.info("AuthController:getCustomerInfo=======>返回结果：{}", customerInfoVO);
        return customerInfoVO;
    }

    /**
     * 用户过滤
     * @return 是否可借信息
     */
    @PostMapping("/auth/isUserAccept")
    public UserFilterVO isUserAccept(@RequestBody @Valid UserFilterBO userFilterBO) {
        //	APP名称 1:来这记、2:贷款管家
        Integer appName = HeaderHelper.getAppName();
        userFilterBO.setAppName(appName);
        //用户编号
        String customerId = HeaderHelper.getCustomerId();
        userFilterBO.setCustomerId(customerId);
        Assert.hasLength(customerId, "http请求头中的customer-id不能为空");
        log.info("AuthController:isUserAccept=======>appName={}、customerId={}", appName, customerId);
        UserFilterVO userFilterVO = userFilterBusiness.judgeUserCanLoan(userFilterBO);
        log.info("AuthController:isUserAccept=======>返回结果：{}", userFilterVO);
        return userFilterVO;
    }
}
