package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    
      // Tìm Employee qua email của Account liên kết
      Employee findByAccount_Email(String email);

      boolean existsByAccount_Email(String email);

      // Tìm Employee qua identityCard của Account liên kết
      Employee findByAccount_IdentityCard(String identityCard);

      boolean existsByAccount_IdentityCard(String identityCard);
  
      // Kiểm tra xem username của Account liên kết đã tồn tại chưa
      boolean existsByAccount_Username(String username);
}
