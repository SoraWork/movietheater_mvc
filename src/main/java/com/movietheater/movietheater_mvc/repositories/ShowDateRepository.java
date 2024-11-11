package com.movietheater.movietheater_mvc.repositories;

import java.util.List;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movietheater.movietheater_mvc.entities.ShowDate;

@Repository
public interface ShowDateRepository extends JpaRepository<ShowDate, UUID>{
    List<ShowDate> findByShowDateBetween(LocalDate startDate, LocalDate endDate);
}
