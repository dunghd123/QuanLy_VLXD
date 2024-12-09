package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueByRegionRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuarterRequest;
import com.example.quanly_vlxd.dto.response.*;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.SalesReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.quanly_vlxd.help.DateConvert.getDate;
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

    @Override
    public List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request) {
        List<OutputInvoice> invoices = fetchInvoices(request);
        List<SalesDetailResponse> salesDetails = new ArrayList<>();
        List<OutputInvoiceDetail> details;
        for (OutputInvoice invoice : invoices) {
            if(!request.getProductIds().isEmpty()) {
                details=outputInvoiceDetailRepository.findByOutputInvoiceIdAndProductId(invoice.getId(), request.getProductIds());
                for (OutputInvoiceDetail detail : details) {
                    SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
                    salesDetails.add(salesDetail);
                }
            }
            else {
                details = outputInvoiceDetailRepository.findByOutputInvoiceId(invoice.getId());
                for (OutputInvoiceDetail detail : details) {
                    SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
                    salesDetails.add(salesDetail);
                }
            }
        }
        return salesDetails;
    }
    private SalesDetailResponse mapToSalesDetailResponse(OutputInvoice invoice,OutputInvoiceDetail detail) {
        Optional<Employee> employee = employeeRepository.findById(invoice.getEmployee().getId());
        Optional<Customer> customer = customerRepository.findById(invoice.getCustomer().getId());
        Optional<Product> product = productRepository.findById(detail.getPro_Id());
        Optional<Warehouse> warehouse = warehouseRepository.findById(detail.getWarehouse().getId());
        if(employee.isEmpty() || customer.isEmpty() || product.isEmpty() || warehouse.isEmpty()) {
            return null;
        }
        return SalesDetailResponse.builder()
                .invoiceID(invoice.getId())
                .invoiceDate(invoice.getCreationTime())
                .customerID(customer.get().getId())
                .customerName(customer.get().getName())
                .employeeID(employee.get().getId())
                .employeeName(employee.get().getName())
                .productName(product.get().getName())
                .productID(detail.getPro_Id())
                .warehouseName(warehouse.get().getName())
                .unitMeasure(product.get().getUnitMeasure())
                .unitPrice(detail.getUnitPrice())
                .quantitySold(detail.getQuantity())
                .build();
    }
    @Override
    public SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request) {
        //xóa trung lap
        List<Integer> uniqueProductIds = request.getProductIds().stream().distinct().toList();
        List<Integer> uniqueCustomerIds = request.getCustomerIds().stream().distinct().toList();
        List<Integer> uniqueEmployeeIds = request.getEmployeeIds().stream().distinct().toList();
        //tinh total revenue
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<OutputInvoiceDetail> details = null;
        List<SalesProductResponse> salesProducts = new ArrayList<>();
        List<SalesCustomerResponse> salesCustomers = new ArrayList<>();
        List<SalesEmployeeResponse> salesEmployee = new ArrayList<>();
        //Loc theo cac tieu chi
        if(!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) {// Tim theo product
            return filterByProduct(details,uniqueProductIds, request,salesProducts,totalRevenue);
        }
        else if(request.getProductIds().isEmpty() && !request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) { // Tim theo customer
            return filterByCustomer(request,uniqueCustomerIds,salesCustomers,totalRevenue);
        }else if(request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && !request.getEmployeeIds().isEmpty()) { // Tim theo employee
            return filterByEmployee(request,uniqueEmployeeIds,salesEmployee,totalRevenue);
        }else if(request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()){   // Hien thi toan bo
            return getTotalRevenue(request,totalRevenue);
        }
        return null;
    }
    //doanh thu tim theo quy
    @Override
    public SalesReportResponse generateSalesReportByQuater(SalesRevenueQuarterRequest request) {
        List<OutputInvoice> invoices = fetchInvoiceByQuater(request);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<OutputInvoiceDetail> details;
        List<SalesDetailResponse> salesDetails = new ArrayList<>();
        for(OutputInvoice invoice : invoices) {
            details=outputInvoiceDetailRepository.findByOutputInvoiceId(invoice.getId());
            for (OutputInvoiceDetail detail : details) {
                SalesDetailResponse salesDetail = mapToSalesDetailResponse(invoice, detail);
                salesDetails.add(salesDetail);
            }
        }
        for(OutputInvoice invoice : invoices) {
            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .SalesDetails(salesDetails)
                .build();
    }
    //doanh thu theo quy
    @Override
    public List<SalesQuarterResponse> allQuarterReport(int year) {
        List<SalesQuarterResponse> salesQuarterRespons = new ArrayList<>();
        for(int i = 1; i <= 4; i++) {
            BigDecimal total = BigDecimal.ZERO;
            List<OutputInvoice> invoices =outputInvoiceRepository.findByCreationTimeBetween(getDate(year, i, "start"), getDate(year, i, "end"));
            for(OutputInvoice invoice : invoices) {
                total = total.add(BigDecimal.valueOf(invoice.getTotalAmount()));
            }
            salesQuarterRespons.add(SalesQuarterResponse.builder()
                    .quarter(i)
                    .total(total)
                    .build());
        }
        return salesQuarterRespons;
    }
    //Tim kiem theo khu vuc
    @Override
    public SalesReportResponse salesRevenueByRegion(SalesRevenueByRegionRequest request) {
        List<SalesRegionResponse> result= new ArrayList<>();
        if(request.getRegion().isEmpty()){
            result.add(doanhThuTheoMien("Miền Bắc"));
            result.add(doanhThuTheoMien("Miền Trung"));
            result.add(doanhThuTheoMien("Miền Nam"));
        }
        if(request.getRegion().size()==1){
            result.add(doanhThuTheoMien(request.getRegion().get(0)));
        }
        if(request.getRegion().size()==2){
            for(int i=0;i<2;i++){
                result.add(doanhThuTheoMien(request.getRegion().get(i)));
            }
        }
        BigDecimal totalRevenue= BigDecimal.ZERO;
        for(SalesRegionResponse sr : result){
            totalRevenue= totalRevenue.add(sr.getTotalRevenue());
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .SalesDetails(result)
                .build();
    }

    private SalesRegionResponse doanhThuTheoMien(String region){
        BigDecimal total= BigDecimal.ZERO;
        for(OutputInvoice oi: outputInvoiceRepository.findAll()){
            if(checkAddress(oi.getShipAddress()).equals(region)){
                total= total.add(BigDecimal.valueOf(oi.getTotalAmount()));
            }
        }
        return SalesRegionResponse.builder()
                .region(region)
                .totalRevenue(total)
                .build();
    }


    //Tong doanh thu trong khoang thoi gian
    private SalesReportResponse getTotalRevenue(SalesDetailReportRequest request, BigDecimal totalRevenue) {
        List<OutputInvoice> invoices = fetchInvoices(request);
        for(OutputInvoice invoice : invoices) {
            totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .build();
    }
    //doanh thu theo nhan vien
    private SalesReportResponse filterByEmployee(
            SalesDetailReportRequest request,
            List<Integer> uniqueEmployeeIds,
            List<SalesEmployeeResponse> salesEmployee,
            BigDecimal totalRevenue) {
        List<OutputInvoice> invoices = fetchInvoices(request);
        for(Integer i : uniqueEmployeeIds) {
            Optional<Employee> employee = employeeRepository.findById(i);
            if (employee.isEmpty()) {
                return null;
            }
            for(OutputInvoice invoice : invoices) {
                if(i.equals(invoice.getEmployee().getId())) {
                    salesEmployee.add(SalesEmployeeResponse.builder()
                            .empId(employee.get().getId())
                            .empName(employee.get().getName())
                            .outputInvoiceId(invoice.getId())
                            .total(invoice.getTotalAmount())
                            .build());
                }
            }
            for(OutputInvoice invoice : invoices) {
                totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
            }
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .SalesDetails(salesEmployee)
                .build();
    }
    //doanh thu theo khach hang
    private SalesReportResponse filterByCustomer(
            SalesDetailReportRequest request,
            List<Integer> uniqueCustomerIds,
            List<SalesCustomerResponse> salesCustomers,
            BigDecimal totalRevenue) {
        List<OutputInvoice> invoices = fetchInvoices(request);
        for(Integer i : uniqueCustomerIds) {
            Optional<Customer> customer = customerRepository.findById(i);
            if(customer.isEmpty()) {
                return null;
            }
            for(OutputInvoice invoice : invoices) {
                if(i.equals(invoice.getCustomer().getId())) {
                    salesCustomers.add(SalesCustomerResponse.builder()
                            .cusId(customer.get().getId())
                            .cusName(customer.get().getName())
                            .outputInvoiceId(invoice.getId())
                            .total(invoice.getTotalAmount())
                            .build());
                }
            }
            for(OutputInvoice invoice : invoices) {
                totalRevenue = totalRevenue.add(BigDecimal.valueOf(invoice.getTotalAmount()));
            }
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .SalesDetails(salesCustomers)
                .build();
    }
    //doanh thu theo san pham
    private SalesReportResponse filterByProduct(
            List<OutputInvoiceDetail> details,
            List<Integer> uniqueProductIds,
            SalesDetailReportRequest request,
            List<SalesProductResponse> salesProducts,
            BigDecimal totalRevenue) {
        details=outputInvoiceDetailRepository.filterByProduct(request.getProductIds());
        for(Integer i : uniqueProductIds) {
            double quantity = 0.0;
            double total=0.0;
            SalesProductResponse salesProduct = null;
            for(OutputInvoiceDetail detail : details) {
                if(i.equals(detail.getPro_Id())) {
                    quantity += detail.getQuantity();
                    total += detail.getAmount();
                    salesProduct = SalesProductResponse.builder()
                            .proId(detail.getPro_Id())
                            .proName(productRepository.findById(detail.getPro_Id()).get().getName())
                            .quantity(quantity)
                            .total(total)
                            .build();
                }
            }
            salesProducts.add(salesProduct);
        }
        //tinh total revenue
        for(SalesProductResponse salesProduct : salesProducts) {
            totalRevenue = totalRevenue.add(BigDecimal.valueOf(salesProduct.getTotal()));
        }
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue)
                .SalesDetails(salesProducts)
                .build();
    }

    private List<OutputInvoice> fetchInvoiceByQuater(SalesRevenueQuarterRequest request) {
        List<OutputInvoice> invoices=new ArrayList<>();
        switch (request.getQuarter()) {
            case 1:{
                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 1, "start"), getDate(request.getYear(), 1, "end"));
                break;
            }
            case 2:{
                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 2, "start"), getDate(request.getYear(),2, "end"));
                break;
            }
            case 3:{
                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 3, "start"), getDate(request.getYear(), 3, "end"));
                break;
            }
            case 4:{
                invoices=outputInvoiceRepository.findByCreationTimeBetween(getDate(request.getYear(), 4, "start"), getDate(request.getYear(), 4, "end"));
                break;
            }
        }
        return invoices;
    }
    private List<OutputInvoice> fetchInvoices(SalesDetailReportRequest request) {
        List<OutputInvoice> invoices;

        if (!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) {
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIds(
                    request.getStartDate(), request.getEndDate(), request.getProductIds());
        } else if (!request.getCustomerIds().isEmpty() && request.getProductIds().isEmpty() && request.getEmployeeIds().isEmpty()) {
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndCustomerIds(request.getStartDate(), request.getEndDate(), request.getCustomerIds());
        } else if (!request.getEmployeeIds().isEmpty() && request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()) {
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndEmployeeIds(
                    request.getStartDate(), request.getEndDate(), request.getEmployeeIds());
        } else if (!request.getProductIds().isEmpty() && !request.getCustomerIds().isEmpty() && request.getEmployeeIds().isEmpty()) { // Tim theo product va customer =>done
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndCustomerIds(
                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getCustomerIds());
        } else if (!request.getProductIds().isEmpty() && request.getCustomerIds().isEmpty()) { // Tim theo product va employee =>done
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndEmployeeIds(
                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getEmployeeIds());
        } else if (!request.getCustomerIds().isEmpty()  && request.getProductIds().isEmpty()) { // Tim theo customer va employee =>done
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndCustomerIdsAndEmployeeIds(
                    request.getStartDate(), request.getEndDate(), request.getCustomerIds(), request.getEmployeeIds());
        } else if (request.getCustomerIds().isEmpty()) {// Hien thi toan bo =>done
            invoices = outputInvoiceRepository.findByCreationTimeBetween(request.getStartDate(), request.getEndDate());
        } else{                                                                            // Tim theo product, customer, employee =>done
            invoices = outputInvoiceRepository.findByCreationTimeBetweenAndProductIdsAndCustomerIdsAndEmployeeIds(
                    request.getStartDate(), request.getEndDate(), request.getProductIds(), request.getCustomerIds(), request.getEmployeeIds());
        }
        return invoices;
    }

   @Override
    public void generateReportQuaterToPdf(int year) throws Exception {
        // Tạo một đối tượng Document
        com.itextpdf.text.Document document = new Document();

        // Tạo một đối tượng PdfWriter
        PdfWriter.getInstance(document, new FileOutputStream("sales_quarter_report.pdf"));

        // Mở tài liệu
        document.open();

       BaseFont fontBase = BaseFont.createFont("C:/Windows/Fonts/Tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
       Font font1 = new Font(fontBase, 18, Font.BOLD);
       Font font2= new Font(fontBase,13);
        Chunk chunk = new Chunk("Báo cáo doanh thu theo quý"+ " năm "+year, font1);
        Paragraph title = new Paragraph(chunk);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

// Thêm bảng biểu
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        PdfPCell cell = new PdfPCell(new Phrase("Quý", font1));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Doanh thu", font1));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);

        List<SalesQuarterResponse> salesQuarterRespons = allQuarterReport(year);
        for (SalesQuarterResponse response : salesQuarterRespons) {
            cell = new PdfPCell(new Phrase(String.valueOf(response.getQuarter()), font2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(response.getTotal().toString() + " VND",font2));
            table.addCell(cell);
        }
        BigDecimal total = allQuarterReport(year).stream().map(SalesQuarterResponse::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        cell = new PdfPCell(new Phrase("Tổng doanh thu", font2));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(total+ " VND",font2));
        table.addCell(cell);

        document.add(table);

        // Đóng tài liệu
        document.close();
    }
    @Override
    public void generateReportRegionToPdf() throws Exception {
        // Tạo một đối tượng Document
        com.itextpdf.text.Document document = new Document();

        // Tạo một đối tượng PdfWriter
        PdfWriter.getInstance(document, new FileOutputStream("sales_region_report.pdf"));

        // Mở tài liệu
        document.open();

        BaseFont fontBase = BaseFont.createFont("C:/Windows/Fonts/Tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font1 = new Font(fontBase, 18, Font.BOLD);
        Font font2= new Font(fontBase,13);
        Chunk chunk = new Chunk("Báo cáo doanh thu theo khu vực", font1);
        Paragraph title = new Paragraph(chunk);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Thêm bảng biểu
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        PdfPCell cell = new PdfPCell(new Phrase("Khu Vực", font1));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Doanh thu", font1));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);

        List<SalesRegionResponse> salesRegionResponses= new ArrayList<>();
        salesRegionResponses.add(doanhThuTheoMien("Miền Bắc"));
        salesRegionResponses.add(doanhThuTheoMien("Miền Trung"));
        salesRegionResponses.add(doanhThuTheoMien("Miền Nam"));
        for (SalesRegionResponse response : salesRegionResponses) {
            cell = new PdfPCell(new Phrase(response.getRegion(),font2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(response.getTotalRevenue().toString() + " VND"));
            table.addCell(cell);
        }
        BigDecimal total= salesRegionResponses.stream().map(SalesRegionResponse::getTotalRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        cell = new PdfPCell(new Phrase("Tổng",font2));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(total + " VND",font2));
        table.addCell(cell);


        document.add(table);

        // Đóng tài liệu
        document.close();
    }
}