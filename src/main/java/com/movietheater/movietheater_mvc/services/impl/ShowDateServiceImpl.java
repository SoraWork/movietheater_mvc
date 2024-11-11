package com.movietheater.movietheater_mvc.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.entities.ShowDate;
import com.movietheater.movietheater_mvc.repositories.ShowDateRepository;
import com.movietheater.movietheater_mvc.services.ShowDateService;

@Service
public class ShowDateServiceImpl implements ShowDateService {
    private final ShowDateRepository showDateRepository;

    public ShowDateServiceImpl(ShowDateRepository showDateRepository) {
        this.showDateRepository = showDateRepository;
    }

    @Override
    public List<ShowDate> findAll() {
       var showDates = showDateRepository.findAll();
        return showDates;
    }

    @Override
    public ShowDate findById(UUID id) {
       var showdate = showDateRepository.findById(id).orElse(null);
        // Check if entity is null then return null
        if (showdate == null) {
            return null;
        }
        return showdate;
    }

    @Override
    public List<ShowDate> findNextThreeDays() {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);
        
        return showDateRepository.findByShowDateBetween(today, threeDaysLater);
    }

}
