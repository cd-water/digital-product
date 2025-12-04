package com.cdwater.digitalproduct.controller.manager.role;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Admin;
import com.cdwater.digitalproduct.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerAdminController")
@RequestMapping("/manager/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.ADMIN)
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result removeOne(@PathVariable Integer id) {
        adminService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.ADMIN)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        adminService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.ADMIN)
    public Result edit(@RequestBody Admin admin, HttpServletRequest request) {
        adminService.edit(admin, request);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result query(@PathVariable Integer id, HttpServletRequest request) {
        Admin admin = adminService.query(id, request);
        return Result.success(admin);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @Permission(RoleEnum.ADMIN)
    public Result page(Admin admin,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> pageInfo = adminService.page(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
