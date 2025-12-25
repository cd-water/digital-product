package com.cdwater.digitalproduct.controller;

import com.cdwater.digitalproduct.service.DigitalExpertService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Resource
    private DigitalExpertService digitalExpertService;

    @PostMapping(value = "/chat", produces = "text/event-stream;charset=utf-8")
    public Flux<String> chat(String memoryId, String message) {
        return digitalExpertService.chat(memoryId, message);
    }
}
