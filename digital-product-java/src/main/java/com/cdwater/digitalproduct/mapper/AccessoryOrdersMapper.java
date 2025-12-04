package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.AccessoryOrders;
import com.cdwater.digitalproduct.model.dto.AccessoryOrdersPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersShowVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AccessoryOrdersMapper {

    void insert(AccessoryOrders accessoryOrders);

    AccessoryOrders selectByOrderNo(Long orderNo);

    List<AccessoryOrdersPageVO> selectPage(AccessoryOrdersPageDTO accessoryOrdersPageDTO);

    AccessoryOrdersPageVO selectById(Integer id);

    void updateById(AccessoryOrders accessoryOrders);

    List<AccessoryOrdersShowVO> selectByUserId(Integer userId);

    List<AccessoryOrders> selectByOrderStatusAndBeforeTime(Integer orderStatus, LocalDateTime time);
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




