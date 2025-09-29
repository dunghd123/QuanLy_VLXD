package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputInvoiceDetailRepo extends JpaRepository<InputInvoiceDetail,Integer> {
    List<InputInvoiceDetail> findByInputInvoiceId(int id);

    @Query(value = "SELECT SUM(d.amount) FROM input_invoice_details d WHERE d.inp_id = :invoiceId", nativeQuery = true)
    Double sumAmountByInvoiceId(@Param("invoiceId") int invoiceId);

    @Query(value = "select * from input_invoice_details i where i.pro_id in :productIds", nativeQuery = true)
    List<InputInvoiceDetail> filterByProduct(List<Integer> productIds);

    @Query(value = "select * from input_invoice_details i where i.pro_id =:proid and i.inp_id in :inputInvoiceIds", nativeQuery = true)
    List<InputInvoiceDetail> findByProductIDAndInputInvoiceID(List<Integer> inputInvoiceIds, int proid);

    @Query(value = "select sum(i.iid_quantity) from input_invoice_details i where i.pro_id = ?1", nativeQuery = true)
    Double totalQuantityInputInvoice(int proId);

}
