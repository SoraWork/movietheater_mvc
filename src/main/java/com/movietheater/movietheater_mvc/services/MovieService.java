package com.movietheater.movietheater_mvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movietheater.movietheater_mvc.dtos.movie.MovieCreateDTO;
import com.movietheater.movietheater_mvc.dtos.movie.MovieDTO;

public interface MovieService {
  List<MovieDTO> findAll();

    Page<MovieDTO> findAll(String keyword, Pageable pageable);

    MovieDTO findById(UUID id);

    MovieDTO create(MovieCreateDTO movieCreateDTO);

    MovieDTO update(UUID id, MovieDTO movieDTO);

    boolean deleteById(UUID id);

}
