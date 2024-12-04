package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.OutputInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutputInvoiceDetailRepo extends JpaRepository<OutputInvoiceDetail,Integer> {
    List<OutputInvoiceDetail> findByOutputInvoiceId(int outputInvoiceId);
    @Query(value = "select * from output_invoice_details o where o.pro_id in ?2 and o.oi_id = ?1",nativeQuery = true)
    List<OutputInvoiceDetail> findByOutputInvoiceIdAndProductId(int outputInvoiceId, List<Integer> productIds);

    //Doanh thu theo san pham
    @Query(value = "select * from output_invoice_details o where o.pro_id in ?1", nativeQuery = true)
    List<OutputInvoiceDetail> filterByProduct(List<Integer> productIds);


}
