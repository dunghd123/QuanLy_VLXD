package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.SupplierDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.InputInvoice;
import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import com.example.quanly_vlxd.entity.Supplier;
import com.example.quanly_vlxd.repo.InputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.InputInvoiceRepo;
import com.example.quanly_vlxd.repo.SupplierRepo;
import com.example.quanly_vlxd.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;

    @Override
    public MessageResponse addSupplier(SupplierDTO supplierDTO) {
        for(Supplier supplier: supplierRepo.findAll()){
            if(supplier.getName().equals(supplierDTO.getName())
                && supplier.getAddress().equals(supplierDTO.getAddress())){
                return MessageResponse.builder().message("Supplier has existed!!!").build();
            }
            if(supplier.getPhoneNum().equals(supplierDTO.getPhoneNum())){
                return MessageResponse.builder().message("Phone number has existed!!!").build();
            }
        }
        Supplier newSup= Supplier.builder()
                .Name(supplierDTO.getName())
                .Address(supplierDTO.getAddress())
                .PhoneNum(supplierDTO.getPhoneNum())
                .IsActive(true)
                .build();
        supplierRepo.save(newSup);
        return MessageResponse.builder().message("Create new supplier successfully!!!").build();
    }

    @Override
    public MessageResponse updateSupplier(int id,SupplierDTO supplierDTO) {
        Optional<Supplier> supplier= supplierRepo.findById(id);
        if(supplier.isEmpty()){
            return MessageResponse.builder().message("ID: "+id+ " is not exist").build();
        }
        List<Supplier> list= new ArrayList<>(supplierRepo.findAll());
        list.removeIf(s->s.getId()==id);
        for(Supplier s: list){
            if(s.getName().equals(supplierDTO.getName())
                    && s.getAddress().equals(supplierDTO.getAddress())){
                return MessageResponse.builder().message("Supplier has existed!!!").build();
            }
            if(s.getPhoneNum().equals(supplierDTO.getPhoneNum())){
                return MessageResponse.builder().message("Phone number has existed!!!").build();
            }
        }
        Supplier supCur= supplier.get();
        supCur.setName(supplierDTO.getName());
        supCur.setAddress(supplierDTO.getAddress());
        supCur.setPhoneNum(supplierDTO.getPhoneNum());
        supplierRepo.save(supCur);
        return MessageResponse.builder().message("Update information successfully!!!").build();
    }

    @Override
    public MessageResponse deleteSuppler(int id) {
        Optional<Supplier> supplier= supplierRepo.findById(id);
        if(supplier.isEmpty()){
            return MessageResponse.builder().message("ID: "+id+ " is not exist").build();
        }
        for(InputInvoice ip: inputInvoiceRepo.findAll()){
            if(ip.getSupplier().getId()==supplier.get().getId()){
                for(InputInvoiceDetail ipd: inputInvoiceDetailRepo.findAll()){
                    if(ipd.getInputInvoice().getId()==ip.getId()){
                        inputInvoiceDetailRepo.deleteById(ipd.getId());
                    }
                }
                ip.setIsActive(false);
                inputInvoiceRepo.save(ip);
            }
        }
        supplier.get().setIsActive(false);
        supplierRepo.save(supplier.get());
        return MessageResponse.builder().message("Delete Id:"+id+" successfully!!!").build();
    }

    @Override
    public Page<Supplier> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return  supplierRepo.getAll(pageable);
    }
}
