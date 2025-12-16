package com.cdwater.digitalproduct.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数码产品分页DTO
 */
@Data
public class ProductPageDTO {
    //分页参数
    private Integer pageNum;
    private Integer pageSize;

    //搜索条件
    private String name;
    private Integer typeId;
    private Boolean onlyInStock;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String shopName;
    private Integer saleStatus;
    private Integer recommend;

    //标记区分是否限定店铺范围
    private Integer shopId;
}
