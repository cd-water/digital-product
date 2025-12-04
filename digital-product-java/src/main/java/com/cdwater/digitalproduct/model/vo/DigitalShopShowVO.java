package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

/**
 * 展示店铺VO
 */
@Data
public class DigitalShopShowVO {
    private Integer id;
    private String nickname;
    private String avatar;
    private String phone;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
}
