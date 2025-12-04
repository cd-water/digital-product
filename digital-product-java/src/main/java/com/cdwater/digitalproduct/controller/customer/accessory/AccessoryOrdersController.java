package com.cdwater.digitalproduct.controller.customer.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.model.dto.AccessoryOrdersPlaceDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPlaceVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersShowVO;
import com.cdwater.digitalproduct.service.AccessoryOrdersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerAccessoryOrdersController")
@RequestMapping("customer/accessoryOrders")
public class AccessoryOrdersController {

    @Resource
    private AccessoryOrdersService accessoryOrdersService;

    /**
     * 用户下单
     */
    @PostMapping("/place")
    @Permission(RoleEnum.USER)
    public Result placeOrder(@RequestBody AccessoryOrdersPlaceDTO accessoryOrdersPlaceDTO) {
        List<AccessoryOrdersPlaceVO> list = accessoryOrdersService.placeOrder(accessoryOrdersPlaceDTO);
        return Result.success(list);
    }

    /**
     * 用户取消订单
     */
    @PostMapping("/cancel/{orderNo}")
    @Permission(RoleEnum.USER)
    public Result cancelOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.cancelOrder(orderNo);
        return Result.success();
    }

    /**
     * 用户完成订单
     */
    @PostMapping("/completed/{orderNo}")
    @Permission(RoleEnum.USER)
    public Result completedOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.completedOrder(orderNo);
        return Result.success();
    }

    /**
     * 用户订单支付
     */
    @PostMapping("/payment")
    @Permission(RoleEnum.USER)
    public Result paymentOrder(@RequestBody List<Long> orderNoArray) {
        accessoryOrdersService.paymentOrder(orderNoArray);
        return Result.success();
    }

    /**
     * 查询所有订单
     */
    @GetMapping("/list")
    @Permission(RoleEnum.USER)
    public Result list() {
        List<AccessoryOrdersShowVO> list = accessoryOrdersService.list();
        return Result.success(list);
    }
}
