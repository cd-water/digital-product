package com.cdwater.digitalproduct.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 收藏夹展示数码产品VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectProductVO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer productUsed;
    private String productImg;
    private BigDecimal productPrice;
    private Integer productStore;
    private Integer productSaleStatus;
}
