package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID>, JpaSpecificationExecutor<Schedule>{
    Schedule findByScheduleTime(String scheduleTime);
}
