package com.cdwater.digitalproduct.model.dto;

import lombok.Data;

/**
 * 店铺分页DTO
 */
@Data
public class DigitalShopPageDTO {
    //分页参数
    private Integer pageNum;
    private Integer pageSize;

    //搜索条件
    private String nickname;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private Integer auditStatus;
}
