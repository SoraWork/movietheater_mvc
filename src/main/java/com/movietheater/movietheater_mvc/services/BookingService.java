package com.movietheater.movietheater_mvc.services;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

import com.movietheater.movietheater_mvc.entities.ScheduleSeat;

public interface BookingService {
    boolean bookTickets(UUID movieId, String selectedSeats, int totalPrice, String schedule, String time, String username);
    List<ScheduleSeat> findAvailableSeatsByMovieAndDate(UUID movieId, LocalDate date, String scheduleTime);
    UUID getScheduleIdByMovieAndTime(UUID movieId, LocalDate date, String scheduleTime);
}
