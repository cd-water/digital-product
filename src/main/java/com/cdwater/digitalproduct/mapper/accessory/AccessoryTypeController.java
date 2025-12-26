package com.cdwater.digitalproduct.mapper.accessory;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.AccessoryType;
import com.cdwater.digitalproduct.service.AccessoryTypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerAccessoryTypeController")
@RequestMapping("/manager/accessoryType")
public class AccessoryTypeController {

    @Resource
    private AccessoryTypeService accessoryTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.ADMIN)
    public Result add(@RequestBody AccessoryType accessoryType) {
        accessoryTypeService.add(accessoryType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result removeOne(@PathVariable Integer id) {
        accessoryTypeService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.ADMIN)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        accessoryTypeService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.ADMIN)
    public Result edit(@RequestBody AccessoryType accessoryType) {
        accessoryTypeService.edit(accessoryType);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id) {
        AccessoryType accessoryType = accessoryTypeService.query(id);
        return Result.success(accessoryType);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result page(AccessoryType accessoryType,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<AccessoryType> pageInfo = accessoryTypeService.page(accessoryType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 查询所有类型
     */
    @GetMapping("/list")
    public Result list() {
        List<AccessoryType> list = accessoryTypeService.getAll();
        return Result.success(list);
    }
}