package com.cdwater.digitalproduct.controller.customer;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.entity.Slideshow;
import com.cdwater.digitalproduct.service.SlideshowService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerSlideshowController")
@RequestMapping("/customer/slideshow")
public class SlideshowController {

    @Resource
    private SlideshowService slideshowService;

    /**
     * 获取轮播图列表
     */
    @GetMapping("/visitor/list")
    public Result list() {
        List<Slideshow> list = slideshowService.list();
        return Result.success(list);
    }
}
