package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.service.impl.WhProServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wh-pro-service/")
@RequiredArgsConstructor
public class WhProServiceController {
    private final WhProServiceImpl whProService;

    @PostMapping("add-first-time")
    public ResponseEntity<MessageResponse> addFirstTime() {
        return new ResponseEntity<>(whProService.AddAllProductFirstTime(), HttpStatus.OK);
    }
    @PostMapping("add-product")
    public ResponseEntity<MessageResponse> addProduct(@RequestParam int id, @RequestParam int whid) {
        return new ResponseEntity<>(whProService.addProduct(id,whid), HttpStatus.OK);
    }

}
