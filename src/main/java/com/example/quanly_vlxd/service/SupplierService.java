package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.SupplierRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.SupplierResponse;
import org.springframework.data.domain.Page;

public interface SupplierService {
    MessageResponse addSupplier(SupplierRequest supplierRequest);
    MessageResponse updateSupplier(int id, SupplierRequest supplierRequest);
    MessageResponse deleteSuppler(int id);

    Page<SupplierResponse> getList(int page, int size);

}
