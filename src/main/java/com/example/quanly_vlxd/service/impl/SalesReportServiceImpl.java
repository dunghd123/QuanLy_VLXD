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