package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.SupplierDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierService {
    MessageResponse addSupplier(SupplierDTO supplierDTO);
    MessageResponse updateSupplier(int id,SupplierDTO supplierDTO);
    MessageResponse deleteSuppler(int id);

    Page<Supplier> getList(int page, int size);

}
