package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台数码配件分页VO
 */
@Data
public class AccessoryPageVO {
    private Integer id;
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
