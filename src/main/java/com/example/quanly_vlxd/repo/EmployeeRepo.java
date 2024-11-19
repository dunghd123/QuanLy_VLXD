package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.dto.response.EmpResponse;
import com.example.quanly_vlxd.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    @Query(value = "select * from employees e where e.isactive = 1",nativeQuery = true)
    Page<Employee> getAll(Pageable pageable);
}
