package com.cdwater.digitalproduct.controller.manager.role;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.DigitalShop;
import com.cdwater.digitalproduct.model.dto.DigitalShopPageDTO;
import com.cdwater.digitalproduct.service.DigitalShopService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerDigitalShopController")
@RequestMapping("/manager/digitalShop")
public class DigitalShopController {

    @Resource
    private DigitalShopService digitalShopService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.ADMIN)
    public Result add(@RequestBody DigitalShop digitalShop) {
        digitalShopService.add(digitalShop);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result removeOne(@PathVariable Integer id) {
        digitalShopService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.ADMIN)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        digitalShopService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    public Result edit(@RequestBody DigitalShop digitalShop, HttpServletRequest request) {
        digitalShopService.edit(digitalShop, request);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id, HttpServletRequest request) {
        DigitalShop digitalShop = digitalShopService.query(id, request);
        return Result.success(digitalShop);
    }

    /**
     * 分页查询
     */
    @Permission(RoleEnum.ADMIN)
    @GetMapping("/page")
    public Result page(DigitalShopPageDTO digitalShopPageDTO) {
        PageInfo<DigitalShop> pageInfo = digitalShopService.page(digitalShopPageDTO);
        return Result.success(pageInfo);
    }
}
