package com.cdwater.digitalproduct.model.vo;

import lombok.Data;

/**
 * 店铺详情VO
 */
@Data
public class DigitalShopDetailVO {
    private Integer id;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
    private String introduce;
}
