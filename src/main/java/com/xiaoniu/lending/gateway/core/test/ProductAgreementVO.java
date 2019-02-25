package com.xiaoniu.lending.gateway.core.test;

import com.xiaoniu.lending.order.api.vo.ProductAgreementVo;
import lombok.Data;

import java.util.List;

/**
 * @author :LiuYinkai
 * @date :2019-01-14 18:22.
 */
@Data
public class ProductAgreementVO extends BaseVo{
    private List<ProductAgreementVo> response;
}
