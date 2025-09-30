package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.dto.response.SalesEmployeeResponse;
import com.example.quanly_vlxd.entity.OutputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<OutputInvoice, Integer> {
    @Query("""
       SELECT new com.example.quanly_vlxd.dto.response.SalesEmployeeResponse(
           e.id, e.name, o.totalAmount
       )
       FROM OutputInvoice o
       JOIN o.employee e
       WHERE o.creationTime BETWEEN :start AND :end
             AND o.status = com.example.quanly_vlxd.enums.InvoiceStatusEnums.COMPLETED
       GROUP BY e.id, e.name
    """)
    List<SalesEmployeeResponse> findRevenueByEmployee(
            @Param("start") Date start,
            @Param("end") Date end
    );


    @Query(value = """
      SELECT p.pro_id AS productId,
             p.pro_name AS productName,
             SUM(d.oid_quantity) AS totalQuantity,
             SUM(d.oid_amount) AS totalAmount
      FROM output_invoice_details d
      JOIN products p ON d.pro_id = p.pro_id
      JOIN output_invoices o ON d.oi_id = o.oi_id
      WHERE o.oi_creation_time BETWEEN :start AND :end
        AND o.isactive = 1
        AND p.isactive = 1
        AND o.oi_status= 'COMPLETED'
      GROUP BY p.pro_id, p.pro_name
    """, nativeQuery = true)
    List<Object[]> findRevenueByProductNative(
            @Param("start") Date start,
            @Param("end") Date end
    );
    @Query(value = """
      SELECT c.cus_id AS cusId,
             c.cus_name AS cusName,
             SUM(o.oi_total_amount) AS totalAmount
      FROM output_invoices o JOIN customers c ON c.cus_id = o.cus_id
      WHERE o.oi_creation_time BETWEEN :start AND :end
        AND o.isactive = 1
        AND c.isactive = 1
        AND o.oi_status= 'COMPLETED'
      GROUP BY c.cus_id, c.cus_name
    """, nativeQuery = true)
    List<Object[]> findRevenueByCustomer(
            @Param("start") Date start,
            @Param("end") Date end
    );
}
