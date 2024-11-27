package com.movietheater.movietheater_mvc.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movietheater.movietheater_mvc.dtos.employee.EmployeeCreateDTO;
import com.movietheater.movietheater_mvc.dtos.employee.EmployeeDTO;
import com.movietheater.movietheater_mvc.entities.Account;
import com.movietheater.movietheater_mvc.entities.Employee;
import com.movietheater.movietheater_mvc.repositories.AccountRepository;
import com.movietheater.movietheater_mvc.repositories.EmployeeRepository;
import com.movietheater.movietheater_mvc.repositories.RoleRepository;
import com.movietheater.movietheater_mvc.services.EmployeeService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        // Get List of entities
        var employees = employeeRepository.findAll();

        // Convert entities to DTOs
        var employeeDTOs = employees.stream().map(employee -> {
            var employeeDTO = new EmployeeDTO();
            employeeDTO.setId(employee.getId());
            employeeDTO.setUsername(employee.getAccount().getUsername());
            employeeDTO.setFullName(employee.getAccount().getFullName());
            employeeDTO.setDateOfBirth(employee.getAccount().getDateOfBirth());
            employeeDTO.setGender(employee.getAccount().getGender());
            employeeDTO.setEmail(employee.getAccount().getEmail());
            employeeDTO.setIdentityCard(employee.getAccount().getIdentityCard());
            employeeDTO.setPhoneNumber(employee.getAccount().getPhoneNumber());
            employeeDTO.setAddress(employee.getAccount().getAddress());
            return employeeDTO;
        }).toList();

        return employeeDTOs;
        }
    


        @Override
        public Page<EmployeeDTO> findAll(String keyword, Pageable pageable) {
            Page<Employee> employees;

            if (keyword == null || keyword.trim().isEmpty()) {
             
                employees = employeeRepository.findAll(pageable);
            } else {
              
                Specification<Employee> specification = (root, query, criteriaBuilder) -> {
                  
                    Join<Employee, Account> accountJoin = root.join("account", JoinType.LEFT);

                 
                    Predicate emailPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("email")), "%" + keyword.toLowerCase() + "%"
                    );

                    Predicate identityCardPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("identityCard")), "%" + keyword.toLowerCase() + "%"
                    );

                  
                    return criteriaBuilder.or(emailPredicate, identityCardPredicate);
                };

               
                employees = employeeRepository.findAll(specification, pageable);
            }

           
            return employees.map(employee -> {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setId(employee.getId());
                employeeDTO.setUsername(employee.getAccount().getUsername());
                employeeDTO.setFullName(employee.getAccount().getFullName());
                employeeDTO.setDateOfBirth(employee.getAccount().getDateOfBirth());
                employeeDTO.setGender(employee.getAccount().getGender());
                employeeDTO.setEmail(employee.getAccount().getEmail());
                employeeDTO.setIdentityCard(employee.getAccount().getIdentityCard());
                employeeDTO.setPhoneNumber(employee.getAccount().getPhoneNumber());
                employeeDTO.setAddress(employee.getAccount().getAddress());
                return employeeDTO;
            });
        }


    @Override
    public EmployeeDTO findById(UUID id) {
        // Get entity by id
        var employee = employeeRepository.findById(id).orElse(null);

       // Check if entity is null then return null
        if (employee == null) {
            return null;
        }
        // Convert entity to DTO
        var employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setUsername(employee.getAccount().getUsername());
        employeeDTO.setFullName(employee.getAccount().getFullName());
        employeeDTO.setDateOfBirth(employee.getAccount().getDateOfBirth());
        employeeDTO.setGender(employee.getAccount().getGender());
        employeeDTO.setEmail(employee.getAccount().getEmail());
        employeeDTO.setIdentityCard(employee.getAccount().getIdentityCard());
        employeeDTO.setPhoneNumber(employee.getAccount().getPhoneNumber());
        employeeDTO.setAddress(employee.getAccount().getAddress());
        return employeeDTO;
    }

    @Override
    public EmployeeDTO create(EmployeeCreateDTO employeeCreateDTO) {
         // Check if employeeCreateDTO is null then throw exception
         if (employeeCreateDTO == null) {
            throw new IllegalArgumentException("EmployeeCreateDTO cannot be null");
        }
        // Check if existingUser is exists then throw exception
        var existingUser = existsByUsername(employeeCreateDTO.getUsername());
        if (existingUser) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email exists in database then throw exception
        var existingEmail = existsByEmail(employeeCreateDTO.getEmail());
        if (existingEmail) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check if identityCard exists in database then throw exception
        var existingIdentityCard = existsByIdentityCard(employeeCreateDTO.getIdentityCard());
        if (existingIdentityCard) {
            throw new IllegalArgumentException("IdentityCard already exists");
        }
        var user = new Account();
        user.setUsername(employeeCreateDTO.getUsername());;
        user.setFullName(employeeCreateDTO.getFullName());
        user.setDateOfBirth(employeeCreateDTO.getDateOfBirth());
        user.setGender(employeeCreateDTO.getGender());
        user.setIdentityCard(employeeCreateDTO.getIdentityCard());
        user.setEmail(employeeCreateDTO.getEmail());
        user.setAddress(employeeCreateDTO.getAddress());
        user.setPhoneNumber(employeeCreateDTO.getPhoneNumber());
        user.setEmail(employeeCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(employeeCreateDTO.getPassword()));

        var role = roleRepository.findByRoleName("EMPLOYEE")
        .orElseThrow(() -> new IllegalArgumentException("Role EMPLOYEE does not exist"));

        user.setRole(role);

        accountRepository.save(user);

        var employee = new Employee();
        employee.setAccount(user);
        employeeRepository.save(employee);

        var employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setUsername(employee.getAccount().getUsername());
        employeeDTO.setFullName(employee.getAccount().getFullName());
        employeeDTO.setDateOfBirth(employee.getAccount().getDateOfBirth());
        employeeDTO.setGender(employee.getAccount().getGender());
        employeeDTO.setEmail(employee.getAccount().getEmail());
        employeeDTO.setIdentityCard(employee.getAccount().getIdentityCard());
        employeeDTO.setPhoneNumber(employee.getAccount().getPhoneNumber());
        employeeDTO.setAddress(employee.getAccount().getAddress());
        employeeDTO.setPassword(employee.getAccount().getPassword());

        return employeeDTO;
        
    }

    @Override
    public EmployeeDTO update(UUID id, EmployeeDTO employeeDTO) {
        // Check if employeeDTO is null then throw exception
        if (employeeDTO == null) {
            throw new IllegalArgumentException("EmployeeDTO cannot be null");
        }
        // Get entity by id
        var employee = employeeRepository.findById(id).orElse(null);
        
        // Check if entity is null then return null
        if (employee == null) {
            return null;
        }
        // Check if account with the same email exists
        var employeeWithSameEmail = employeeRepository.findByAccount_Email(employeeDTO.getEmail()); 
        if (employeeWithSameEmail != null && !employeeWithSameEmail.getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Check if account with the same identity card exists
        var employeeWithSameIdentityCard = employeeRepository.findByAccount_IdentityCard(employeeDTO.getIdentityCard());
        if (employeeWithSameIdentityCard != null && !employeeWithSameIdentityCard.getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Identity card already exists");
        }

        // Update entity

        employee.getAccount().setPassword(employeeDTO.getPassword());
        employee.getAccount().setFullName(employeeDTO.getFullName());
        employee.getAccount().setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.getAccount().setGender(employeeDTO.getGender());
        employee.getAccount().setIdentityCard(employeeDTO.getIdentityCard());
        employee.getAccount().setEmail(employeeDTO.getEmail());
        employee.getAccount().setAddress(employeeDTO.getAddress());
        employee.getAccount().setPhoneNumber(employeeDTO.getPhoneNumber());
        
        // Save entity
        employee = employeeRepository.save(employee);

        // Convert entity to DTO
        var employeeDTOUpdated = new EmployeeDTO();
        employeeDTOUpdated.setId(employee.getId());
        employeeDTOUpdated.setUsername(employee.getAccount().getUsername());
        employeeDTOUpdated.setFullName(employee.getAccount().getFullName());
        employeeDTOUpdated.setDateOfBirth(employee.getAccount().getDateOfBirth());
        employeeDTOUpdated.setGender(employee.getAccount().getGender());
        employeeDTOUpdated.setEmail(employee.getAccount().getEmail());
        employeeDTOUpdated.setIdentityCard(employee.getAccount().getIdentityCard());
        employeeDTOUpdated.setPhoneNumber(employee.getAccount().getPhoneNumber());
        employeeDTOUpdated.setAddress(employee.getAccount().getAddress());
        employeeDTOUpdated.setPassword(employee.getAccount().getPassword());

        return employeeDTOUpdated;
    }

    @Override
    public boolean deleteById(UUID id) {
        var employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return false;
        }
        employeeRepository.delete(employee);
        // Check if entity is deleted
        var isDeleted = employeeRepository.findById(id).orElse(null) == null;
          
        return isDeleted;
    }

    @Override
    public boolean existsByUsername(String username) {
       return employeeRepository.existsByAccount_Username(username);
    }

    @Override
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        
        Page<Employee> employees = employeeRepository.findAll(pageable);
    
    
        return employees.map(employee -> {
           
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(employee.getId());
            employeeDTO.setUsername(employee.getAccount().getUsername());
            employeeDTO.setFullName(employee.getAccount().getFullName());
            employeeDTO.setDateOfBirth(employee.getAccount().getDateOfBirth());
            employeeDTO.setGender(employee.getAccount().getGender());
            employeeDTO.setEmail(employee.getAccount().getEmail());
            employeeDTO.setIdentityCard(employee.getAccount().getIdentityCard());
            employeeDTO.setPhoneNumber(employee.getAccount().getPhoneNumber());
            employeeDTO.setAddress(employee.getAccount().getAddress());
            return employeeDTO;
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByAccount_Email(email);
    }

    @Override
    public boolean existsByIdentityCard(String username) {
        return employeeRepository.existsByAccount_IdentityCard(username);
    }
    


}
