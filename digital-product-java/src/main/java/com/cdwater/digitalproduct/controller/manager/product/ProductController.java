package com.cdwater.digitalproduct.controller.manager.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Product;
import com.cdwater.digitalproduct.model.dto.ProductPageDTO;
import com.cdwater.digitalproduct.model.vo.ProductGroupVO;
import com.cdwater.digitalproduct.model.vo.ProductPageVO;
import com.cdwater.digitalproduct.model.vo.ProductQueryVO;
import com.cdwater.digitalproduct.service.ProductService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerProductController")
@RequestMapping("/manager/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result add(@RequestBody Product product) {
        productService.add(product);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result removeOne(@PathVariable Integer id) {
        productService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        productService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    public Result edit(@RequestBody Product product) {
        productService.edit(product);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id) {
        ProductQueryVO productQueryVO = productService.query(id);
        return Result.success(productQueryVO);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(ProductPageDTO productPageDTO) {
        PageInfo<ProductPageVO> pageInfo = productService.page(productPageDTO);
        return Result.success(pageInfo);
    }

    /**
     * 数码产品分组查询
     */
    @GetMapping("/group")
    public Result productGroup() {
        List<ProductGroupVO> list = productService.productGroup();
        return Result.success(list);
    }
}
