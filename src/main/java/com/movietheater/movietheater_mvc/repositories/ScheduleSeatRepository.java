package com.movietheater.movietheater_mvc.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movietheater.movietheater_mvc.entities.ScheduleSeat;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, UUID>{
    List<ScheduleSeat> findByScheduleId(UUID scheduleId);
}
