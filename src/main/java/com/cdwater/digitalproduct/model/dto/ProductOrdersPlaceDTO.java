package com.cdwater.digitalproduct.model.dto;

import lombok.Data;

@Data
public class ProductOrdersPlaceDTO {
    private Integer userId;
    private Integer shopId;
    private Integer productId;
    private Integer addressId;
}
