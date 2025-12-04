package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台数码产品分页VO
 */
@Data
public class ProductPageVO {
    private Integer id;
    private String name;
    private Integer used;
    private String img;
    private BigDecimal price;
    private Integer store;
    private String introduce;
    private String content;
    private Integer saleStatus;
    private Integer recommend;
    private String shopName;
    private String typeName;
}