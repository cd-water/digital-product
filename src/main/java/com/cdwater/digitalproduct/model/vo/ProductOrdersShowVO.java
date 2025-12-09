package com.cdwater.digitalproduct.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数码产品订单展示VO
 */
@Data
public class ProductOrdersShowVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderNo;

    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private Integer shopId;

    private Integer productId;

    private String productName;

    private String productImg;

    private BigDecimal productPrice;

    private String shopName;
}
