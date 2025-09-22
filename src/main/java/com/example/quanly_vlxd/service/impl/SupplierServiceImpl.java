package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.SupplierRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.SupplierResponse;
import com.example.quanly_vlxd.entity.InputInvoice;
import com.example.quanly_vlxd.entity.Supplier;
import com.example.quanly_vlxd.repo.InputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.InputInvoiceRepo;
import com.example.quanly_vlxd.repo.SupplierRepo;
import com.example.quanly_vlxd.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;

    @Override
    public ResponseEntity<MessageResponse> addSupplier(SupplierRequest supplierRequest) {
       boolean isPhoneNumExist= supplierRepo.existsByPhoneNum(supplierRequest.getPhone());
       boolean isNameAndAddressExist= supplierRepo.existsByNameAndAddress(supplierRequest.getName(), supplierRequest.getAddress());
       if(isNameAndAddressExist){
           return ResponseEntity.badRequest().body(MessageResponse.builder().message("Supplier has existed!!!").build());
       }
       if(isPhoneNumExist){
           return ResponseEntity.badRequest().body(MessageResponse.builder().message("Phone number has existed!!!").build());
       }
        Supplier newSup= Supplier.builder()
                .name(supplierRequest.getName())
                .address(supplierRequest.getAddress())
                .phoneNum(supplierRequest.getPhone())
                .isActive(true)
                .build();
        supplierRepo.save(newSup);
        return ResponseEntity.ok(MessageResponse.builder().message("Create new supplier successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateSupplier(int id, SupplierRequest supplierRequest) {
        Optional<Supplier> supplier= supplierRepo.findById(id);
        if(supplier.isEmpty()){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("ID: "+id+ " is not exist").build());
        }
        boolean isPhoneNumExist= supplierRepo.existsByPhoneNumAndIdNot(supplierRequest.getPhone(), id);
        boolean isNameAndAddressExist= supplierRepo.existsByNameAndAddressAndIdNot(supplierRequest.getName(), supplierRequest.getAddress(), id);
        if(isNameAndAddressExist){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Supplier has existed!!!").build());
        }
        if(isPhoneNumExist){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Phone number has existed!!!").build());
        }
        Supplier supCur= supplier.get();
        supCur.setName(supplierRequest.getName());
        supCur.setAddress(supplierRequest.getAddress());
        supCur.setPhoneNum(supplierRequest.getPhone());
        supplierRepo.save(supCur);
        return ResponseEntity.ok(MessageResponse.builder().message("Update information successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> deleteSuppler(int id) {
        Optional<Supplier> supplier= supplierRepo.findById(id);
        if(supplier.isEmpty()){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("ID: "+id+ " is not exist").build());
        }
        for(InputInvoice ip: inputInvoiceRepo.findAll()){
            if(ip.getSupplier().getId()==supplier.get().getId()){
                ip.setActive(false);
                inputInvoiceRepo.save(ip);
            }
        }
        supplier.get().setActive(false);
        supplierRepo.save(supplier.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Delete Id:"+id+" successfully!!!").build());
    }

    @Override
    public Page<SupplierResponse> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return  supplierRepo.getAll(pageable).map(this::convertToDTO);
    }

    @Override
    public List<SupplierResponse> getAllActiveSup() {
        return supplierRepo.getAllActiveSup().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private SupplierResponse convertToDTO(Supplier supplier){
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .phoneNum(supplier.getPhoneNum())
                .isActive(supplier.isActive())
                .build();
    }
}
