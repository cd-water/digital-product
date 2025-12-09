package com.cdwater.digitalproduct.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 后台宠数码配件订单分页VO
 */
@Data
public class AccessoryOrdersPageVO {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderNo;

    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String accessoryName;

    private String accessoryImg;

    private BigDecimal accessoryPrice;

    private String consignee;

    private String phoneNumber;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String detailAddress;

    private String userNickname;

    private String shopName;
}