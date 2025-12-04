package com.cdwater.digitalproduct.controller.customer.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.entity.ProductType;
import com.cdwater.digitalproduct.service.ProductTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerProductTypeController")
@RequestMapping("/customer/productType")
public class ProductTypeController {

    @Resource
    private ProductTypeService productTypeService;

    /**
     * 查询所有类型
     */
    @GetMapping("/visitor/all")
    public Result getAll() {
        List<ProductType> list = productTypeService.getAll();
        return Result.success(list);
    }

    /**
     * 查询热门类型（有效订单数前4名）
     */
    @GetMapping("/visitor/hot")
    public Result getHot() {
        List<ProductType> list = productTypeService.getHot();
        return Result.success(list);
    }
}
