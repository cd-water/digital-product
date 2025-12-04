package com.cdwater.digitalproduct.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 购物车展示数码配件VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartAccessoryVO {
    private Integer id;
    private Integer userId;
    private Integer quantity;
    private Integer accessoryId;
    private Integer shopId;
    private String accessoryName;
    private String accessoryImg;
    private BigDecimal accessoryPrice;
    private Integer accessoryStore;
    private Integer accessorySaleStatus;
}
