package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    
      Employee findByAccount_Email(String email);

      boolean existsByAccount_Email(String email);

      Employee findByAccount_IdentityCard(String identityCard);

      boolean existsByAccount_IdentityCard(String identityCard);

      boolean existsByAccount_Username(String username);
}
