package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.OutputInvoice;
import com.example.quanly_vlxd.entity.OutputInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputInvoiceDetailRepo extends JpaRepository<OutputInvoiceDetail,Integer> {

}
