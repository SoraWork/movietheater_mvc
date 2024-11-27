package com.movietheater.movietheater_mvc.repositories;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.movietheater.movietheater_mvc.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID>, JpaSpecificationExecutor<Schedule>{
    Schedule findByScheduleTime(String scheduleTime);
    @Query("""
        SELECT s FROM Schedule s
        JOIN s.movies m
        JOIN m.showDates d
        WHERE m.cinemaRoomId = :cinemaRoomId
          AND s.scheduleTime = :scheduleTime
          AND d.showDate = :showDate
    """)
    List<Schedule> findConflictingSchedules(UUID cinemaRoomId, String scheduleTime, LocalDate showDate);
}
