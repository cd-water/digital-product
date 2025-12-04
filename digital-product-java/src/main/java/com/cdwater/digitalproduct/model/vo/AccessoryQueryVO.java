package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台数码配件查询VO
 */
@Data
public class AccessoryQueryVO {
    private Integer id;
    private Integer typeId;
    private Integer shopId;
    private String name;
    private String img;
    private BigDecimal price;
    private Integer store;
    private String introduce;
    private Integer saleVolume;
    private Integer saleStatus;
}
