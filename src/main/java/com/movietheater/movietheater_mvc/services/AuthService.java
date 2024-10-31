package com.movietheater.movietheater_mvc.services;

import java.util.UUID;

import com.movietheater.movietheater_mvc.dtos.auth.RegisterDTO;

public interface AuthService {
    UUID save(RegisterDTO registerDTO);

    boolean existsByUsername(String username); 
}
