package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.OutputInvoiceDetailRequest;
import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceDetailResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.OutputInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OutputInvoiceServiceImpl implements OutputInvoiceService {
    private final OutputInvoiceRepo outputInvoiceRepo;
    private final OutputInvoiceDetailRepo outputInvoiceDetailRepo;
    private final CustomerRepo customerRepo;
    private final WareHouseRepo wareHouseRepo;
    private final WarehouseProductRepo warehouseProductRepo;
    private final ProductRepo productRepo;
    private final EmployeeRepo employeeRepo;
    private final PriceHistoryRepo priceHistoryRepo;
    public double getOutputPriceByProductID(int proid, Date creationTime) {
        for(ProductPriceHistory p: priceHistoryRepo.findAll()) {
            if (p.getProduct().getId() == proid
                    && p.getInvoiceType().equals(InvoiceTypeEnums.OUTPUT)) {
                if (p.getEndDate() == null || p.getEndDate().after(creationTime)) {
                    return p.getPrice();
                }
            }
        }
        return 0;
    }
    public OutputInvoiceDetail createOutputInvoiceDetail(OutputInvoiceDetailRequest outputInvoiceDetailRequest,double quantity, double price, OutputInvoice savedOutputInvoice,Warehouse warehouse) {
        return OutputInvoiceDetail.builder()
                .outputInvoice(savedOutputInvoice)
                .warehouse(warehouse)
                .Pro_Id(outputInvoiceDetailRequest.getPro_id())
                .unitPrice(price)
                .quantity(quantity)
                .Amount(price*quantity)
                .build();
    }
    public static OutputInvoice createOutputInvoice(OutputInvoiceRequest outputInvoiceRequest, Customer customer, Employee employee) {
        return OutputInvoice.builder()
                .customer(customer)
                .employee(employee)
                .CreationTime(outputInvoiceRequest.getCreationTime())
                .UpdateTime(outputInvoiceRequest.getCreationTime())
                .ShipAddress(outputInvoiceRequest.getShipAddress())
                .TotalAmount(0)
                .Status(false)
                .IsActive(true)
                .build();

    }
    @Override
    public MessageResponse addOutputInvoice(OutputInvoiceRequest outputInvoiceRequest) {
        Optional<Customer> customer = customerRepo.findById(outputInvoiceRequest.getCusID());
        if (customer.isEmpty())
            return MessageResponse.builder().message("Customer ID: " + outputInvoiceRequest.getCusID() + " is not exist").build();
        Optional<Employee> employee = employeeRepo.findById(outputInvoiceRequest.getEmpID());
        if(employee.isEmpty())
            return MessageResponse.builder().message("Employee ID: " + outputInvoiceRequest.getEmpID() + " is not exist").build();
        //Lưu thông tin hoá đơn
        OutputInvoice outputInvoice = createOutputInvoice(outputInvoiceRequest, customer.get(), employee.get());
        OutputInvoice savedOutputInvoice = outputInvoiceRepo.save(outputInvoice);
        // Lưu các chi tiết hoá đơn
        List<OutputInvoiceDetail> outputInvoiceDetailList = new ArrayList<>();
        for(OutputInvoiceDetailRequest outputInvoiceDetailRequest: outputInvoiceRequest.getListOutputDetails()) {
            Optional<Product> product = productRepo.findById(outputInvoiceDetailRequest.getPro_id());
            if(product.isEmpty())
                return MessageResponse.builder().message("Product ID: " + outputInvoiceDetailRequest.getPro_id() + " is not exist").build();
            Optional<Warehouse> warehouse = wareHouseRepo.findById(outputInvoiceDetailRequest.getWh_id());
            if(warehouse.isEmpty())
                return MessageResponse.builder().message("Warehouse ID: " + outputInvoiceDetailRequest.getWh_id() + " is not exist").build();

            double quantity= outputInvoiceDetailRequest.getQuantity();
            double price = getOutputPriceByProductID(outputInvoiceDetailRequest.getPro_id(), outputInvoiceRequest.getCreationTime());
            OutputInvoiceDetail outputInvoiceDetail = createOutputInvoiceDetail(outputInvoiceDetailRequest, quantity, price, savedOutputInvoice, warehouse.get());

            outputInvoiceDetailList.add(outputInvoiceDetail);
            // Update số lượng tồn kho
            Optional<WareHouse_Product> wareHouseProduct = warehouseProductRepo.findByWarehouseAndProduct(outputInvoiceDetailRequest.getWh_id(), outputInvoiceDetailRequest.getPro_id());
            if(wareHouseProduct.isEmpty())
                return MessageResponse.builder().message(product.get().getName() + " is not exist in warehouse : " + warehouse.get().getName()).build();
            else {
                if(wareHouseProduct.get().getQuantity() < quantity){
                    outputInvoiceRepo.delete(savedOutputInvoice);
                    return MessageResponse.builder()
                            .message("Product " + outputInvoiceDetailRequest.getPro_id() + ". Current quantity in warehouse: "+wareHouseProduct.get().getQuantity()+  " " +product.get().getUnitMeasure())
                            .build();
                }
                wareHouseProduct.get().setQuantity(wareHouseProduct.get().getQuantity() - quantity);
                wareHouseProduct.get().setLastUpdated(new Date());
                warehouseProductRepo.save(wareHouseProduct.get());
            }
        }
        outputInvoiceDetailRepo.saveAll(outputInvoiceDetailList);
        // Cập nhật tổng tiền
        double total=0.0;
        for(OutputInvoiceDetail oid: outputInvoiceDetailList){
            total += oid.getAmount();
        }
        savedOutputInvoice.setTotalAmount(total);
        outputInvoiceRepo.save(savedOutputInvoice);
        return MessageResponse.builder().message("Add output invoice successfully").build();
    }

    @Override
    public MessageResponse updateOutputInvoice(int id) {
        Optional<OutputInvoice> outputInvoice = outputInvoiceRepo.findById(id);
        if(outputInvoice.isEmpty())
            return MessageResponse.builder().message("Output invoice ID: " + id + " is not exist").build();
        outputInvoice.get().setUpdateTime(new Date());
        outputInvoice.get().setStatus(true);
        outputInvoiceRepo.save(outputInvoice.get());
        return MessageResponse.builder().message("the output invoice has been completed").build();
    }

    @Override
    public MessageResponse deleteOutputInvoice(int id) {
        Optional<OutputInvoice> outputInvoice = outputInvoiceRepo.findById(id);
        if(outputInvoice.isEmpty())
            return MessageResponse.builder().message("Output invoice ID: " + id + " is not exist").build();
        for(OutputInvoiceDetail oid: outputInvoiceDetailRepo.findAll()){
            Optional<WareHouse_Product> warehouseProduct = warehouseProductRepo.findByWarehouseAndProduct(oid.getWarehouse().getId(), oid.getPro_Id());
            if (warehouseProduct.isPresent()) {
                warehouseProduct.get().setQuantity(warehouseProduct.get().getQuantity() + oid.getQuantity());
                warehouseProduct.get().setLastUpdated(new Date());
                warehouseProductRepo.save(warehouseProduct.get());
            }
            outputInvoiceDetailRepo.delete(oid);
        }
        outputInvoice.get().setIsActive(false);
        outputInvoiceRepo.save(outputInvoice.get());
        return MessageResponse.builder().message("Delete output invoice successfully").build();
    }

    @Override
    public OutputInvoiceResponse findOutputInvoice(int id) {
        Optional<OutputInvoice> outputInvoice = outputInvoiceRepo.findByOutputInvoiceId(id);
        return outputInvoice.map(this::convertOutputInvoiceToOutputInvoiceResponse).orElse(null);
    }
    public OutputInvoiceResponse convertOutputInvoiceToOutputInvoiceResponse(OutputInvoice outputInvoice){
        return OutputInvoiceResponse.builder()
                .id(outputInvoice.getId())
                .cusName(outputInvoice.getCustomer().getName())
                .empName(outputInvoice.getEmployee().getName())
                .creationTime(outputInvoice.getCreationTime())
                .updateTime(outputInvoice.getUpdateTime())
                .shipAddress(outputInvoice.getShipAddress())
                .status(outputInvoice.isStatus())
                .listOutputInvoiceDetails(convertToSetOutputInvoiceDetailResponse(outputInvoice.getOutputInvoiceDetails()))
                .totalAmount(outputInvoice.getTotalAmount())
                .build();
    }
    public OutputInvoiceDetailResponse convertOutputInvoiceDetailToOutputInvoiceDetailResponse(OutputInvoiceDetail outputInvoiceDetail){
        Optional<Product> product  = productRepo.findById(outputInvoiceDetail.getPro_Id());
        return product.map(value -> OutputInvoiceDetailResponse.builder()
                .id(outputInvoiceDetail.getId())
                .productName(value.getName())
                .warehouseName(outputInvoiceDetail.getWarehouse().getName())
                .unitMeasure(value.getUnitMeasure())
                .quantity(outputInvoiceDetail.getQuantity())
                .price(outputInvoiceDetail.getUnitPrice())
                .amount(outputInvoiceDetail.getAmount())
                .build()).orElse(null);

    }
    public Set<OutputInvoiceDetailResponse> convertToSetOutputInvoiceDetailResponse(Set<OutputInvoiceDetail> outputInvoiceDetails){
        Set<OutputInvoiceDetailResponse> outputInvoiceDetailResponses = new HashSet<>();
        for(OutputInvoiceDetail outputInvoiceDetail: outputInvoiceDetails){
            outputInvoiceDetailResponses.add(convertOutputInvoiceDetailToOutputInvoiceDetailResponse(outputInvoiceDetail));
        }
        return outputInvoiceDetailResponses;
    }
}
