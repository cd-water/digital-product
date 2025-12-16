package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 展示数码产品VO
 */
@Data
public class ProductShowVO {
    private Integer id;
    private String name;
    private Integer used;
    private String img;
    private BigDecimal price;
    private Integer store;
    private Integer saleStatus;
}
