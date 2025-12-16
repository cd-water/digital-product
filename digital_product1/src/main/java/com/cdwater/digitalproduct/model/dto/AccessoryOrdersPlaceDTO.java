package com.cdwater.digitalproduct.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccessoryOrdersPlaceDTO {
    private Integer userId;
    private Integer addressId;
    private List<SelectAccessoryItem> selectAccessories;

    @Data
    public static class SelectAccessoryItem {
        private Integer accessoryId;
        private Integer quantity;
    }
}
