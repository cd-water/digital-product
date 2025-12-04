package com.cdwater.digitalproduct.entity;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 数码产品实体类
 */
@Data
public class Product {

    private Integer id;

    private Integer shopId;

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