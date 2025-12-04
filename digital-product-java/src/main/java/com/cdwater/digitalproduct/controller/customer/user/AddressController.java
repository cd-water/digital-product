package com.cdwater.digitalproduct.controller.customer.user;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Address;
import com.cdwater.digitalproduct.service.AddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerAddressController")
@RequestMapping("customer/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 查询用户所有地址
     */
    @GetMapping("/list")
    @Permission(RoleEnum.USER)
    public Result list() {
        List<Address> list = addressService.list();
        return Result.success(list);
    }

    /**
     * 新增地址
     */
    @PostMapping("/add")
    @Permission(RoleEnum.USER)
    public Result add(@RequestBody Address address) {
        addressService.add(address);
        return Result.success();
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/remove/{id}")
    @Permission(RoleEnum.USER)
    public Result removeOne(@PathVariable Integer id) {
        addressService.removeOne(id);
        return Result.success();
    }

    /**
     * 修改地址
     */
    @PutMapping("/edit")
    @Permission(RoleEnum.USER)
    public Result edit(@RequestBody Address address) {
        addressService.edit(address);
        return Result.success();
    }

    /**
     * id查询
     */
    @GetMapping("/query/{id}")
    @Permission(RoleEnum.USER)
    public Result query(@PathVariable Integer id) {
        Address address = addressService.query(id);
        return Result.success(address);
    }
}
