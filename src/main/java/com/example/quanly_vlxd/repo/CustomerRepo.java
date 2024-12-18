package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    @Query(value = "select * from customers c where c.isactive = 1",nativeQuery = true)
    Page<Customer> getAll(Pageable pageable);
}
