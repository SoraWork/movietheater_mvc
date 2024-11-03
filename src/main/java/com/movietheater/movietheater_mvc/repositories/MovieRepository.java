package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Movie;


public interface MovieRepository extends JpaRepository<Movie, UUID>, JpaSpecificationExecutor<Movie>{

    Movie findByMovieNameEnglish(String movieNameEnglish);

    Movie findByMovieNameVn(String movieNameVn);

}
