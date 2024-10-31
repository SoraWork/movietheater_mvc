package com.movietheater.movietheater_mvc.repositories;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Account;


public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account>{
    Account findByUsername(String username);

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<Account> findById(UUID id);

}
