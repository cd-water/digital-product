package com.cdwater.digitalproduct.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 后台数码产品订单分页VO
 */
@Data
public class ProductOrdersPageVO {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderNo;

    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private String productName;

    private String productImg;

    private BigDecimal productPrice;

    private String consignee;

    private String phoneNumber;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String detailAddress;

    private String userNickname;

    private String shopName;
}