package com.cdwater.digitalproduct.controller.customer.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.model.dto.ProductOrdersPlaceDTO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersPlaceVO;
import com.cdwater.digitalproduct.model.vo.ProductOrdersShowVO;
import com.cdwater.digitalproduct.service.ProductOrdersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerProductOrdersController")
@RequestMapping("customer/productOrders")
public class ProductOrdersController {

    @Resource
    private ProductOrdersService productOrdersService;

    /**
     * 用户下单
     */
    @PostMapping("/place")
    @Permission(RoleEnum.USER)
    public Result placeOrder(@RequestBody ProductOrdersPlaceDTO productOrdersPlaceDTO) {
        ProductOrdersPlaceVO productOrdersPlaceVO = productOrdersService.placeOrder(productOrdersPlaceDTO);
        return Result.success(productOrdersPlaceVO);
    }

    /**
     * 用户取消订单
     */
    @PostMapping("/cancel/{orderNo}")
    @Permission(RoleEnum.USER)
    public Result cancelOrder(@PathVariable Long orderNo) {
        productOrdersService.cancelOrder(orderNo);
        return Result.success();
    }

    /**
     * 用户完成订单
     */
    @PostMapping("/completed/{orderNo}")
    @Permission(RoleEnum.USER)
    public Result completedOrder(@PathVariable Long orderNo) {
        productOrdersService.completedOrder(orderNo);
        return Result.success();
    }

    /**
     * 用户订单支付
     */
    @PostMapping("/payment/{orderNo}")
    @Permission(RoleEnum.USER)
    public Result paymentOrder(@PathVariable Long orderNo) {
        productOrdersService.paymentOrder(orderNo);
        return Result.success();
    }

    /**
     * 查询所有订单
     */
    @GetMapping("/list")
    @Permission(RoleEnum.USER)
    public Result list() {
        List<ProductOrdersShowVO> list = productOrdersService.list();
        return Result.success(list);
    }
}
