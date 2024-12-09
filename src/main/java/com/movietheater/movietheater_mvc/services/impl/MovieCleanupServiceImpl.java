package com.movietheater.movietheater_mvc.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.entities.Movie;
import com.movietheater.movietheater_mvc.repositories.MovieRepository;

@Service
public class MovieCleanupServiceImpl {

    @Autowired
    private MovieRepository movieRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy lúc 0h mỗi ngày
    public void removeExpiredMovies() {
        LocalDate today = LocalDate.now();
        List<Movie> expiredMovies = movieRepository.findByToDateBefore(today);
        if (!expiredMovies.isEmpty()) {
            movieRepository.deleteAll(expiredMovies);
            System.out.println("Deleted " + expiredMovies.size() + " expired movies.");
        }
    }
}
