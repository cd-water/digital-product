package com.cdwater.digitalproduct.entity;

import lombok.Data;

/**
 * 轮播图实体类
 */
@Data
public class Slideshow {

    private Integer id;

    private Integer productId;

    private Integer shopId;

    private String img;
}