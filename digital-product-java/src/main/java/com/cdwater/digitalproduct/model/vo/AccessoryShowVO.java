package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 展示数码配件VO
 */
@Data
public class AccessoryShowVO {
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
    private String typeName;
    private String shopName;
}