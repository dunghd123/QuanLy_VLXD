package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.service.impl.WhProServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wh-pro-service/")
@RequiredArgsConstructor
public class WhProServiceController {
    private final WhProServiceImpl whProService;

    @PostMapping("add-first-time")
    public void addFirstTime() { whProService.AddAllProductFirstTime();}
}
