package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.InputFilterRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceDetailRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceDetailResponse;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.InputInvoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InputInvoiceServiceImpl implements InputInvoiceService  {
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;
    private final SupplierRepo supplierRepo;
    private final WareHouseRepo wareHouseRepo;
    private final ProductRepo productRepo;
    private final PriceHistoryRepo priceHistoryRepo;
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
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
    @Transactional
    @Override
    public ResponseEntity<MessageResponse> addInputInvoice(InputInvoiceRequest inputInvoiceRequest) {
        try {
            validateInputInvoiceRequest(inputInvoiceRequest);
            Supplier supplier = supplierRepo.findById(inputInvoiceRequest.getSupId()).get();
            Employee employee = employeeRepo.findById(inputInvoiceRequest.getEmpId()).get();

            InputInvoice inputInvoice = InputInvoice.builder()
                    .supplier(supplier)
                    .employee(employee)
                    .status(InvoiceStatusEnums.PENDING)
                    .creationTime(inputInvoiceRequest.getCreationTime())
                    .updateTime(inputInvoiceRequest.getUpdateTime())
                    .totalAmount(0)
                    .isActive(true)
                    .build();
            InputInvoice saveInputInvoice = inputInvoiceRepo.save(inputInvoice);
            List<InputInvoiceDetail> details = new ArrayList<>();
            double total = 0.0;

            for (InputInvoiceDetailRequest d : inputInvoiceRequest.getListInvoiceDetails()) {
                Product product = productRepo.findById(d.getProId()).get();
                Warehouse warehouse = wareHouseRepo.findById(d.getWhId()).get();

                double quantity = d.getQuantity();
                double price = getInputPriceByProductID(d.getProId(), inputInvoiceRequest.getCreationTime());

                InputInvoiceDetail detail = InputInvoiceDetail.builder()
                        .inputInvoice(saveInputInvoice)
                        .product(product)
                        .warehouse(warehouse)
                        .quantity(quantity)
                        .unitPrice(price)
                        .amount(quantity * price)
                        .build();
                details.add(detail);
                total += detail.getAmount();
            }
            inputInvoiceDetailRepo.saveAll(details);

            saveInputInvoice.setTotalAmount(total);
            inputInvoiceRepo.save(saveInputInvoice);

            return ResponseEntity.ok(MessageResponse.builder().message("Add input invoice successfully").build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder().message(ex.getMessage()).build());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> ApproveInputInvoice(int inputInvoiceId) {
        InputInvoice invoice = inputInvoiceRepo.findById(inputInvoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() != InvoiceStatusEnums.PENDING) {
            throw new RuntimeException("Only pending invoices can be approved");
        }

        for (InputInvoiceDetail detail : invoice.getInputInvoiceDetails()) {
            Warehouse warehouse = detail.getWarehouse();
            Product product = detail.getProduct();

            Optional<WareHouse_Product> whProductOp = warehouseProductRepo
                    .findByWarehouseAndProduct(warehouse.getId(), product.getId());
            if(whProductOp.isEmpty()){
                return ResponseEntity.badRequest().body(MessageResponse.builder().message("Warehouse product not found").build());
            }
            WareHouse_Product whProduct = whProductOp.get();
            whProduct.setQuantity(whProduct.getQuantity() + detail.getQuantity());
            whProduct.setLastUpdated(new Date());
            warehouseProductRepo.save(whProduct);
        }

        invoice.setStatus(InvoiceStatusEnums.APPROVED);
        inputInvoiceRepo.save(invoice);

        return ResponseEntity.ok(MessageResponse.builder().message("Invoice approved successfully").build());
    }

    private void validateInputInvoiceRequest(InputInvoiceRequest request) {
        if (supplierRepo.findById(request.getSupId()).isEmpty()) {
            throw new IllegalArgumentException("Supplier ID " + request.getSupId() + " is not exist");
        }
        if (employeeRepo.findById(request.getEmpId()).isEmpty()) {
            throw new IllegalArgumentException("Employee ID " + request.getEmpId() + " is not exist");
        }

        for (InputInvoiceDetailRequest detail : request.getListInvoiceDetails()) {
            if (productRepo.findById(detail.getProId()).isEmpty()) {
                throw new IllegalArgumentException("Product ID " + detail.getProId() + " is not exist");
            }
            if (wareHouseRepo.findById(detail.getWhId()).isEmpty()) {
                throw new IllegalArgumentException("Warehouse ID " + detail.getWhId() + " is not exist");
            }
            if (detail.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0 for product ID " + detail.getProId());
            }
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateInputInvoice(int id, InputInvoiceRequest inputInvoiceRequest) {
        try {
            validateInputInvoiceRequest(inputInvoiceRequest);
            Supplier supplier = supplierRepo.findById(inputInvoiceRequest.getSupId()).get();
            Employee employee = employeeRepo.findById(inputInvoiceRequest.getEmpId()).get();
            Optional<InputInvoice> inputInvoiceOp = inputInvoiceRepo.findById(id);
            if(inputInvoiceOp.isEmpty()){
                return ResponseEntity.badRequest().body(MessageResponse.builder().message("Input invoice does not exist!!").build());
            }
            InputInvoice inputInvoice = inputInvoiceOp.get();
            inputInvoice.setSupplier(supplier);
            inputInvoice.setEmployee(employee);
            inputInvoice.setUpdateTime(inputInvoiceRequest.getCreationTime());
            inputInvoiceRepo.save(inputInvoice);
            return ResponseEntity.ok(MessageResponse.builder().message("Update input invoice successfully").build());
        }catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message(ex.getMessage()).build());
        }

    }

    @Override
    public ResponseEntity<MessageResponse> deleteInputInvoice(int id) {
        Optional<InputInvoice> inputInvoice = inputInvoiceRepo.findById(id);
        if(inputInvoice.isEmpty())
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Input invoice does not exist!!").build());
        inputInvoice.get().setActive(false);
        inputInvoiceRepo.save(inputInvoice.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Delete input invoice successfully").build());
    }

    @Override
    public InputInvoiceResponse getInputInvoice(int id) {
        Optional<InputInvoice> inputInvoice = inputInvoiceRepo.findByInputID(id);
        return inputInvoice.map(this::convertToInputInvoiceResponse).orElse(null);
    }

    @Override
    public Page<InputInvoiceResponse> getAllInputInvoiceByEmp(InputFilterRequest inputFilterRequest, String username) {
        Optional<User> user = userRepo.findByUserName(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Employee employee = employeeRepo.findByUserID(user.get().getId());
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        Sort sort = Sort.by(Sort.Order.desc("creationTime"));
        Pageable pageable = PageRequest.of(inputFilterRequest.getPageFilter(), inputFilterRequest.getSizeFilter(), sort);
        Specification<InputInvoice> spec = Specification.where(null);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("employee").get("id"), employee.getId()));

        if (inputFilterRequest.getSupNameFilter() != null && !inputFilterRequest.getSupNameFilter().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("supplier").get("name")), "%" + inputFilterRequest.getSupNameFilter().toLowerCase() + "%"));
        }
        if (inputFilterRequest.getStatusFilter() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), inputFilterRequest.getStatusFilter()));
        }

        return inputInvoiceRepo.findAll(spec, pageable)
                .map(this::convertToInputInvoiceResponse);
    }

    private InputInvoiceResponse convertToInputInvoiceResponse(InputInvoice inputInvoice){
        return InputInvoiceResponse.builder()
                .id(inputInvoice.getId())
                .supName(inputInvoice.getSupplier().getName())
                .empName(inputInvoice.getEmployee().getName())
                .creationTime(inputInvoice.getCreationTime())
                .updateTime(inputInvoice.getUpdateTime())
                .status(inputInvoice.getStatus().name())
                .listInvoiceDetails(convertToSetInputInvoiceDetailResponse(inputInvoice.getInputInvoiceDetails()))
                .totalAmount(inputInvoice.getTotalAmount())
                .build();
    }
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
