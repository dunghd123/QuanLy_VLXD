package com.example.quanly_vlxd.repo;


import com.example.quanly_vlxd.entity.OutputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface OutputInvoiceRepo extends JpaRepository<OutputInvoice,Integer>, JpaSpecificationExecutor<OutputInvoice> {
    @Query(value = "select * from output_invoices o where o.isactive = 1 and o.oi_id = ?1",nativeQuery = true)
    Optional<OutputInvoice> findByOutputInvoiceId(int id);
    @Query(value = "select * from output_invoices o where o.isactive = 1 and o.emp_id = ?1",nativeQuery = true)
    List<OutputInvoice> findByEmployeeId(int employeeId);
    ///Chi tiet ban hang
    // Lọc theo sản phẩm (nhiều sản phẩm)
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "INNER JOIN  output_invoice_details d ON o.oi_id = d.oi_id " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndProductIds(
            Date startDate, Date endDate, List<Integer> productIds);

    // Lọc theo khách hàng (nhiều khách hàng)
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND o.cus_id IN :customerIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndCustomerIds(
            Date startDate, Date endDate, List<Integer> customerIds);

    // Lọc theo nhân viên (nhiều nhân viên)
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND o.emp_id IN :employeeIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndEmployeeIds(
            Date startDate, Date endDate, List<Integer> employeeIds);

    // Lọc theo kết hợp sản phẩm và khách hàng
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "INNER JOIN output_invoice_details d ON o.oi_id = d.oi_id " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds " +
            "AND o.cus_id IN :customerIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndProductIdsAndCustomerIds(
            Date startDate, Date endDate, List<Integer> productIds, List<Integer> customerIds);

    // Lọc theo kết hợp sản phẩm và nhân viên
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "INNER JOIN output_invoice_details d ON o.oi_id = d.oi_id " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds " +
            "AND o.emp_id IN :employeeIds",nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndProductIdsAndEmployeeIds(
            Date startDate, Date endDate, List<Integer> productIds, List<Integer> employeeIds);

    // Lọc theo kết hợp khách hàng và nhân viên
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND o.cus_id IN :customerIds " +
            "AND o.emp_id IN :employeeIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndCustomerIdsAndEmployeeIds(
            Date startDate, Date endDate, List<Integer> customerIds, List<Integer> employeeIds);

    // Lọc theo kết hợp tất cả 3 tiêu chí: sản phẩm, khách hàng, nhân viên
    @Query(value = "SELECT DISTINCT o.* FROM output_invoices o " +
            "INNER JOIN output_invoice_details d ON o.oi_id = d.oi_id " +
            "WHERE o.oi_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds " +
            "AND o.cus_id IN :customerIds " +
            "AND o.emp_id IN :employeeIds", nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetweenAndProductIdsAndCustomerIdsAndEmployeeIds(
            Date startDate, Date endDate, List<Integer> productIds, List<Integer> customerIds, List<Integer> employeeIds);

    @Query(value = "select * from output_invoices o where o.oi_creation_time between ?1 and ?2",nativeQuery = true)
    List<OutputInvoice> findByCreationTimeBetween(Date startDate, Date endDate);
    @Query(value = "select * from output_invoices o where YEAR(o.oi_creation_time) = ?1",nativeQuery = true)
    List<OutputInvoice> findAllInYear(int year);

}
