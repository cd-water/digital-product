package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数码产品详情VO
 */
@Data
public class ProductDetailVO {
    private Integer id;
    private Integer shopId;
    private String name;
    private Integer used;
    private String img;
    private BigDecimal price;
    private Integer store;
    private String introduce;
    private String content;
    private Integer saleStatus;
    private String shopName;
    private String shopAvatar;
    private String typeName;
    private Boolean hasCollect;
}
