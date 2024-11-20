package com.movietheater.movietheater_mvc.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movietheater.movietheater_mvc.entities.ScheduleSeat;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, UUID>{
    List<ScheduleSeat> findByMovieIdAndScheduleId(UUID movieId, UUID scheduleId);

    @Query("SELECT ss FROM ScheduleSeat ss WHERE ss.movieId = :movieId AND ss.scheduleId IN :scheduleIds")
    List<ScheduleSeat> findByMovieIdAndScheduleIds(
            @Param("movieId") UUID movieId,
            @Param("scheduleIds") List<UUID> scheduleIds);
}
