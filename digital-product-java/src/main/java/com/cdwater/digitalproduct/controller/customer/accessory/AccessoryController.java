package com.cdwater.digitalproduct.controller.customer.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.model.dto.AccessoryPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryShowVO;
import com.cdwater.digitalproduct.service.AccessoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerAccessoryController")
@RequestMapping("/customer/accessory")
public class AccessoryController {

    @Resource
    private AccessoryService accessoryService;

    /**
     * 获取热销数码配件（销量前12名）
     */
    @GetMapping("/visitor/hot")
    public Result getHot() {
        List<AccessoryShowVO> list = accessoryService.getHot();
        return Result.success(list);
    }

    /**
     * 顾客端分页查询
     */
    @GetMapping("/visitor/page")
    public Result page(AccessoryPageDTO accessoryPageDTO) {
        PageInfo<AccessoryShowVO> pageInfo = accessoryService.pagePublic(accessoryPageDTO);
        return Result.success(pageInfo);
    }
}
