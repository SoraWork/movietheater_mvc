package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Type;


public interface TypeRepository extends JpaRepository<Type, UUID>, JpaSpecificationExecutor<Type>{

}
