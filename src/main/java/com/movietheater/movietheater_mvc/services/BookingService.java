package com.movietheater.movietheater_mvc.services;

import java.util.UUID;
import java.util.List;

import com.movietheater.movietheater_mvc.entities.ScheduleSeat;

public interface BookingService {
    boolean bookTickets(UUID movieId, String selectedSeats, int totalPrice, String schedule, String time, String username);
    List<ScheduleSeat> getSeatsByScheduleId(UUID scheduleId);
}
