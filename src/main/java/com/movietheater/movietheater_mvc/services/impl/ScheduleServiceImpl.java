package com.movietheater.movietheater_mvc.services.impl;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.entities.Schedule;
import com.movietheater.movietheater_mvc.repositories.ScheduleRepository;
import com.movietheater.movietheater_mvc.services.ScheduleService;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
   public List<Schedule> getSchedulesByShowDateAndMovieId(UUID movieId, UUID showDateId) {
        return null;
    }


}
