package com.cdwater.digitalproduct.controller.customer.user;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Cart;
import com.cdwater.digitalproduct.model.vo.CartAccessoryVO;
import com.cdwater.digitalproduct.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerCartController")
@RequestMapping("customer/cart")
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 查询用户购物车所有数码配件
     */
    @GetMapping("/list")
    @Permission(RoleEnum.USER)
    public Result list() {
        List<CartAccessoryVO> list = cartService.list();
        return Result.success(list);
    }

    /**
     * 加入购物车
     */
    @PostMapping("/join")
    @Permission(RoleEnum.USER)
    public Result join(@RequestBody Cart cart) {
        cartService.join(cart);
        return Result.success();
    }

    /**
     * 移出购物车
     */
    @DeleteMapping("/out")
    @Permission(RoleEnum.USER)
    public Result out(Integer userId, Integer accessoryId) {
        cartService.out(userId, accessoryId);
        return Result.success();
    }
}
