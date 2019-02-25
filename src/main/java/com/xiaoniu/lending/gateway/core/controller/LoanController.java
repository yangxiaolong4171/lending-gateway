package com.xiaoniu.lending.gateway.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.lending.order.api.bo.ProductAgreementBO;
import com.xiaoniu.lending.order.api.bo.ProductCountBO;
import com.xiaoniu.lending.order.api.business.ProductDubboBusiness;
import com.xiaoniu.lending.order.api.vo.ProductAgreementVo;
import com.xiaoniu.lending.order.api.vo.ProductCountVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuyinkai
 */
@RestController
@Log4j2
public class LoanController {

    @Reference(version = "1.0.0", retries = 0, timeout = 10000)
    private ProductDubboBusiness productDubboBusiness;

    /***
     * @description 查询商户金额试算
     * @author liuyinkai
     * @param productCountBO
     * @return
     */
    @PostMapping("/order/queryProductCount")
    public ProductCountVo queryProductCount(@RequestBody ProductCountBO productCountBO) {
        //用户编号
        String customerId = HeaderHelper.getCustomerId();
        Assert.hasLength(customerId, "http请求头中的customer-id不能为空");
        //审批金额 单位（分）
        Assert.hasLength(String.valueOf(productCountBO.getLoan_amount()), "入参审批金额单位不能为空");
        //审批期限
        Assert.hasLength(String.valueOf(productCountBO.getLoan_term()), "入参审批期限不能为空");
        //1:按天; 2：按月; 3：按年
        Assert.hasLength(String.valueOf(productCountBO.getTerm_type()), "入参贷款期限配型不能为空");
        //商户id
        Assert.hasLength(productCountBO.getProductId(), "入参商户ID不能为空");
        log.info("LoanController:productCount=======>{}", productCountBO);
        ProductCountVo productCountVo = productDubboBusiness.queryProductCount(productCountBO);
        log.info("LoanController.productCount:resultData======>{}", productCountVo);
        return productCountVo;
    }

    /***
     * 查询商户协议
     * @au
     * @param productAgreementBO
     * @return
     */
    @PostMapping("/order/queryProductAgreement")
    public List<ProductAgreementVo> queryProductAgreement(@RequestBody ProductAgreementBO productAgreementBO) {
        log.info("LoanController:queryProductAgreement=======>{}", productAgreementBO);
        //商户id
        Assert.hasLength(productAgreementBO.getProductId(), "入参商户ID不能为空");
        List<ProductAgreementVo> productAgreementVo = productDubboBusiness.queryAgreement(productAgreementBO);
        log.info("LoanController.queryProductAgreement:resultData======>{}", productAgreementVo);
        return productAgreementVo;
    }
}
