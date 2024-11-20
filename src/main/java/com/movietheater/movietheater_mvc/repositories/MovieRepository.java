package com.movietheater.movietheater_mvc.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movietheater.movietheater_mvc.entities.Movie;


public interface MovieRepository extends JpaRepository<Movie, UUID>, JpaSpecificationExecutor<Movie>{

    Movie findByMovieNameEnglish(String movieNameEnglish);

    Movie findByMovieNameVn(String movieNameVn);

    // Tìm các bộ phim theo ngày chiếu
    @Query("SELECT m FROM Movie m JOIN m.showDates sd WHERE sd.showDate = :showDate")
    List<Movie> findByShowDate(@Param("showDate") LocalDate showDate);

    // Tìm các bộ phim theo tên của thể loại
    @Query("SELECT m FROM Movie m JOIN m.types t WHERE t.typeName = :typeName")
    List<Movie> findByTypeName(@Param("typeName") String typeName);

}
