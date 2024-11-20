package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.movietheater.movietheater_mvc.entities.Seat;

public interface SeatRepository extends JpaRepository<Seat, UUID>, JpaSpecificationExecutor<Seat>{
    List<Seat> findSeatsByCinemaRoomId(UUID cinemaRoomId);
}
