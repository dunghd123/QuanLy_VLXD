package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.OutputFilterRequest;
import com.example.quanly_vlxd.dto.request.OutputInvoiceDetailRequest;
import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceDetailResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.OutputInvoiceService;
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
import java.util.stream.Collectors;

import static com.example.quanly_vlxd.help.InvoiceHelper.generateCode;

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
    private final UserRepo userRepo;
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
    private void validateOutputInvoiceRequest(OutputInvoiceRequest request) {
        if (customerRepo.findById(request.getCusId()).isEmpty()) {
            throw new IllegalArgumentException("Customer ID " + request.getCusId() + " is not exist");
        }
        if (employeeRepo.findById(request.getEmpId()).isEmpty()) {
            throw new IllegalArgumentException("Employee ID " + request.getEmpId() + " is not exist");
        }

        for (OutputInvoiceDetailRequest detail : request.getListInvoiceDetails()) {
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
    @Transactional
    public ResponseEntity<MessageResponse> addOutputInvoice(OutputInvoiceRequest outputInvoiceRequest) {
        try {
            validateOutputInvoiceRequest(outputInvoiceRequest);
            Customer customer = customerRepo.findById(outputInvoiceRequest.getCusId()).get();
            Employee employee = employeeRepo.findById(outputInvoiceRequest.getEmpId()).get();

            OutputInvoice outputInvoice = OutputInvoice.builder()
                    .customer(customer)
                    .employee(employee)
                    .code(generateCode(InvoiceTypeEnums.OUTPUT))
                    .status(InvoiceStatusEnums.PENDING)
                    .shipAddress(outputInvoiceRequest.getShipAddress())
                    .creationTime(outputInvoiceRequest.getCreationTime())
                    .updateTime(outputInvoiceRequest.getUpdateTime())
                    .totalAmount(0)
                    .isActive(true)
                    .build();
            OutputInvoice saveOutputInvoice = outputInvoiceRepo.save(outputInvoice);
            List<OutputInvoiceDetail> details = new ArrayList<>();
            double total = 0.0;
            for (OutputInvoiceDetailRequest oid : outputInvoiceRequest.getListInvoiceDetails()) {
                Warehouse warehouse = wareHouseRepo.findById(oid.getWhId()).get();

                double quantity = oid.getQuantity();
                double price = getOutputPriceByProductID(oid.getProId(), outputInvoice.getCreationTime());

                OutputInvoiceDetail detail = OutputInvoiceDetail.builder()
                        .outputInvoice(saveOutputInvoice)
                        .proId(oid.getProId())
                        .warehouse(warehouse)
                        .quantity(quantity)
                        .unitPrice(price)
                        .amount(quantity * price)
                        .build();
                details.add(detail);
                total += detail.getAmount();
            }
            outputInvoiceDetailRepo.saveAll(details);

            saveOutputInvoice.setTotalAmount(total);
            outputInvoiceRepo.save(saveOutputInvoice);

            return ResponseEntity.ok(MessageResponse.builder().message("Add output invoice successfully").build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder().message(ex.getMessage()).build());
        }
    }
    private OutputInvoice getOutputInvoice(int id) {
        OutputInvoice invoice = outputInvoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() != InvoiceStatusEnums.PENDING) {
            throw new RuntimeException("Only pending invoices can be approved");
        }
        return invoice;
    }

    @Override
    public ResponseEntity<MessageResponse> approveOutputInvoice(int id) {
        OutputInvoice invoice = getOutputInvoice(id);
        for (OutputInvoiceDetail detail : invoice.getOutputInvoiceDetails()) {
            Warehouse warehouse = detail.getWarehouse();

            Optional<WareHouse_Product> whProductOp = warehouseProductRepo
                    .findByWarehouseAndProduct(warehouse.getId(), detail.getProId());
            if(whProductOp.isEmpty()){
                return ResponseEntity.badRequest().body(MessageResponse.builder().message("Warehouse product not found").build());
            }
            WareHouse_Product whProduct = whProductOp.get();
            whProduct.setQuantity(whProduct.getQuantity() - detail.getQuantity());
            whProduct.setLastUpdated(new Date());
            warehouseProductRepo.save(whProduct);
        }

        invoice.setStatus(InvoiceStatusEnums.APPROVED);
        outputInvoiceRepo.save(invoice);

        return ResponseEntity.ok(MessageResponse.builder().message("Output invoice approved successfully").build());
    }

    @Override
    public ResponseEntity<MessageResponse> completeOutputInvoice(int id) {
        OutputInvoice invoice = getOutputInvoice(id);
        invoice.setStatus(InvoiceStatusEnums.COMPLETED);
        outputInvoiceRepo.save(invoice);
        return ResponseEntity.ok(MessageResponse.builder().message("Output invoice completed successfully").build());
    }

    @Override
    public ResponseEntity<MessageResponse> rejectOutputInvoice(int id) {
        OutputInvoice invoice = getOutputInvoice(id);
        invoice.setStatus(InvoiceStatusEnums.REJECTED);
        outputInvoiceRepo.save(invoice);
        return ResponseEntity.ok(MessageResponse.builder().message("Output invoice rejected successfully").build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateOutputInvoice(int id, OutputInvoiceRequest outputInvoiceRequest) {
        try {
            validateOutputInvoiceRequest(outputInvoiceRequest);
            Customer customer = customerRepo.findById(outputInvoiceRequest.getCusId()).get();
            Employee employee = employeeRepo.findById(outputInvoiceRequest.getEmpId()).get();
            Optional<OutputInvoice> outputInvoiceOp = outputInvoiceRepo.findById(id);
            if(outputInvoiceOp.isEmpty()){
                return ResponseEntity.badRequest().body(MessageResponse.builder().message("Output invoice does not exist!!").build());
            }
            OutputInvoice outputInvoice = outputInvoiceOp.get();
            outputInvoice.setCustomer(customer);
            outputInvoice.setEmployee(employee);
            outputInvoice.setShipAddress(outputInvoiceRequest.getShipAddress());
            outputInvoice.setUpdateTime(outputInvoiceRequest.getUpdateTime());
            OutputInvoice updatedInputInvoice = outputInvoiceRepo.save(outputInvoice);


            Map<Integer, OutputInvoiceDetail> existingDetails = outputInvoice.getOutputInvoiceDetails()
                    .stream()
                    .collect(Collectors.toMap(OutputInvoiceDetail::getId, d -> d));

            double total=0.0;
            for (OutputInvoiceDetailRequest dReq : outputInvoiceRequest.getListInvoiceDetails()) {
                if (dReq.getId() != 0) {
                    OutputInvoiceDetail detail = existingDetails.get(dReq.getId());
                    if (detail != null) {
                        detail.setQuantity(dReq.getQuantity());
                        double price= getOutputPriceByProductID(dReq.getProId(), outputInvoiceRequest.getCreationTime());
                        detail.setUnitPrice(price);
                        detail.setAmount(dReq.getQuantity() * price);
                        outputInvoiceDetailRepo.save(detail);
                        existingDetails.remove(dReq.getId());
                        total+=price*dReq.getQuantity();
                    }
                } else {
                    double price= getOutputPriceByProductID(dReq.getProId(), outputInvoiceRequest.getCreationTime());
                    OutputInvoiceDetail newDetail = OutputInvoiceDetail.builder()
                            .outputInvoice(updatedInputInvoice)
                            .proId(dReq.getProId())
                            .quantity(dReq.getQuantity())
                            .unitPrice(price)
                            .amount(dReq.getQuantity() * price)
                            .warehouse(wareHouseRepo.findById(dReq.getWhId()).orElse(null))
                            .build();
                    outputInvoiceDetailRepo.save(newDetail);
                    total+=price*dReq.getQuantity();

                }
            }
            outputInvoiceDetailRepo.deleteAll(existingDetails.values());
            updatedInputInvoice.setTotalAmount(total);
            outputInvoiceRepo.save(updatedInputInvoice);
            return ResponseEntity.ok(MessageResponse.builder().message("Update output invoice successfully").build());
        }catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message(ex.getMessage()).build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteOutputInvoice(int id) {
        Optional<OutputInvoice> outputInvoice = outputInvoiceRepo.findById(id);
        if(outputInvoice.isEmpty()){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Output invoice ID: " + id + " is not exist").build());
        }
        outputInvoice.get().setActive(false);
        outputInvoiceRepo.save(outputInvoice.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Delete output invoice successfully").build());
    }

    private Employee getEmployeeByUsername(String username) {
        Optional<User> user = userRepo.findByUserName(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Employee employee = employeeRepo.findByUserID(user.get().getId());
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        return employee;
    }
    @Override
    public Page<OutputInvoiceResponse> getAllOutputInvoiceByEmp(OutputFilterRequest outputFilterRequest, String username) {
        Employee employee =  getEmployeeByUsername(username);
        Sort sort = Sort.by(Sort.Order.desc("creationTime"));
        Pageable pageable = PageRequest.of(outputFilterRequest.getPageFilter(), outputFilterRequest.getSizeFilter(), sort);
        Specification<OutputInvoice> spec = Specification.where(null);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("employee").get("id"), employee.getId()));

        if (outputFilterRequest.getCusNameFilter() != null && !outputFilterRequest.getCusNameFilter().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("customer").get("name")), "%" + outputFilterRequest.getCusNameFilter().toLowerCase() + "%"));
        }
        if (outputFilterRequest.getStatusFilter() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), outputFilterRequest.getStatusFilter()));
        }
        return outputInvoiceRepo.findAll(spec,pageable).map(this::convertToOutputInvoiceResponse);
    }

    @Override
    public Page<OutputInvoiceResponse> getAllPendingOutputInvoiceByEmp(int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("creationTime"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<OutputInvoice> spec = Specification.where(null);
        spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"),true));
        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), InvoiceStatusEnums.PENDING));
        return outputInvoiceRepo.findAll(spec, pageable).map(this::convertToOutputInvoiceResponse);
    }

    private OutputInvoiceResponse convertToOutputInvoiceResponse(OutputInvoice outputInvoice){
        return OutputInvoiceResponse.builder()
                .id(outputInvoice.getId())
                .code(outputInvoice.getCode())
                .cusName(outputInvoice.getCustomer().getName())
                .empName(outputInvoice.getEmployee().getName())
                .creationTime(outputInvoice.getCreationTime())
                .updateTime(outputInvoice.getUpdateTime())
                .shipAddress(outputInvoice.getShipAddress())
                .status(outputInvoice.getStatus().name())
                .listOutputInvoiceDetails(convertToSetOutputInvoiceDetailResponse(outputInvoice.getOutputInvoiceDetails()))
                .totalAmount(outputInvoice.getTotalAmount())
                .build();
    }
    public OutputInvoiceDetailResponse convertToOutputInvoiceDetailResponse(OutputInvoiceDetail outputInvoiceDetail){
        Optional<Product> product  = productRepo.findById(outputInvoiceDetail.getProId());
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
            outputInvoiceDetailResponses.add(convertToOutputInvoiceDetailResponse(outputInvoiceDetail));
        }
        return outputInvoiceDetailResponses;
    }
}
