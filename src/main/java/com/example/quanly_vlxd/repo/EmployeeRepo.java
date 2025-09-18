package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    @Query(value = "select * from employees e where e.isactive = 1",nativeQuery = true)
    Page<Employee> getAll(Pageable pageable);

    @Query(value = "select * from employees e where e.isactive = 1 and e.manager_id is null",nativeQuery = true)
    List<Employee> getAllManager();
    @Query(value = "select * from employees e where e.user_id = ?1 and e.isactive = 1", nativeQuery = true)
    Employee findByUserID(int userId);
    @Query(value = "select * from employees e where e.user_id != ?1 and e.isactive = 1", nativeQuery = true)
    List<Employee> findAnotherEmployee(int userId);
    boolean existsByPhoneNum(String phoneNum);
    boolean existsByPhoneNumAndUserIdNot(String phoneNum, int id);
}
