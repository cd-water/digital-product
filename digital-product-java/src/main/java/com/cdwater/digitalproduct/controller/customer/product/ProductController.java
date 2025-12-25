package com.cdwater.digitalproduct.controller.customer.product;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.model.dto.ProductPageDTO;
import com.cdwater.digitalproduct.model.vo.ProductDetailVO;
import com.cdwater.digitalproduct.model.vo.ProductShowVO;
import com.cdwater.digitalproduct.service.ProductService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerProductController")
@RequestMapping("/customer/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 获取推荐数码产品（共12个）
     */
    @GetMapping("/visitor/recommend")
    public Result getRecommend() {
        List<ProductShowVO> list = productService.getRecommend();
        return Result.success(list);
    }

    /**
     * 顾客端分页查询
     */
    @GetMapping("/visitor/page")
    public Result page(ProductPageDTO productPageDTO) {
        PageInfo<ProductShowVO> pageInfo = productService.pagePublic(productPageDTO);
        return Result.success(pageInfo);
    }

    /**
     * 数码产品详情
     */
    @GetMapping("/visitor/detail/{id}")
    public Result detail(@PathVariable Integer id, Integer userId, Integer role) {
        ProductDetailVO productDetailVO = productService.detail(id, userId, role);
        return Result.success(productDetailVO);
    }
}
