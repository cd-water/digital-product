package com.cdwater.digitalproduct.controller.manager.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.model.dto.*;
import com.cdwater.digitalproduct.model.vo.ProductOrdersPageVO;
import com.cdwater.digitalproduct.service.ProductOrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("managerProductOrdersController")
@RequestMapping("/manager/productOrders")
public class ProductOrdersController {

    @Resource
    private ProductOrdersService productOrdersService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(ProductOrdersPageDTO productOrdersPageDTO) {
        PageInfo<ProductOrdersPageVO> pageInfo = productOrdersService.page(productOrdersPageDTO);
        return Result.success(pageInfo);
    }

    /**
     * 接单
     */
    @PostMapping("/accept/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result acceptOrder(@PathVariable Long orderNo) {
        productOrdersService.acceptOrder(orderNo);
        return Result.success();
    }

    /**
     * 送达
     */
    @PostMapping("/delivery/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result deliveryOrder(@PathVariable Long orderNo) {
        productOrdersService.deliveryOrder(orderNo);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result cancelOrder(@PathVariable Long orderNo) {
        productOrdersService.cancelOrder(orderNo);
        return Result.success();
    }

    /**
     * 售后退款
     */
    @PostMapping("/refund/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result refundOrder(@PathVariable Long orderNo) {
        productOrdersService.refundOrder(orderNo);
        return Result.success();
    }

    /**
     * 查询待接单订单数
     */
    @GetMapping("/count/{shopId}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result count(@PathVariable Integer shopId) {
        Integer count = productOrdersService.count(shopId);
        return Result.success(count);
    }
}
