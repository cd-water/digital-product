package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台数码产品查询VO
 */
@Data
public class ProductQueryVO {
    private Integer id;
    private Integer typeId;
    private String name;
    private Integer used;
    private String img;
    private BigDecimal price;
    private Integer store;
    private String introduce;
    private String content;
    private Integer saleStatus;
    private Integer recommend;
}
