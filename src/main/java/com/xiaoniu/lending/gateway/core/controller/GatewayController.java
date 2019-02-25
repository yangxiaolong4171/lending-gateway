package com.xiaoniu.lending.gateway.core.controller;

import com.google.common.collect.Lists;
import com.xiaoniu.architecture.commons.web.http.HeaderHelper;
import com.xiaoniu.lending.gateway.core.test.*;
import com.xiaoniu.lending.order.api.vo.ProductAgreementVo;
import com.xiaoniu.lending.order.api.vo.ProductCountObjectVo;
import com.xiaoniu.lending.order.api.vo.ProductCountVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关里面的controller可以直接返回data的类型就可以了，
 * Controller上的注解是
 * <code>
 *
 * @author guoqiang
 * @Controller
 * @ResponseBody </code>
 * 这样框架会自动包装响应体
 * 如果不需要框架包装响应体除了配置白名单之外还可以在响应的类上面实现接口NoneResponseBodyWrapable
 * 或者把Controller上注解改成<code>@RestController</code>
 * <p>
 * 网关控制器
 * @date 2019-01-02 14:28 PM
 */
@RestController
@Slf4j
public class GatewayController {

    /**
     * 返回原生类型或者String必须加<code>produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}</code>
     *
     * @return
     */
    @GetMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String success() {
        System.out.println("getRequestId:" + HeaderHelper.getRequestId());
        return "来这借2.0网关";
    }

    @GetMapping(value = "/format")
    public GatewayDTO format() {
        return GatewayDTO.builder().name("少帅").age(18).build();
    }

    /****
     *  对外接口测试用例
     * @param baseRequestVo
     * @return
     */
    @PostMapping("/api/external")
    public String externalProduct(@RequestBody BaseRequestVo baseRequestVo) {
        //查找绑卡银行列表-对外接口测试
        if (baseRequestVo.getCall().equals("BindCard.getValidBankList")) {
            ValidBankListVO validBankListVO = new ValidBankListVO();
            validBankListVO.setStatus(1);
            validBankListVO.setMessage("success");
            List<ValidBankVo> validBankVoList = Lists.newArrayList();
            ValidBankVo validBankVo = new ValidBankVo();
            validBankVo.setBankCode("ICBC");
            validBankVo.setBankName("工商银行");
            validBankVo.setBankTitle("单笔1W/日2W/月2W");
            validBankVo.setBankIcon("https://h5.u51.com/99fenqi/image/logo/ICBC20161031.png");
            validBankVoList.add(validBankVo);
            validBankVo = new ValidBankVo();
            validBankVo.setBankCode("BOC");
            validBankVo.setBankName("中国银行");
            validBankVo.setBankTitle("单笔5W/日20W/月20W");
            validBankVo.setBankIcon("https://h5.u51.com/99fenqi/image/logo/BOC20161031.png");
            validBankVoList.add(validBankVo);
            validBankListVO.setResponse(validBankVoList);

            String returnStr = JSONUtils.toJSONString(validBankListVO);
            log.info("[externalProduct,BindCard.getValidBankList],封装测试的json数据:{}", returnStr);
            return returnStr;
            //试算
        }else if (baseRequestVo.getCall().equals("Order.loanCalculate")){
            log.info("[externalProduct,productCount,进入商户协议接口，接口标识{}", baseRequestVo.getCall());
            ProductCountVO productCountVO = new ProductCountVO();
            ProductCountVo productCountVo = new ProductCountVo();
            productCountVO.setStatus(1);
            productCountVO.setMessage("success");
            productCountVo.setService_fee(6000);
            productCountVo.setService_fee_desc("包含30%风控服务费， 40%信息认证费");
            productCountVo.setInterest_fee(1000);
            productCountVo.setGoods_amount(90000);
            productCountVo.setRepay_amount(1000000);
            productCountVo.setReceive_amount(20000);
            List<ProductCountObjectVo> list = new ArrayList<>(6);
            ProductCountObjectVo productCountObjectVo = new ProductCountObjectVo();
            productCountObjectVo.setPeriod_no(1);
            productCountObjectVo.setRepay_amount(500000);
            productCountObjectVo.setRepay_amount_desc("本金500元，利息3~5元，服务费20元");
            list.add(productCountObjectVo);
            productCountVo.setRepay_plan(list);
            productCountVO.setResponse(productCountVo);
            String returnStr = JSONUtils.toJSONString(productCountVO);
            log.info("[externalProduct,productCount,封装测试的json数据:{}", returnStr);
            return JSONUtils.toJSONString(productCountVO);
            //商户协议
        }else if (baseRequestVo.getCall().equals("Order.getContracts")){
            log.info("[externalProduct,productAgreement,进入商户协议接口，接口标识{}", baseRequestVo.getCall());
            ProductAgreementVO productAgreementVO = new ProductAgreementVO();
            List<ProductAgreementVo> list = new ArrayList<>(5);
            ProductAgreementVo productAgreementVo = new ProductAgreementVo();
            productAgreementVO.setStatus(1);
            productAgreementVO.setMessage("success");
            productAgreementVo.setLink("www.baidu.com");
            productAgreementVo.setName("借贷协议");
            list.add(productAgreementVo);
            productAgreementVO.setResponse(list);
            return JSONUtils.toJSONString(productAgreementVO);
        }
        return "Success";
    }

}
