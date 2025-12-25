package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.model.dto.*;
import com.cdwater.digitalproduct.model.vo.ProductOrdersPlaceVO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersShowVO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersPageVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductOrdersService {
    PageInfo<ProductOrdersPageVO> page(ProductOrdersPageDTO productOrdersPageDTO);

    void acceptOrder(Long orderNo);

    void deliveryOrder(Long orderNo);

    void cancelOrder(Long orderNo);

    void completedOrder(Long orderNo);

    void refundOrder(Long orderNo);

    ProductOrdersPlaceVO placeOrder(ProductOrdersPlaceDTO productOrdersPlaceDTO);

    void paymentOrder(Long orderNo);

    List<ProductOrdersShowVO> list();

    Integer count(Integer shopId);
}
