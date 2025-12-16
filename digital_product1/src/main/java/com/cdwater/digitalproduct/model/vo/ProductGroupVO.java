package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGroupVO {
    private Integer shopId;
    private String shopName;
    private List<ProductItem> productList;

    @Data
    public static class ProductItem {
        private Integer productId;
        private String productName;
    }
}
