package com.movietheater.movietheater_mvc.services;

import java.util.List;
import java.util.UUID;

import com.movietheater.movietheater_mvc.entities.ShowDate;

public interface ShowDateService {

    List<ShowDate> findAll();

    ShowDate findById(UUID id);

    List<ShowDate> findNextThreeDays();
}
