package com.movietheater.movietheater_mvc.services;

import java.util.List;
import java.util.UUID;

import com.movietheater.movietheater_mvc.dtos.employee.EmployeeCreateDTO;
import com.movietheater.movietheater_mvc.dtos.employee.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EmployeeService {
    List<EmployeeDTO> findAll();

    Page<EmployeeDTO> findAll(String keyword, Pageable pageable);

    EmployeeDTO findById(UUID id);

    EmployeeDTO create(EmployeeCreateDTO employeeCreateDTO);

    EmployeeDTO update(UUID id, EmployeeDTO employeeDTO);

    boolean deleteById(UUID id);

    boolean existsByUsername(String username); 
}
