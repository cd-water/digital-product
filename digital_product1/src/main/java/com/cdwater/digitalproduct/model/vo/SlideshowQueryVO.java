package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

/**
 * 后台轮播图查询VO
 */
@Data
public class SlideshowQueryVO {
    private Integer id;
    private Integer productId;
    private Integer shopId;
    private String img;
}
