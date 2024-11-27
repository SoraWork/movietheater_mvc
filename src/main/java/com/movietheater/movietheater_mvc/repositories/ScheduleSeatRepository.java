package com.movietheater.movietheater_mvc.repositories;

import java.time.LocalDate;
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

  @Query("SELECT s FROM ScheduleSeat s WHERE s.movieId = :movieId AND s.scheduleId IN " +
       "(SELECT sch.id FROM Schedule sch WHERE sch.scheduleTime = :scheduleTime) AND s.seatDate = :seatDate")
    List<ScheduleSeat> findBookedSeatsByMovieAndScheduleAndDate(
            @Param("movieId") UUID movieId, 
            @Param("scheduleTime") String scheduleTime, 
            @Param("seatDate") LocalDate seatDate);
}
