package com.movietheater.movietheater_mvc.services;

import java.util.UUID;
import java.util.List;

import com.movietheater.movietheater_mvc.entities.Schedule;

public interface ScheduleService {
    List<Schedule> getSchedulesByShowDateAndMovieId(UUID movieId, UUID showDateId);
}
