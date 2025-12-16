package com.cdwater.digitalproduct.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数码配件订单展示VO
 */
@Data
public class AccessoryOrdersShowVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderNo;

    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Integer shopId;

    private String accessoryName;

    private String accessoryImg;

    private BigDecimal accessoryPrice;

    private String shopName;
}
