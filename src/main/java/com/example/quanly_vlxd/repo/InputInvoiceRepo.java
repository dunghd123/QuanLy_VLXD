package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.InputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InputInvoiceRepo extends JpaRepository<InputInvoice,Integer> {
    @Query(value = "select * from input_invoices i where i.isactive = 1 and i.inp_id = ?1",nativeQuery = true)
    Optional<InputInvoice> findByInputID(int  id);
    //Theo thoi gian
    @Query(value = "select * from input_invoices i where i.inp_creation_time between ?1 and ?2",nativeQuery = true)
    List<InputInvoice> findByCreationTimeBetween(Date startDate, Date endDate);
    //theo san pham
    @Query(value = "SELECT DISTINCT o.* FROM input_invoices i " +
            "INNER JOIN  input_invoice_details d ON i.inp_id = d.inp_id " +
            "WHERE i.inp_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds", nativeQuery = true)
    List<InputInvoice> findByCreationTimeBetweenAndProductIds(
            Date startDate, Date endDate, List<Integer> productIds);
    //theo nha cung cap
    @Query(value = "SELECT DISTINCT o.* FROM input_invoices i " +
            "INNER JOIN  suppliers s ON i.sup_id = s.sup_id " +
            "WHERE i.inp_creation_time BETWEEN :startDate AND :endDate " +
            "AND s.sup_id IN :supplierIds", nativeQuery = true)
    List<InputInvoice> findByCreationTimeBetweenAndSupplierIds(
            Date startDate, Date endDate, List<Integer> supplierIds);
    //theo san pham va nha cung cap
    @Query(value = "SELECT DISTINCT o.* FROM input_invoices i " +
            "INNER JOIN input_invoice_details d ON o.inp_id = d.inp_id " +
            "WHERE i.inp_creation_time BETWEEN :startDate AND :endDate " +
            "AND d.pro_id IN :productIds " +
            "AND i.sup_id IN :supplierIds", nativeQuery = true)
    List<InputInvoice> findByCreationTimeBetweenAndProductIdsAndSupplierIds(
            Date startDate, Date endDate, List<Integer> productIds, List<Integer> supplierIds);
}
