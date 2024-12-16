package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.InputInvoiceDetailRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceDetailResponse;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.InputInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InputInvoiceServiceImpl implements InputInvoiceService  {
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;
    private final SupplierRepo supplierRepo;
    private final WareHouseRepo wareHouseRepo;
    private final ProductRepo ProductRepo;
    private final PriceHistoryRepo priceHistoryRepo;
    private final EmployeeRepo employeeRepo;
    private final WarehouseProductRepo warehouseProductRepo;
    // Lấy ra giá nhập theo ID sp
    public double  getInputPriceByProductID(int proid, Date creationTime){
        for(ProductPriceHistory priceHistory: priceHistoryRepo.findAll()) {
            if (priceHistory.getProduct().getId() == proid
                && priceHistory.getInvoiceType().equals(InvoiceTypeEnums.INPUT)) {
                if (priceHistory.getEndDate() == null || priceHistory.getEndDate().after(creationTime)) {
                    return priceHistory.getPrice();
                }
            }
        }
        return 0;
    }
    public void updateUnitPrice(){
        for(InputInvoice inputInvoice: inputInvoiceRepo.findAll()){
            for(InputInvoiceDetail iid: inputInvoiceDetailRepo.findAll()){
                if(iid.getInputInvoice().getId() == inputInvoice.getId()){
                    iid.setUnitPrice(getInputPriceByProductID(iid.getProduct().getId(), inputInvoice.getCreationTime()));
                    inputInvoiceDetailRepo.save(iid);
                }
            }
        }
        for(InputInvoiceDetail iid: inputInvoiceDetailRepo.findAll()){
            iid.setAmount(iid.getQuantity() * iid.getUnitPrice());
            inputInvoiceDetailRepo.save(iid);
        }
        for(InputInvoice inputInvoice: inputInvoiceRepo.findAll()){
            double total=0.0;
            for(InputInvoiceDetail iid: inputInvoiceDetailRepo.findAll()){
                if(iid.getInputInvoice().getId() == inputInvoice.getId()){
                    total += iid.getAmount();
                }
            }
            inputInvoice.setTotalAmount(total);
            inputInvoiceRepo.save(inputInvoice);
        }
    }
    @Override
    public MessageResponse addInputInvoice(InputInvoiceRequest inputInvoiceRequest) {
        Optional<Supplier> checkSup = supplierRepo.findById(inputInvoiceRequest.getSupID());
        if (checkSup.isEmpty())
            return MessageResponse.builder().message("Supplier ID: " + inputInvoiceRequest.getSupID() + " is not exist").build();
        Optional<Employee> checkEmp = employeeRepo.findById(inputInvoiceRequest.getEmpID());
        if (checkEmp.isEmpty())
            return MessageResponse.builder().message("Employee ID: " + inputInvoiceRequest.getEmpID() + " is not exist").build();
        // Lưu thông tin hóa đơn nhập
        InputInvoice inputInvoice = InputInvoice.builder()
                .supplier(checkSup.get())
                .employee(checkEmp.get())
                .Status(false)
                .CreationTime(inputInvoiceRequest.getCreationTime())
                .UpdateTime(inputInvoiceRequest.getCreationTime())
                .TotalAmount(0)
                .IsActive(true)
                .build();
        InputInvoice saveInputInvoice =  inputInvoiceRepo.save(inputInvoice);

        // convert inputInvoiceDetailRequest thành InputInvoiceDetail
        List<InputInvoiceDetail> lst= new ArrayList<>();
        for(InputInvoiceDetailRequest inputInvoiceDetailRequest: inputInvoiceRequest.getListInvoiceDetails()){
             Optional<Product> checkProduct = ProductRepo.findById(inputInvoiceDetailRequest.getPro_id());
            if (checkProduct.isEmpty())
                return MessageResponse.builder().message("Product ID: " + inputInvoiceDetailRequest.getPro_id() + " is not exist").build();
            Optional<Warehouse> checkWH = wareHouseRepo.findById(inputInvoiceDetailRequest.getWh_id());
            if (checkWH.isEmpty())
                return MessageResponse.builder().message("Warehouse ID: " + inputInvoiceDetailRequest.getWh_id() + " is not exist").build();

            double quantity= inputInvoiceDetailRequest.getQuantity();
            double price = getInputPriceByProductID(inputInvoiceDetailRequest.getPro_id(), inputInvoiceRequest.getCreationTime());
            InputInvoiceDetail inputInvoiceDetail = InputInvoiceDetail.builder()
                    .inputInvoice(saveInputInvoice)
                    .product(checkProduct.get())
                    .warehouse(checkWH.get())
                    .Quantity(quantity)
                    .UnitPrice(price)
                    .Amount(quantity * price)
                    .build();
            lst.add(inputInvoiceDetail);
            // Cap nhat so luong vao kho
            Optional<WareHouse_Product> warehouseProduct = warehouseProductRepo.findByWarehouseAndProduct(checkWH.get().getId(), checkProduct.get().getId());
            if (warehouseProduct.isPresent()) {
                warehouseProduct.get().setQuantity(warehouseProduct.get().getQuantity() + quantity);
                warehouseProduct.get().setLastUpdated(inputInvoiceRequest.getCreationTime());
                warehouseProductRepo.save(warehouseProduct.get());
            }
        }
        inputInvoiceDetailRepo.saveAll(lst);

        // Tinh tong tien
        double total=0.0;
        for(InputInvoiceDetail iid: lst){
            total += iid.getAmount();
        }
        saveInputInvoice.setTotalAmount(total);
        inputInvoiceRepo.save(saveInputInvoice);
        return MessageResponse.builder().message("Add input invoice successfully").build();
}

    @Override
    public MessageResponse updateInputInvoice(int id) {
        Optional<InputInvoice> inputInvoice = inputInvoiceRepo.findById(id);
        if(inputInvoice.isEmpty())
            return MessageResponse.builder().message("Input invoice does not exist!!").build();
        inputInvoice.get().setUpdateTime(new Date());
        inputInvoice.get().setStatus(true);
        inputInvoiceRepo.save(inputInvoice.get());
        return MessageResponse.builder().message("the input invoice has been completed").build();
    }

    @Override
    public MessageResponse deleteInputInvoice(int id) {
        Optional<InputInvoice> inputInvoice = inputInvoiceRepo.findById(id);
        if(inputInvoice.isEmpty())
            return MessageResponse.builder().message("Input invoice does not exist!!").build();
        for(InputInvoiceDetail iid: inputInvoice.get().getInputInvoiceDetails()){
            Optional<WareHouse_Product> warehouseProduct = warehouseProductRepo.findByWarehouseAndProduct(iid.getWarehouse().getId(), iid.getProduct().getId());
            if (warehouseProduct.isPresent()) {
                warehouseProduct.get().setQuantity(warehouseProduct.get().getQuantity() - iid.getQuantity());
                warehouseProduct.get().setLastUpdated(new Date());
                warehouseProductRepo.save(warehouseProduct.get());
            }
            inputInvoiceDetailRepo.delete(iid);
        }
        inputInvoice.get().setIsActive(false);
        inputInvoiceRepo.save(inputInvoice.get());
        return MessageResponse.builder().message("Delete input invoice successfully").build();
    }

    @Override
    public InputInvoiceResponse getInputInvoice(int id) {
        Optional<InputInvoice> inputInvoice = inputInvoiceRepo.findByInputID(id);
        return inputInvoice.map(this::convertToInputInvoiceResponse).orElse(null);
    }
    // convert InputInvoice -> InputInvoiceResponse
    private InputInvoiceResponse convertToInputInvoiceResponse(InputInvoice inputInvoice){
        return InputInvoiceResponse.builder()
                .id(inputInvoice.getId())
                .supName(inputInvoice.getSupplier().getName())
                .empName(inputInvoice.getEmployee().getName())
                .creationTime(inputInvoice.getCreationTime())
                .updateTime(inputInvoice.getUpdateTime())
                .listInvoiceDetails(convertToSetInputInvoiceDetailResponse(inputInvoice.getInputInvoiceDetails()))
                .totalAmount(inputInvoice.getTotalAmount())
                .build();
    }
    // convert InputInvoiceDetail -> InputInvoiceDetailResponse
    private InputInvoiceDetailResponse convertToInputInvoiceDetailResponse(InputInvoiceDetail inputInvoiceDetail){
        return InputInvoiceDetailResponse.builder()
                .id(inputInvoiceDetail.getId())
                .productName(inputInvoiceDetail.getProduct().getName())
                .unitMeasure(inputInvoiceDetail.getProduct().getUnitMeasure())
                .warehouseName(inputInvoiceDetail.getWarehouse().getName())
                .quantity(inputInvoiceDetail.getQuantity())
                .price(inputInvoiceDetail.getUnitPrice())
                .amount(inputInvoiceDetail.getAmount())
                .build();
    }
    // convert Set<InputInvoiceDetail> -> Set<InputInvoiceDetailResponse>
    private Set<InputInvoiceDetailResponse> convertToSetInputInvoiceDetailResponse(Set<InputInvoiceDetail> inputInvoiceDetails){
        Set<InputInvoiceDetailResponse> lst= new HashSet<>();
        for(InputInvoiceDetail inputInvoiceDetail: inputInvoiceDetails){
            lst.add(convertToInputInvoiceDetailResponse(inputInvoiceDetail));
        }
        return lst;
    }
}
