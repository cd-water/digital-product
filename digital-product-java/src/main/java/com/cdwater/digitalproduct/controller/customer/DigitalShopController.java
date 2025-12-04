package com.cdwater.digitalproduct.controller.customer;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.model.dto.DigitalShopPageDTO;
import com.cdwater.digitalproduct.model.vo.DigitalShopShowVO;
import com.cdwater.digitalproduct.model.vo.DigitalShopDetailVO;
import com.cdwater.digitalproduct.service.DigitalShopService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerDigitalShopController")
@RequestMapping("/customer/digitalShop")
public class DigitalShopController {

    @Resource
    private DigitalShopService digitalShopService;

    /**
     * 获取热门店铺（订单数前6名）
     */
    @GetMapping("/visitor/hot")
    public Result getHot() {
        List<DigitalShopShowVO> list = digitalShopService.getHot();
        return Result.success(list);
    }

    /**
     * 顾客端分页查询
     */
    @GetMapping("/visitor/page")
    public Result page(DigitalShopPageDTO digitalShopPageDTO) {
        PageInfo<DigitalShopShowVO> pageInfo = digitalShopService.pagePublic(digitalShopPageDTO);
        return Result.success(pageInfo);
    }

    /**
     * 店铺详情
     */
    @GetMapping("/visitor/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        DigitalShopDetailVO digitalShopDetailVO = digitalShopService.detail(id);
        return Result.success(digitalShopDetailVO);
    }
}
