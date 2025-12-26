package com.cdwater.digitalproduct.mapper.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.model.dto.AccessoryOrdersPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPageVO;
import com.cdwater.digitalproduct.service.AccessoryOrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("managerAccessoryOrdersController")
@RequestMapping("/manager/accessoryOrders")
public class AccessoryOrdersController {

    @Resource
    private AccessoryOrdersService accessoryOrdersService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(AccessoryOrdersPageDTO accessoryOrdersPageDTO) {
        PageInfo<AccessoryOrdersPageVO> pageInfo = accessoryOrdersService.page(accessoryOrdersPageDTO);
        return Result.success(pageInfo);
    }

    /**
     * 接单
     */
    @PostMapping("/accept/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result acceptOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.acceptOrder(orderNo);
        return Result.success();
    }

    /**
     * 送达
     */
    @PostMapping("/delivery/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result deliveryOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.deliveryOrder(orderNo);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result cancelOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.cancelOrder(orderNo);
        return Result.success();
    }

    /**
     * 售后退款
     */
    @PostMapping("/refund/{orderNo}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result refundOrder(@PathVariable Long orderNo) {
        accessoryOrdersService.refundOrder(orderNo);
        return Result.success();
    }

    /**
     * 查询待接单订单数
     */
    @GetMapping("/count/{shopId}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result count(@PathVariable Integer shopId) {
        Integer count = accessoryOrdersService.count(shopId);
        return Result.success(count);
    }
}
