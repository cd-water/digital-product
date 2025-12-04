package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.model.dto.*;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPlaceVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersShowVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AccessoryOrdersService {
    PageInfo<AccessoryOrdersPageVO> page(AccessoryOrdersPageDTO accessoryOrdersPageDTO);

    void acceptOrder(Long orderNo);

    void deliveryOrder(Long orderNo);

    void cancelOrder(Long orderNo);

    void completedOrder(Long orderNo);

    void refundOrder(Long orderNo);

    List<AccessoryOrdersPlaceVO> placeOrder(AccessoryOrdersPlaceDTO accessoryOrdersPlaceDTO);

    void paymentOrder(List<Long> orderNoArray);

    List<AccessoryOrdersShowVO> list();

    Integer count(Integer shopId);
}
