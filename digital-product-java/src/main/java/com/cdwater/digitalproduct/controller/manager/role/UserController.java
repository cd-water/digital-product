package com.cdwater.digitalproduct.controller.manager.role;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.User;
import com.cdwater.digitalproduct.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerUserController")
@RequestMapping("/manager/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Permission(RoleEnum.ADMIN)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result removeOne(@PathVariable Integer id) {
        userService.removeOne(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/batch")
    @Permission(RoleEnum.ADMIN)
    public Result removeBatch(@RequestBody List<Integer> ids) {
        userService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.ADMIN)
    public Result edit(@RequestBody User user, HttpServletRequest request) {
        userService.edit(user, request);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    @Permission(RoleEnum.ADMIN)
    public Result query(@PathVariable Integer id, HttpServletRequest request) {
        User user = userService.query(id, request);
        return Result.success(user);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @Permission(RoleEnum.ADMIN)
    public Result page(User user,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> pageInfo = userService.page(user, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}