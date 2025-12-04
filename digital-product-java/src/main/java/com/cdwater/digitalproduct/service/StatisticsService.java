package com.cdwater.digitalproduct.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    /**
     * 管理员端统计
     */
    Map<String, Object> getAdminCount();

    List<Map<String, Object>> getProductPie();

    Map<String, Object> getProductOrdersBar();

    List<Map<String, Object>> getAccessoryPie();

    Map<String, Object> getAccessoryOrdersBar();


    /**
     * 店铺端统计
     */
    Map<String, Object> getShopTodayData(Integer id);

    Map<String, Object> getShopAmountLine(Integer id, LocalDate begin, LocalDate end);

    Map<String, Object> getShopNumberLine(Integer id, LocalDate begin, LocalDate end);
}
