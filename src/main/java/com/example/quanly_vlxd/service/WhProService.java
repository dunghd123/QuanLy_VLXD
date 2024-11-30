package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.response.MessageResponse;

public interface WhProService {
    MessageResponse AddAllProductFirstTime();
    MessageResponse addProduct(int id,int whid);

}
