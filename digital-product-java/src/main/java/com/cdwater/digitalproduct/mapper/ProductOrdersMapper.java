package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.ProductOrders;
import com.cdwater.digitalproduct.model.dto.ProductOrdersPageDTO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersShowVO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersPageVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProductOrdersMapper {

    List<ProductOrdersPageVO> selectPage(ProductOrdersPageDTO productOrdersPageDTO);

    void insert(ProductOrders productOrders);

    ProductOrders selectByOrderNo(Long orderNo);

    void updateById(ProductOrders productOrders);

    List<ProductOrdersShowVO> selectByUserId(Integer userId);

    List<ProductOrders> selectByOrderStatusAndBeforeTime(Integer orderStatus, LocalDateTime time);

    /**
     * 统计相关
     */
    BigDecimal sumByOrderStatus();

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> getShopAmountMap();

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> selectTodayDataByShopId(Integer shopId);

    List<BigDecimal> selectRangeAmountDataByShopId(Integer shopId, List<LocalDate> dateList);

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Long>> selectRangeNumberDataByShopId(Integer shopId, List<LocalDate> dateList);

    Integer countByShopIdAndOrderStatus(Integer shopId, Integer orderStatus);
}




