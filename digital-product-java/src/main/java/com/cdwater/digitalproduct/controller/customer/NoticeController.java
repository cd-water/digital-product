package com.cdwater.digitalproduct.controller.customer;

import com.cdwater.digitalproduct.common.Result;
import com.cdwater.digitalproduct.entity.Notice;
import com.cdwater.digitalproduct.service.NoticeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("customerNoticeController")
@RequestMapping("/customer/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 查询最新公告
     */
    @GetMapping("/visitor/latest")
    public Result latest() {
        Notice notice = noticeService.latest();
        return Result.success(notice);
    }
}
