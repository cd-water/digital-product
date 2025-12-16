package com.cdwater.digitalproduct.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数码配件订单实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessoryOrders {

    private Integer id;

    private Long orderNo;

    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Integer userId;

    private Integer shopId;

    private Integer accessoryId;

    private String accessoryName;

    private String accessoryImg;

    private BigDecimal accessoryPrice;

    private Integer addressId;

    private String consignee;

    private String phoneNumber;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String detailAddress;
}