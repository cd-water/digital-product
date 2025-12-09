package com.cdwater.digitalproduct.entity;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 数码配件实体类
 */
@Data
public class Accessory {

    private Integer id;

    private Integer shopId;

    private Integer typeId;

    private String name;

    private String img;

    private BigDecimal price;

    private Integer store;

    private String introduce;

    private Integer saleVolume;

    private Integer saleStatus;
}