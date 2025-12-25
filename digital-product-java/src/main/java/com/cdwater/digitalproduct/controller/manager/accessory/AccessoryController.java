package com.cdwater.digitalproduct.controller.manager.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Accessory;
import com.cdwater.digitalproduct.model.dto.AccessoryPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.service.AccessoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerAccessoryController")
@RequestMapping("/manager/accessory")
public class AccessoryController {

    @Resource
    private AccessoryService accessoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result add(@RequestBody Accessory accessory) {
        accessoryService.add(accessory);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result edit(@RequestBody Accessory accessory) {
        accessoryService.edit(accessory);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result removeOne(@PathVariable Integer id) {
        accessoryService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.DIGITALSHOP)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        accessoryService.removeBatch(ids);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id) {
        AccessoryQueryVO accessoryQueryVO = accessoryService.query(id);
        return Result.success(accessoryQueryVO);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(AccessoryPageDTO accessoryPageDTO) {
        PageInfo<AccessoryPageVO> pageInfo = accessoryService.page(accessoryPageDTO);
        return Result.success(pageInfo);
    }
}
