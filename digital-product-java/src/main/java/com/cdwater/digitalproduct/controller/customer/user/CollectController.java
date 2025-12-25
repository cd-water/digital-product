package com.cdwater.digitalproduct.controller.customer.user;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.RoleEnum;
import com.cdwater.digitalproduct.entity.Collect;
import com.cdwater.digitalproduct.model.vo.CollectProductVO;
import com.cdwater.digitalproduct.service.CollectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerCollectController")
@RequestMapping("customer/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    /**
     * 查询用户所有收藏
     */
    @GetMapping("/list")
    @Permission(RoleEnum.USER)
    public Result list() {
        List<CollectProductVO> list = collectService.list();
        return Result.success(list);
    }

    /**
     * 加入收藏夹
     */
    @PostMapping("/join")
    @Permission(RoleEnum.USER)
    public Result join(@RequestBody Collect collect) {
        collectService.join(collect);
        return Result.success();
    }

    /**
     * 移出收藏夹
     */
    @DeleteMapping("/out")
    @Permission(RoleEnum.USER)
    public Result out(Integer userId, Integer productId) {
        collectService.out(userId, productId);
        return Result.success();
    }
}
