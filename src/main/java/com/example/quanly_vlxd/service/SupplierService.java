package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.SupplierRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface SupplierService {
    ResponseEntity<MessageResponse> addSupplier(SupplierRequest supplierRequest);
    ResponseEntity<MessageResponse> updateSupplier(int id, SupplierRequest supplierRequest);
    ResponseEntity<MessageResponse> deleteSuppler(int id);

    Page<SupplierResponse> getList(int page, int size);

}
