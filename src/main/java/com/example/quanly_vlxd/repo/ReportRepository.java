package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.OutputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<OutputInvoice, Integer> {
    @Query(value = """
    SELECT  e.emp_id AS empId,
            e.emp_name AS empName,
            SUM(oi_total_amount) AS totalAmount
       FROM output_invoices o INNER JOIN employees e ON o.emp_id= e.emp_id
       WHERE o.oi_status = "COMPLETED" AND o.oi_creation_time BETWEEN :start AND :end
       GROUP BY e.emp_id, e.emp_name
    """, nativeQuery = true)
    List<Object[]> findRevenueByEmployee(
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


    @Query(value = """
        SELECT m.month AS month,
               COALESCE(SUM(o.oi_total_amount), 0) AS totalAmount
        FROM (
          SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
          SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION
          SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) m
        LEFT JOIN output_invoices o
               ON MONTH(o.oi_creation_time) = m.month
              AND YEAR(o.oi_creation_time) = :year
              AND o.isactive = 1
              AND o.oi_status = 'COMPLETED'
        GROUP BY m.month
        ORDER BY m.month
    """, nativeQuery = true)
    List<Object[]> findRevenueByMonth(@Param("year") int year);

    @Query(value = """
        SELECT q.quarter AS quarter,
               COALESCE(SUM(o.oi_total_amount), 0) AS totalAmount
        FROM (
          SELECT 1 AS quarter UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
        ) q
        LEFT JOIN output_invoices o
               ON QUARTER(o.oi_creation_time) = q.quarter
              AND YEAR(o.oi_creation_time) = :year
              AND o.isactive = 1
              AND o.oi_status = 'COMPLETED'
        GROUP BY q.quarter
        ORDER BY q.quarter
    """, nativeQuery = true)
    List<Object[]> findRevenueByQuarter(@Param("year") int year);
}
