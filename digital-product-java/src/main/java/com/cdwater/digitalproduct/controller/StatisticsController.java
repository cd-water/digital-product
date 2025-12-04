package com.cdwater.digitalproduct.controller;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.service.*;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 管理员端统计
     */
    @GetMapping("/admin/count")
    @Permission(RoleEnum.ADMIN)
    public Result getAdminCount() {
        Map<String, Object> map = statisticsService.getAdminCount();
        return Result.success(map);
    }

    @GetMapping("/admin/productPie")
    @Permission(RoleEnum.ADMIN)
    public Result getProductPie() {
        List<Map<String, Object>> productPie = statisticsService.getProductPie();
        return Result.success(productPie);
    }


    @GetMapping("/admin/productOrdersBar")
    @Permission(RoleEnum.ADMIN)
    public Result getProductOrdersBar() {
        Map<String, Object> map = statisticsService.getProductOrdersBar();
        return Result.success(map);
    }


    @GetMapping("/admin/accessoryPie")
    @Permission(RoleEnum.ADMIN)
    public Result getAccessoryPie() {
        List<Map<String, Object>> accessoryPie = statisticsService.getAccessoryPie();
        return Result.success(accessoryPie);
    }

    @GetMapping("/admin/accessoryOrdersBar")
    @Permission(RoleEnum.ADMIN)
    public Result getAccessoryOrdersBar() {
        Map<String, Object> map = statisticsService.getAccessoryOrdersBar();
        return Result.success(map);
    }


    /**
     * 店铺端统计
     */
    @GetMapping("/shop/today/{id}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result getShopTodayData(@PathVariable Integer id) {
        Map<String, Object> map = statisticsService.getShopTodayData(id);
        return Result.success(map);
    }

    @GetMapping("/shop/amountLine/{id}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result getShopAmountLine(
            @PathVariable Integer id,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        Map<String, Object> map = statisticsService.getShopAmountLine(id, begin, end);
        return Result.success(map);
    }

    @GetMapping("/shop/numberLine/{id}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result getShopNumberLine(
            @PathVariable Integer id,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        Map<String, Object> map = statisticsService.getShopNumberLine(id, begin, end);
        return Result.success(map);
    }
}
