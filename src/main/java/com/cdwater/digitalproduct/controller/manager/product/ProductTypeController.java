package com.cdwater.digitalproduct.controller.manager.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.ProductType;
import com.cdwater.digitalproduct.service.ProductTypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerProductTypeController")
@RequestMapping("/manager/productType")
public class
ProductTypeController {

    @Resource
    private ProductTypeService productTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.ADMIN)
    public Result add(@RequestBody ProductType productType) {
        productTypeService.add(productType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result removeOne(@PathVariable Integer id) {
        productTypeService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.ADMIN)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        productTypeService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.ADMIN)
    public Result edit(@RequestBody ProductType productType) {
        productTypeService.edit(productType);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id) {
        ProductType productType = productTypeService.query(id);
        return Result.success(productType);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(ProductType productType,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ProductType> pageInfo = productTypeService.page(productType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 查询所有类型
     */
    @GetMapping("/list")
    public Result list() {
        List<ProductType> list = productTypeService.getAll();
        return Result.success(list);
    }
}
