package com.movietheater.movietheater_mvc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import com.movietheater.movietheater_mvc.entities.Seat;

public interface SeatService {
    Optional<Seat> findById(UUID seatId);
    List<Seat> findSeatsByCinemaRoom(UUID cinemaRoomId);
    Seat save(Seat seat);
    void deleteById(UUID seatId);
}
