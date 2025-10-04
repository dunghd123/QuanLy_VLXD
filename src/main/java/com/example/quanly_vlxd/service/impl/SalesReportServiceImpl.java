package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.response.*;
import com.example.quanly_vlxd.entity.Customer;
import com.example.quanly_vlxd.entity.OutputInvoice;
import com.example.quanly_vlxd.enums.ReportTypeEnums;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.quanly_vlxd.help.VnProvinceHelper.checkAddress;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OutputInvoiceRepo outputInvoiceRepository;
    private final OutputInvoiceDetailRepo outputInvoiceDetailRepository;
    private final ProductRepo productRepository;
    private final CustomerRepo customerRepository;
    private final EmployeeRepo employeeRepository;
    private final WareHouseRepo warehouseRepository;
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;
    private final SupplierRepo supplierRepository;
    private final PriceHistoryRepo priceHistoryRepo;
    private final ReportRepository reportRepository;


    private SaleReportResponse getRevenueByEmployee(Date start, Date end) {
        List<Object[]> rows = reportRepository.findRevenueByEmployee(start, end);
        List<SalesEmployeeResponse> details = new ArrayList<>();
        for(Object[] row : rows){
            details.add(new SalesEmployeeResponse(
                    ((Number) row[0]).intValue(),
                    row[1].toString(),
                    ((Number) row[2]).doubleValue()
            ));
        }
        BigDecimal totalRevenue = details.stream()
                .map(r -> BigDecimal.valueOf(r.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();

        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }


    private SaleReportResponse getRevenueByProduct(Date start, Date end) {
        List<Object[]> rows = reportRepository.findRevenueByProductNative(start, end);
        List<SalesProductResponse> details = new ArrayList<>();

        for (Object[] row : rows) {
            details.add(new SalesProductResponse(
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    ((Number) row[2]).doubleValue(),
                    ((Number) row[3]).doubleValue()
            ));
        }

        BigDecimal totalRevenue = details.stream()
                .map(r -> BigDecimal.valueOf(r.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();

        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }


    private SaleReportResponse getRevenueByCustomer(Date start, Date end) {
        List<Object[]> rows = reportRepository.findRevenueByCustomer(start, end);
        List<SalesCustomerResponse> details = new ArrayList<>();

        for (Object[] row : rows) {
            details.add(new SalesCustomerResponse(
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    ((Number) row[2]).doubleValue()
            ));
        }

        BigDecimal totalRevenue = details.stream()
                .map(r -> BigDecimal.valueOf(r.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();

        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }
    private SaleReportResponse getRevenueByMonth(int year){
        List<Object[]> rows = reportRepository.findRevenueByMonth(year);
        List<SalesMonthResponse> details = new ArrayList<>();
        for(Object[] row : rows){
            details.add(new SalesMonthResponse(
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).doubleValue())
            );
        }
        BigDecimal totalRevenue = details.stream().map(r -> BigDecimal.valueOf(r.getTotalAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();
        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }
    private SaleReportResponse getRevenueByQuarter(int year){
        List<Object[]> rows = reportRepository.findRevenueByQuarter(year);
        List<SalesQuarterResponse> details = new ArrayList<>();
        for(Object[] row : rows){
            details.add(new SalesQuarterResponse(
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).doubleValue()
            ));
        }
        BigDecimal totalRevenue = details.stream().map(r -> BigDecimal.valueOf(r.getTotalAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();
        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }

    @Override
    public SaleReportResponse getRevenueInDate(Date start, Date end, String type) {
        if(ReportTypeEnums.EMPLOYEE.name().equalsIgnoreCase(type))
            return getRevenueByEmployee(start, end);
        else if(ReportTypeEnums.PRODUCT.name().equalsIgnoreCase(type))
            return getRevenueByProduct(start, end);
        else if(ReportTypeEnums.CUSTOMER.name().equalsIgnoreCase(type))
            return getRevenueByCustomer(start, end);
        else return null;
    }

    @Override
    public SaleReportResponse getRevenueInMonth(int year) {
        return getRevenueByMonth(year);
    }

    @Override
    public SaleReportResponse getRevenueInQuarter(int year) {
        return getRevenueByQuarter(year);
    }

    @Override
    public SaleReportResponse getRegionRevenue(int year) {
        List<SalesRegionResponse> details = new ArrayList<>();
        details.add(revenueByRegion("Miền Bắc", year));
        details.add(revenueByRegion("Miền Trung", year));
        details.add(revenueByRegion("Miền Nam", year));
        BigDecimal totalRevenue = details.stream().map(r -> BigDecimal.valueOf(r.getTotalAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
        SaleReportResponse.Summary summary = SaleReportResponse.Summary.builder()
                .totalRevenue(totalRevenue)
                .recordCount(details.size())
                .build();
        return SaleReportResponse.builder()
                .summary(summary)
                .details(details)
                .build();
    }


    private SalesRegionResponse revenueByRegion(String region, int year){
        double total= 0.0;
        for(OutputInvoice oi: outputInvoiceRepository.findAllInYear(year)){
            if(!oi.getShipAddress().equalsIgnoreCase("") && checkAddress(oi.getShipAddress()).equals(region)){
                total+= oi.getTotalAmount();
            }else if(oi.getShipAddress().equalsIgnoreCase("")){
                Customer cus = customerRepository.findById(oi.getCustomer().getId()).orElseThrow();
                if(checkAddress(cus.getAddress()).equals(region)){
                    total+= oi.getTotalAmount();
                }
            }
        }
        return SalesRegionResponse.builder()
                .region(region)
                .totalAmount(total)
                .build();
    }

//    //chi tiet ban hang
//    @Override
//    public List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request) {
//        List<OutputInvoice> invoices = fetchOutputInvoices(request);
//        List<SalesDetailResponse> salesDetails = new ArrayList<>();
//        List<OutputInvoiceDetail> details;
//        for (OutputInvoice invoice : invoices) {
//            if(!request.getProductIds().isEmpty()) {
//                details=outputInvoiceDetailRepository.findByOutputInvoiceIdAndProductId(invoice.getId(), request.getProductIds());
//                for (OutputInvoiceDetail detail : details) {
//                    SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
//                    salesDetails.add(salesDetail);
//                }
//            }
//            else {
//                details = outputInvoiceDetailRepository.findByOutputInvoiceId(invoice.getId());
//                for (OutputInvoiceDetail detail : details) {
//                    SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
//                    salesDetails.add(salesDetail);
//                }
//            }
//        }
//        return salesDetails;
//    }
//    private SalesDetailResponse mapToSalesDetailResponse(OutputInvoice invoice,OutputInvoiceDetail detail) {
//        Optional<Employee> employee = employeeRepository.findById(invoice.getEmployee().getId());
//        Optional<Customer> customer = customerRepository.findById(invoice.getCustomer().getId());
//        Optional<Product> product = productRepository.findById(detail.getProId());
//        Optional<Warehouse> warehouse = warehouseRepository.findById(detail.getWarehouse().getId());
//        if(employee.isEmpty() || customer.isEmpty() || product.isEmpty() || warehouse.isEmpty()) {
//            return null;
//        }
//        return SalesDetailResponse.builder()
//                .invoiceID(invoice.getId())
//                .invoiceDate(invoice.getCreationTime())
//                .customerID(customer.get().getId())
//                .customerName(customer.get().getName())
//                .employeeID(employee.get().getId())
//                .employeeName(employee.get().getName())
//                .productName(product.get().getName())
//                .productID(detail.getProId())
//                .warehouseName(warehouse.get().getName())
//                .unitMeasure(product.get().getUnitMeasure())
//                .unitPrice(detail.getUnitPrice())
//                .quantitySold(detail.getQuantity())
//                .build();
//    }
//    private SalesInputResponse mapToSalesInputResponse(InputInvoice invoice,InputInvoiceDetail detail) {
//        Optional<Employee> employee = employeeRepository.findById(invoice.getEmployee().getId());
//        Optional<Supplier> supplier = supplierRepository.findById(invoice.getSupplier().getId());
//        Optional<Product> product = productRepository.findById(detail.getProduct().getId());
//        Optional<Warehouse> warehouse = warehouseRepository.findById(detail.getWarehouse().getId());
//        if(employee.isEmpty() || supplier.isEmpty() || product.isEmpty() || warehouse.isEmpty()) {
//            return null;
//        }
//        return SalesInputResponse.builder()
//                .invoiceID(invoice.getId())
//                .invoiceDate(invoice.getCreationTime())
//                .supplierID(supplier.get().getId())
//                .supplierName(supplier.get().getName())
//                .employeeID(employee.get().getId())
//                .employeeName(employee.get().getName())
//                .productName(product.get().getName())
//                .productID(detail.getProduct().getId())
//                .warehouseName(warehouse.get().getName())
//                .unitMeasure(product.get().getUnitMeasure())
//                .unitPrice(detail.getUnitPrice())
//                .quantityBuy(detail.getQuantity())
//                .build();
//    }
//    //doanh thu theo cac tieu chi
//    @Override
//    public SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request) {
//        //xóa trung lap
//        List<Integer> uniqueProductIds = request.getProductIds().stream().distinct().toList();
//        List<Integer> uniqueCustomerIds = request.getCustomerIds().stream().distinct().toList();
//        List<Integer> uniqueEmployeeIds = request.getEmployeeIds().stream().distinct().toList();
//        //tinh total revenue
//        BigDecimal totalRevenue = BigDecimal.ZERO;
//        List<OutputInvoiceDetail> details = new ArrayList<>();
//        List<SalesProductResponse> salesProducts = new ArrayList<>();
//        List<SalesCustomerResponse> salesCustomers = new ArrayList<>();
//        List<SalesEmployeeResponse> salesEmployee = new ArrayList<>();
//        //Loc theo cac tieu chi
//        if(!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) {// Tim theo product
//            return filterByProductOutput(details,uniqueProductIds, request,salesProducts,totalRevenue);
//        }
//        else if(request.getProductIds().isEmpty() && !request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) { // Tim theo customer
//            return filterByCustomer(request,uniqueCustomerIds,salesCustomers,totalRevenue);
//        }else if(request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && !request.getEmployeeIds().isEmpty()) { // Tim theo employee
//            return filterByEmployee(request,uniqueEmployeeIds,salesEmployee,totalRevenue);
//        }else if(request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()){   // Hien thi toan bo
//            return getTotalRevenue(request,totalRevenue);
//        }
//        return null;
//    }
//    //doanh thu tim theo quy
//    @Override
//    public SalesReportResponse generateSalesReportByQuarter(SalesRevenueQuarterRequest request) {
//        List<OutputInvoice> invoices = fetchOutputInvoiceByQuarter(request);
//        BigDecimal totalRevenue = BigDecimal.ZERO;
//        List<OutputInvoiceDetail> details;
//        List<SalesDetailResponse> salesDetails = new ArrayList<>();
//        for(OutputInvoice invoice : invoices) {
//            details=outputInvoiceDetailRepository.findByOutputInvoiceId(invoice.getId());
//            for (OutputInvoiceDetail detail : details) {
//                SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
//                salesDetails.add(salesDetail);
//            }
//        }
//        for(OutputInvoice invoice : invoices) {
//            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesDetails)
//                .build();
//    }
//    //doanh thu theo quy
//    @Override
//    public List<SalesQuarterResponse> allQuarterReport(int year) {
//        List<SalesQuarterResponse> salesQuarterRespons = new ArrayList<>();
//        for(int i = 1; i <= 4; i++) {
//            BigDecimal total = BigDecimal.ZERO;
//            List<OutputInvoice> invoices =outputInvoiceRepository.findByCreationTimeBetween(getDate(year, i, "start"), getDate(year, i, "end"));
//            for(OutputInvoice invoice : invoices) {
//                total = total.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//            }
//            salesQuarterRespons.add(SalesQuarterResponse.builder()
//                    .quarter(i)
//                    .total(total)
//                    .build());
//        }
//        return salesQuarterRespons;
//    }
//    //doanh thu theo thang
//    @Override
//    public List<SalesMonthResponse> salesRevenueByMonth(int year) {
//        List<SalesMonthResponse> salesMonthResponses = new ArrayList<>();
//
//        for(int i = 1; i <= 12; i++) {
//            BigDecimal total = BigDecimal.ZERO;
//            for(OutputInvoice invoice : outputInvoiceRepository.findAll()) {
//                if(getMonth(invoice.getCreationTime())==i && getYear(invoice.getCreationTime())==year) {
//                    total = total.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//                }
//            }
//            salesMonthResponses.add(SalesMonthResponse.builder()
//                    .month(i)
//                    .total(total)
//                    .build());
//        }
//        return salesMonthResponses;
//    }
//    //doanh thu theo san pham (2)
//    @Override
//    public SalesRevenueProductResponse salesRevenueProduct(int proId) {
//        DecimalFormat format= new DecimalFormat("#,###");
//        Optional<Product> product = productRepository.findById(proId);
//        if(product.isEmpty()) return null;
//        double quantityBuy = inputInvoiceDetailRepo.totalQuantityInputInvoice(proId) == null ? 0 : inputInvoiceDetailRepo.totalQuantityInputInvoice(proId);
//        double quantitySold = outputInvoiceDetailRepository.totalQuantityOutputInvoice(proId) == null ? 0 : outputInvoiceDetailRepository.totalQuantityOutputInvoice(proId);
//        double inputPrice= findInputPrice(proId);
//        double outputPrice = findOutputPrice(proId);
//        double revenue = quantitySold*outputPrice-quantityBuy*inputPrice;
//        return SalesRevenueProductResponse.builder()
//                .proId(proId)
//                .proName(product.get().getName())
//                .inputTotal(format.format(quantityBuy*inputPrice)+" VND")
//                .outputTotal(format.format(quantitySold*outputPrice)+ " VND")
//                .revenue(format.format(revenue)+" VND")
//                .build();
//    }
//    public double findOutputPrice(int proId) {
//        for(ProductPriceHistory priceHistory: priceHistoryRepo.findAll()) {
//            if (priceHistory.getProduct().getId() == proId
//                    && priceHistory.getInvoiceType().equals(InvoiceTypeEnums.OUTPUT)) {
//                if (priceHistory.getEndDate() == null) {
//                    return priceHistory.getPrice();
//                }
//            }
//        }
//        return 0;
//    }
//    public double findInputPrice(int proId) {
//        for(ProductPriceHistory priceHistory: priceHistoryRepo.findAll()) {
//            if (priceHistory.getProduct().getId() == proId
//                    && priceHistory.getInvoiceType().equals(InvoiceTypeEnums.INPUT)) {
//                if (priceHistory.getEndDate() == null) {
//                    return priceHistory.getPrice();
//                }
//            }
//        }
//        return 0;
//    }
//
//    //Tim kiem theo khu vuc
//    @Override
//    public SalesReportResponse salesRevenueByRegion(SalesRevenueByRegionRequest request) {
//        List<SalesRegionResponse> result= new ArrayList<>();
//        if(request.getRegion().isEmpty()){
//            result.add(doanhThuTheoMien("Miền Bắc"));
//            result.add(doanhThuTheoMien("Miền Trung"));
//            result.add(doanhThuTheoMien("Miền Nam"));
//        }
//        if(request.getRegion().size()==1){
//            result.add(doanhThuTheoMien(request.getRegion().get(0)));
//        }
//        if(request.getRegion().size()==2){
//            for(int i=0;i<2;i++){
//                result.add(doanhThuTheoMien(request.getRegion().get(i)));
//            }
//        }
//        BigDecimal totalRevenue= BigDecimal.ZERO;
//        for(SalesRegionResponse sr : result){
//            totalRevenue= totalRevenue.add(sr.getTotalRevenue());
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(result)
//                .build();
//    }
//    //Tổng tiền nhập hàng theo sản phẩm hoặc theo nhà cung cấp và theo thời gian
//    @Override
//    public SalesReportResponse getTotalAmountInputInvoice(InputInvoiceReportRequest request) {
//        //xóa trung lap
//        List<Integer> uniqueProductIds = request.getProductIds().stream().distinct().toList();
//        List<Integer> uniqueSupplierIds = request.getSupplierIds().stream().distinct().toList();
//
//        //tinh total amount
//        BigDecimal totalRevenue = BigDecimal.ZERO;
//        List<SalesProductResponse> salesProducts = new ArrayList<>();
//        List<SupplierDetailResonse> salesCustomers = new ArrayList<>();
//        //Loc theo cac tieu chi
//        if(!request.getProductIds().isEmpty() && request.getSupplierIds().isEmpty()) {// Tim theo product
//            return filterByProductInput(uniqueProductIds, request,salesProducts,totalRevenue);
//        }
//        else if(request.getProductIds().isEmpty() && !request.getSupplierIds().isEmpty()) { // Tim theo supplier
//            return filterBySupplier(request,uniqueSupplierIds,salesCustomers,totalRevenue);
//        }else {
//            return  getTotalAmountByMonth(request, totalRevenue);
//        }
//    }
//    //Tổng tiền nhập hàng theo quý
//    @Override
//    public SalesReportResponse getTotalAmountByQuater(SalesRevenueQuarterRequest request) {
//        List<InputInvoice> invoices = fetchInputInvoiceByQuarter(request);
//        BigDecimal totalRevenue = BigDecimal.ZERO;
//        List<InputInvoiceDetail> details;
//        List<SalesInputResponse> salesInputResponseList = new ArrayList<>();
//        for(InputInvoice invoice : invoices) {
//            details=inputInvoiceDetailRepo.findByInputInvoiceId(invoice.getId());
//            for (InputInvoiceDetail detail : details) {
//                SalesInputResponse salesInputResponse = mapToSalesInputResponse(invoice, detail);
//                salesInputResponseList.add(salesInputResponse);
//            }
//        }
//        for(InputInvoice invoice : invoices) {
//            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesInputResponseList)
//                .build();
//    }
//
//    private SalesRegionResponse doanhThuTheoMien(String region){
//        BigDecimal total= BigDecimal.ZERO;
//        for(OutputInvoice oi: outputInvoiceRepository.findAll()){
//            if(checkAddress(oi.getShipAddress()).equals(region)){
//                total= total.add(BigDecimal.valueOf(oi.getTotalAmount()));
//            }
//        }
//        return SalesRegionResponse.builder()
//                .region(region)
//                .totalRevenue(total)
//                .build();
//    }
//    // tong tien nhap theo thang
//    private SalesReportResponse getTotalAmountByMonth(InputInvoiceReportRequest request, BigDecimal totalRevenue) {
//        List<InputInvoice> invoices = fetchInputInvoices(request);
//        for(InputInvoice invoice : invoices) {
//            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .build();
//    }
//    //Tong doanh thu trong khoang thoi gian
//    private SalesReportResponse getTotalRevenue(SalesDetailReportRequest request, BigDecimal totalRevenue) {
//        List<OutputInvoice> invoices = fetchOutputInvoices(request);
//        for(OutputInvoice invoice : invoices) {
//            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .build();
//    }
//    //doanh thu theo nhan vien
//    private SalesReportResponse filterByEmployee(
//            SalesDetailReportRequest request,
//            List<Integer> uniqueEmployeeIds,
//            List<SalesEmployeeResponse> salesEmployee,
//            BigDecimal totalRevenue) {
//        List<OutputInvoice> invoices = fetchOutputInvoices(request);
//        DecimalFormat format= new DecimalFormat("#,###");
//        for(Integer i : uniqueEmployeeIds) {
//            Optional<Employee> employee = employeeRepository.findById(i);
//            if (employee.isEmpty()) {
//                return null;
//            }
//            for(OutputInvoice invoice : invoices) {
//                if(i.equals(invoice.getEmployee().getId())) {
//                    salesEmployee.add(SalesEmployeeResponse.builder()
//                            .empId(employee.get().getId())
//                            .empName(employee.get().getName())
//                            .outputInvoiceId(invoice.getId())
//                            .total(format.format(invoice.getTotalAmount())+ " VND")
//                            .build());
//                }
//            }
//            for(OutputInvoice invoice : invoices) {
//                totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//            }
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesEmployee)
//                .build();
//    }
//    //doanh thu theo khach hang
//    private SalesReportResponse filterByCustomer(
//            SalesDetailReportRequest request,
//            List<Integer> uniqueCustomerIds,
//            List<SalesCustomerResponse> salesCustomers,
//            BigDecimal totalRevenue) {
//        List<OutputInvoice> invoices = fetchOutputInvoices(request);
//        DecimalFormat format= new DecimalFormat("#,###");
//        for(Integer i : uniqueCustomerIds) {
//            Optional<Customer> customer = customerRepository.findById(i);
//            if(customer.isEmpty()) {
//                return null;
//            }
//            for(OutputInvoice invoice : invoices) {
//                if(i.equals(invoice.getCustomer().getId())) {
//                    salesCustomers.add(SalesCustomerResponse.builder()
//                            .cusId(customer.get().getId())
//                            .cusName(customer.get().getName())
//                            .outputInvoiceId(invoice.getId())
//                            .total(format.format(BigDecimal.valueOf(invoice.getTotalAmount()))+ " VND")
//                            .build());
//                }
//            }
//            for(OutputInvoice invoice : invoices) {
//                totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//            }
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesCustomers)
//                .build();
//    }
//    // tong tien theo nha cung cap
//    private SalesReportResponse filterBySupplier(
//            InputInvoiceReportRequest request,
//            List<Integer> uniqueSupplierIds,
//            List<SupplierDetailResonse> supplierResponse,
//            BigDecimal totalRevenue) {
//        List<InputInvoice> invoices = fetchInputInvoices(request);
//        for(Integer i : uniqueSupplierIds) {
//            Optional<Supplier> supplier = supplierRepository.findById(i);
//            if(supplier.isEmpty()) {
//                return null;
//            }
//            for(InputInvoice invoice : invoices) {
//                if(i.equals(invoice.getSupplier().getId())) {
//                    supplierResponse.add(SupplierDetailResonse.builder()
//                            .suppId(supplier.get().getId())
//                            .suppName(supplier.get().getName())
//                            .inputInvoiceId(invoice.getId())
//                            .total(String.format("%.0f", invoice.getTotalAmount())+" VND")
//                            .build());
//                }
//            }
//            for(InputInvoice invoice : invoices) {
//                totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
//            }
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(supplierResponse)
//                .build();
//    }
//    //doanh thu theo san pham
//    private SalesReportResponse filterByProductOutput(
//            List<OutputInvoiceDetail> details,
//            List<Integer> uniqueProductIds,
//            SalesDetailReportRequest request,
//            List<SalesProductResponse> salesProducts,
//            BigDecimal totalRevenue) {
//        details=outputInvoiceDetailRepository.filterByProduct(request.getProductIds());
//        for(Integer i : uniqueProductIds) {
//            Optional<Product> product = productRepository.findById(i);
//            if(product.isEmpty()) {
//                return null;
//            }
//            double quantity = 0.0;
//            BigDecimal total= BigDecimal.ZERO;
//            SalesProductResponse salesProduct  = SalesProductResponse.builder()
//                    .proId(product.get().getId())
//                    .proName(product.get().getName())
//                    .quantity(0)
//                    .total(BigDecimal.ZERO)
//                    .build();
//            for(OutputInvoiceDetail detail : details) {
//                if(i.equals(detail.getProId())) {
//                    quantity += detail.getQuantity();
//                    total =total.add(BigDecimal.valueOf(detail.getAmount()));
//                    salesProduct.setQuantity(quantity);
//                    salesProduct.setTotal(total);
//                }
//            }
//            salesProducts.add(salesProduct);
//        }
//        for(SalesProductResponse salesProduct : salesProducts) {
//            totalRevenue = totalRevenue.add(salesProduct.getTotal());
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesProducts)
//                .build();
//    }
//    //tong tien nhap theo san pham
//    private SalesReportResponse filterByProductInput(
//            List<Integer> uniqueProductIds,
//            InputInvoiceReportRequest request,
//            List<SalesProductResponse> salesProducts,
//            BigDecimal totalRevenue) {
//        List<InputInvoice> invoices = fetchInputInvoices(request);
//        for(Integer i : uniqueProductIds) {
//            Optional<Product> product = productRepository.findById(i);
//            if(product.isEmpty()) {
//                return null;
//            }
//            double quantity = 0.0;
//            double total=0.0;
//            SalesProductResponse salesProduct = SalesProductResponse.builder()
//                    .proId(product.get().getId())
//                    .proName(product.get().getName())
//                    .quantity(0)
//                    .total(BigDecimal.ZERO)
//                    .build();
//            List<Integer> inputInvoiceIds = new ArrayList<>();
//            for (InputInvoice invoice : invoices) {
//                inputInvoiceIds.add(invoice.getId());
//            }
//            List<InputInvoiceDetail> details=inputInvoiceDetailRepo.findByProductIDAndInputInvoiceID(inputInvoiceIds,i);
//            for(InputInvoiceDetail detail : details) {
//                if(i.equals(detail.getProduct().getId())) {
//                    quantity += detail.getQuantity();
//                    total += detail.getAmount();
//                    salesProduct.setQuantity(quantity);
//                    salesProduct.setTotal(BigDecimal.valueOf(total));
//                }
//            }
//            salesProducts.add(salesProduct);
//        }
//        for(SalesProductResponse salesProduct : salesProducts) {
//            totalRevenue = totalRevenue.add(salesProduct.getTotal());
//        }
//        return SalesReportResponse.builder()
//                .totalRevenue(totalRevenue)
//                .SalesDetails(salesProducts)
//                .build();
//    }
//    //doanh thu theo quy
//    private List<OutputInvoice> fetchOutputInvoiceByQuarter(SalesRevenueQuarterRequest request) {
//        List<OutputInvoice> invoices=new ArrayList<>();
//        switch (request.getQuarter()) {
//            case 1:{
//                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 1, "start"), getDate(request.getYear(), 1, "end"));
//                break;
//            }
//            case 2:{
//                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 2, "start"), getDate(request.getYear(),2, "end"));
//                break;
//            }
//            case 3:{
//                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 3, "start"), getDate(request.getYear(), 3, "end"));
//                break;
//            }
//            case 4:{
//                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 4, "start"), getDate(request.getYear(), 4, "end"));
//                break;
//            }
//        }
//        return invoices;
//    }
//    //tong tien nhap theo quy
//    private List<InputInvoice> fetchInputInvoiceByQuarter(SalesRevenueQuarterRequest request) {
//        List<InputInvoice> invoices=new ArrayList<>();
//        switch (request.getQuarter()) {
//            case 1:{
//                invoices=inputInvoiceRepo.findByCreationTimeBetween(getDate(request.getYear(), 1, "start"), getDate(request.getYear(), 1, "end"));
//                break;
//            }
//            case 2:{
//                invoices=inputInvoiceRepo.findByCreationTimeBetween(getDate(request.getYear(), 2, "start"), getDate(request.getYear(),2, "end"));
//                break;
//            }
//            case 3:{
//                invoices=inputInvoiceRepo.findByCreationTimeBetween(getDate(request.getYear(), 3, "start"), getDate(request.getYear(), 3, "end"));
//                break;
//            }
//            case 4:{
//                invoices=inputInvoiceRepo.findByCreationTimeBetween(getDate(request.getYear(), 4, "start"), getDate(request.getYear(), 4, "end"));
//                break;
//            }
//        }
//        return invoices;
//    }
//    //Doanh thu cac tieu chi
//    private List<OutputInvoice> fetchOutputInvoices(SalesDetailReportRequest request) {
//        List<OutputInvoice> invoices;
//
//        if (!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) {
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIds(
//                    request.getStartDate(), request.getEndDate(), request.getProductIds());
//        } else if (!request.getCustomerIds().isEmpty() && request.getProductIds().isEmpty() && request.getEmployeeIds().isEmpty()) {
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndCustomerIds(request.getStartDate(), request.getEndDate(), request.getCustomerIds());
//        } else if (!request.getEmployeeIds().isEmpty() && request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()) {
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndEmployeeIds(
//                    request.getStartDate(), request.getEndDate(), request.getEmployeeIds());
//        } else if (!request.getProductIds().isEmpty() && !request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) { // Tim theo product va customer =>done
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndCustomerIds(
//                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getCustomerIds());
//        } else if (!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()) { // Tim theo product va employee =>done
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndEmployeeIds(
//                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getEmployeeIds());
//        } else if (!request.getCustomerIds().isEmpty()  && request.getProductIds().isEmpty()) { // Tim theo customer va employee =>done
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndCustomerIdsAndEmployeeIds(
//                    request.getStartDate(), request.getEndDate(), request.getCustomerIds(), request.getEmployeeIds());
//        } else if (request.getCustomerIds().isEmpty()) {// Hien thi toan bo =>done
//            invoices = outputInvoiceRepository.findByCreationTimeBetween(request.getStartDate(), request.getEndDate());
//        } else{                                                                            // Tim theo product, customer, employee =>done
//            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndCustomerIdsAndEmployeeIds(
//                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getCustomerIds(), request.getEmployeeIds());
//        }
//        return invoices;
//    }
//    //tong tien nhap theo cac tieu chi
//    private List<InputInvoice> fetchInputInvoices(InputInvoiceReportRequest request) {
//        if (!request.getProductIds().isEmpty() && request.getSupplierIds().isEmpty()) { // loc theo san pham
//            return inputInvoiceRepo.findByCreationTimeBetweenAndProductIds(
//                    request.getStartDate(), request.getEndDate(), request.getProductIds());
//        }else if(request.getProductIds().isEmpty() && !request.getSupplierIds().isEmpty()){ //loc theo nha cung cap
//            return inputInvoiceRepo.findByCreationTimeBetweenAndSupplierIds(
//                    request.getStartDate(), request.getEndDate(), request.getSupplierIds());
//        }else {
//            return inputInvoiceRepo.findByCreationTimeBetween(request.getStartDate(), request.getEndDate());
//        }
//    }
//    // Xuat file PDF doanh thu theo quy
//   @Override
//    public void generateReportQuaterToPdf(int year) throws Exception {
//       DecimalFormat format= new DecimalFormat("#,###");
//        // Tạo một đối tượng Document
//        Document document = new Document();
//
//        // Tạo một đối tượng PdfWriter
//        PdfWriter.getInstance(document, new FileOutputStream("sales_quarter_report.pdf"));
//
//        // Mở tài liệu
//        document.open();
//
//       BaseFont fontBase = BaseFont.createFont("C:/Windows/Fonts/Tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//       Font font1 = new Font(fontBase, 18, Font.BOLD);
//       Font font2= new Font(fontBase,13);
//        Chunk chunk = new Chunk("Báo cáo doanh thu theo quý"+ " năm "+year, font1);
//        Paragraph title = new Paragraph(chunk);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//
//        // Thêm bảng biểu
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10);
//        table.setSpacingAfter(10);
//
//        PdfPCell cell = new PdfPCell(new Phrase("Quý", font1));
//        cell.setPadding(5);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setBackgroundColor(BaseColor.WHITE);
//        cell.setBorderWidth(1);
//        cell.setBorderColor(BaseColor.BLACK);
//        table.addCell(cell);
//
//        cell = new PdfPCell(new Phrase("Doanh thu", font1));
//        cell.setPadding(5);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setBackgroundColor(BaseColor.WHITE);
//        cell.setBorderWidth(1);
//        cell.setBorderColor(BaseColor.BLACK);
//        table.addCell(cell);
//
//        List<SalesQuarterResponse> salesQuarterRespons = allQuarterReport(year);
//        for (SalesQuarterResponse response : salesQuarterRespons) {
//            cell = new PdfPCell(new Phrase(String.valueOf(response.getQuarter()), font2));
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase(format.format(response.getTotal()) + " VND",font2));
//            table.addCell(cell);
//        }
//        BigDecimal total = allQuarterReport(year).stream().map(SalesQuarterResponse::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
//        cell = new PdfPCell(new Phrase("Tổng doanh thu", font2));
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase(format.format(total)+ " VND",font2));
//        table.addCell(cell);
//
//        document.add(table);
//
//        // Đóng tài liệu
//        document.close();
//    }
//    // Xuat file PDF doanh thu theo khu vuc
//    @Override
//    public void generateReportRegionToPdf() throws Exception {
//        DecimalFormat format= new DecimalFormat("#,###");
//        // Tạo một đối tượng Document
//        Document document = new Document();
//
//        // Tạo một đối tượng PdfWriter
//        PdfWriter.getInstance(document, new FileOutputStream("sales_region_report.pdf"));
//
//        // Mở tài liệu
//        document.open();
//
//        BaseFont fontBase = BaseFont.createFont("C:/Windows/Fonts/Tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        Font font1 = new Font(fontBase, 18, Font.BOLD);
//        Font font2= new Font(fontBase,13);
//        Chunk chunk = new Chunk("Báo cáo doanh thu theo khu vực", font1);
//        Paragraph title = new Paragraph(chunk);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//
//        // Thêm bảng biểu
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10);
//        table.setSpacingAfter(10);
//
//        PdfPCell cell = new PdfPCell(new Phrase("Khu Vực", font1));
//        cell.setPadding(5);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setBackgroundColor(BaseColor.WHITE);
//        cell.setBorderWidth(1);
//        cell.setBorderColor(BaseColor.BLACK);
//        table.addCell(cell);
//
//        cell = new PdfPCell(new Phrase("Doanh thu", font1));
//        cell.setPadding(5);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setBackgroundColor(BaseColor.WHITE);
//        cell.setBorderWidth(1);
//        cell.setBorderColor(BaseColor.BLACK);
//        table.addCell(cell);
//
//        List<SalesRegionResponse> salesRegionResponses= new ArrayList<>();
//        salesRegionResponses.add(doanhThuTheoMien("Miền Bắc"));
//        salesRegionResponses.add(doanhThuTheoMien("Miền Trung"));
//        salesRegionResponses.add(doanhThuTheoMien("Miền Nam"));
//        for (SalesRegionResponse response : salesRegionResponses) {
//            cell = new PdfPCell(new Phrase(response.getRegion(),font2));
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase(format.format(response.getTotalRevenue()) + " VND"));
//            table.addCell(cell);
//        }
//        BigDecimal total= salesRegionResponses.stream().map(SalesRegionResponse::getTotalRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
//        cell = new PdfPCell(new Phrase("Tổng",font2));
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase(format.format(total) + " VND",font2));
//        table.addCell(cell);
//
//        document.add(table);
//        // Đóng tài liệu
//        document.close();
//    }
}