package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.WarehouseRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.WarehouseResponse;
import org.springframework.data.domain.Page;

public interface WarehouseService {
    MessageResponse addWarehouse(WarehouseRequest warehouseRequest);
    MessageResponse updateWarehouse(int id, WarehouseRequest warehouseRequest);
    MessageResponse deleteWarehouse(int id);

    Page<WarehouseResponse> getList(int page, int size);
}
