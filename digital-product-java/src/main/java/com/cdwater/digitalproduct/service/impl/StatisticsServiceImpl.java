package com.cdwater.digitalproduct.service.impl;

import com.cdwater.digitalproduct.common.constants.*;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.mapper.*;
import com.cdwater.digitalproduct.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private DigitalShopMapper digitalShopMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private AccessoryMapper accessoryMapper;
    @Resource
    private ProductOrdersMapper productOrdersMapper;
    @Resource
    private AccessoryOrdersMapper accessoryOrdersMapper;

    /**
     * 管理员端统计
     */
    @Override
    public Map<String, Object> getAdminCount() {
        Integer settledShopCount = digitalShopMapper.countByAuditStatus(TextInfo.AUDIT_SUCCESS);
        Integer saleProductCount = productMapper.countBySaleStatus(TextInfo.ON_SALE);
        Integer saleAccessoryCount = accessoryMapper.countBySaleStatus(TextInfo.ON_SALE);
        BigDecimal productOrdersAmount = productOrdersMapper.sumByOrderStatus();
        BigDecimal accessoryOrdersAmount = accessoryOrdersMapper.sumByOrderStatus();

        Map<String, Object> map = new HashMap<>();
        map.put("settledShopCount", settledShopCount);
        map.put("saleProductCount", saleProductCount);
        map.put("saleAccessoryCount", saleAccessoryCount);
        map.put("productOrdersAmount", productOrdersAmount);
        map.put("accessoryOrdersAmount", accessoryOrdersAmount);
        return map;
    }

    @Override
    public List<Map<String, Object>> getProductPie() {
        return productMapper.getTypeCountMap();
    }

    @Override
    public Map<String, Object> getProductOrdersBar() {
        List<Map<String, Object>> mapList = productOrdersMapper.getShopAmountMap();

        List<String> shopNameList = new ArrayList<>();
        List<BigDecimal> amountList = new ArrayList<>();

        mapList.forEach(item -> {
            shopNameList.add((String) item.get("shopName"));
            amountList.add((BigDecimal) item.get("amount"));
        });

        Map<String, Object> map = new HashMap<>();
        map.put("shopNameList", shopNameList);
        map.put("amountList", amountList);
        return map;
    }

    @Override
    public List<Map<String, Object>> getAccessoryPie() {
        return accessoryMapper.getTypeCountMap();
    }

    @Override
    public Map<String, Object> getAccessoryOrdersBar() {
        List<Map<String, Object>> mapList = accessoryOrdersMapper.getShopAmountMap();

        List<String> shopNameList = new ArrayList<>();
        List<BigDecimal> amountList = new ArrayList<>();

        mapList.forEach(item -> {
            shopNameList.add((String) item.get("shopName"));
            amountList.add((BigDecimal) item.get("amount"));
        });

        Map<String, Object> map = new HashMap<>();
        map.put("shopNameList", shopNameList);
        map.put("amountList", amountList);
        return map;
    }


    /**
     * 店铺端统计
     */
    @Override
    public Map<String, Object> getShopTodayData(Integer id) {
        //只允许店铺查看本店的今日数据
        if (!ThreadUtil.hasPermission(id, RoleType.DIGITALSHOP)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //数码产品订单数据
        List<Map<String, Object>> productOrdersMapList = productOrdersMapper.selectTodayDataByShopId(id);
        BigDecimal productOrdersTurnover = BigDecimal.ZERO;
        Long todayProductOrders = 0L;
        for (Map<String, Object> item : productOrdersMapList) {
            BigDecimal price = (BigDecimal) item.get("amount");
            if (price != null && !OrderStatus.CANCELLED.equals(((Long) item.get("status")).intValue())) {
                productOrdersTurnover = productOrdersTurnover.add(price);
            }
            Long num = (Long) item.get("count");
            todayProductOrders += num;
        }

        Map<String, Object> productOrdersMap = new HashMap<>();
        productOrdersMap.put("turnover", productOrdersTurnover);
        productOrdersMap.put("todayOrders", todayProductOrders);
        productOrdersMap.put("pendingPayment", productOrdersMapList.get(OrderStatus.PENDING_PAYMENT).get("count"));
        productOrdersMap.put("pendingAccept", productOrdersMapList.get(OrderStatus.PENDING_ACCEPT).get("count"));
        productOrdersMap.put("delivering", productOrdersMapList.get(OrderStatus.DELIVERING).get("count"));
        productOrdersMap.put("delivered", productOrdersMapList.get(OrderStatus.DELIVERED).get("count"));
        productOrdersMap.put("completed", productOrdersMapList.get(OrderStatus.COMPLETED).get("count"));
        productOrdersMap.put("cancelled", productOrdersMapList.get(OrderStatus.CANCELLED).get("count"));


        //数码配件订单数据
        List<Map<String, Object>> accessoryOrdersMapList = accessoryOrdersMapper.selectTodayDataByShopId(id);
        BigDecimal accessoryOrdersTurnover = BigDecimal.ZERO;
        Long todayAccessoryOrders = 0L;
        for (Map<String, Object> item : accessoryOrdersMapList) {
            BigDecimal price = (BigDecimal) item.get("amount");
            if (price != null && !OrderStatus.CANCELLED.equals(((Long) item.get("status")).intValue())) {
                accessoryOrdersTurnover = accessoryOrdersTurnover.add(price);
            }
            Long num = (Long) item.get("count");
            todayAccessoryOrders += num;
        }

        Map<String, Object> accessoryOrdersMap = new HashMap<>();
        accessoryOrdersMap.put("turnover", accessoryOrdersTurnover);
        accessoryOrdersMap.put("todayOrders", todayAccessoryOrders);
        accessoryOrdersMap.put("pendingPayment", accessoryOrdersMapList.get(OrderStatus.PENDING_PAYMENT).get("count"));
        accessoryOrdersMap.put("pendingAccept", accessoryOrdersMapList.get(OrderStatus.PENDING_ACCEPT).get("count"));
        accessoryOrdersMap.put("delivering", accessoryOrdersMapList.get(OrderStatus.DELIVERING).get("count"));
        accessoryOrdersMap.put("delivered", accessoryOrdersMapList.get(OrderStatus.DELIVERED).get("count"));
        accessoryOrdersMap.put("completed", accessoryOrdersMapList.get(OrderStatus.COMPLETED).get("count"));
        accessoryOrdersMap.put("cancelled", accessoryOrdersMapList.get(OrderStatus.CANCELLED).get("count"));

        //汇总统计
        Map<String, Object> map = new HashMap<>();
        map.put("turnover", ((BigDecimal) productOrdersMap.get("turnover")).add((BigDecimal) accessoryOrdersMap.get("turnover")));
        map.put("todayOrders", (Long) productOrdersMap.get("todayOrders") + (Long) accessoryOrdersMap.get("todayOrders"));
        map.put("pendingPayment", (Long) productOrdersMap.get("pendingPayment") + (Long) accessoryOrdersMap.get("pendingPayment"));
        map.put("pendingAccept", (Long) productOrdersMap.get("pendingAccept") + (Long) accessoryOrdersMap.get("pendingAccept"));
        map.put("delivering", (Long) productOrdersMap.get("delivering") + (Long) accessoryOrdersMap.get("delivering"));
        map.put("delivered", (Long) productOrdersMap.get("delivered") + (Long) accessoryOrdersMap.get("delivered"));
        map.put("completed", (Long) productOrdersMap.get("completed") + (Long) accessoryOrdersMap.get("completed"));
        map.put("cancelled", (Long) productOrdersMap.get("cancelled") + (Long) accessoryOrdersMap.get("cancelled"));
        return map;
    }

    @Override
    public Map<String, Object> getShopAmountLine(Integer id, LocalDate begin, LocalDate end) {
        //只允许店铺查看本店的营业数据
        if (!ThreadUtil.hasPermission(id, RoleType.DIGITALSHOP)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //计算日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //计算金额列表（有效）
        List<BigDecimal> productAmountList = productOrdersMapper.selectRangeAmountDataByShopId(id, dateList);
        List<BigDecimal> accessoryAmountList = accessoryOrdersMapper.selectRangeAmountDataByShopId(id, dateList);

        //合并金额列表（有效）
        List<BigDecimal> addAmountList = IntStream.range(0, productAmountList.size())
                .mapToObj(i -> {
                    BigDecimal productAmount = productAmountList.get(i) != null ? productAmountList.get(i) : BigDecimal.ZERO;
                    BigDecimal accessoryAmount = accessoryAmountList.get(i) != null ? accessoryAmountList.get(i) : BigDecimal.ZERO;
                    return productAmount.add(accessoryAmount);
                })
                .toList();

        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dateList);
        map.put("amountList", addAmountList);
        return map;
    }

    @Override
    public Map<String, Object> getShopNumberLine(Integer id, LocalDate begin, LocalDate end) {
        //只允许店铺查看本店的营业数据
        if (!ThreadUtil.hasPermission(id, RoleType.DIGITALSHOP)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //计算日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //计算订单列表（总&有效）
        List<Map<String, Long>> productNumberList = productOrdersMapper.selectRangeNumberDataByShopId(id, dateList);
        List<Map<String, Long>> accessoryNumberList = accessoryOrdersMapper.selectRangeNumberDataByShopId(id, dateList);

        //合并订单列表（总&有效）
        List<Long> totalNumberList = IntStream.range(0, productNumberList.size())
                .mapToObj(i -> productNumberList.get(i).get("totalNumber") + accessoryNumberList.get(i).get("totalNumber"))
                .toList();
        List<Long> validNumberList = IntStream.range(0, productNumberList.size())
                .mapToObj(i -> productNumberList.get(i).get("validNumber") + accessoryNumberList.get(i).get("validNumber"))
                .toList();

        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dateList);
        map.put("totalNumberList", totalNumberList);
        map.put("validNumberList", validNumberList);
        return map;
    }
}
