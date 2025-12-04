package com.cdwater.digitalproduct.controller.customer.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.entity.AccessoryType;
import com.cdwater.digitalproduct.service.AccessoryTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerAccessoryTypeController")
@RequestMapping("/customer/accessoryType")
public class AccessoryTypeController {

    @Resource
    private AccessoryTypeService accessoryTypeService;

    /**
     * 查询所有类型
     */
    @GetMapping("/visitor/all")
    public Result getAll() {
        List<AccessoryType> list = accessoryTypeService.getAll();
        return Result.success(list);
    }

    /**
     * 查询热门类型（订单数前4名）
     */
    @GetMapping("/visitor/hot")
    public Result getHot() {
        List<AccessoryType> list = accessoryTypeService.getHot();
        return Result.success(list);
    }
}
