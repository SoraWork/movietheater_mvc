package com.movietheater.movietheater_mvc.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.entities.Seat;
import com.movietheater.movietheater_mvc.repositories.SeatRepository;
import com.movietheater.movietheater_mvc.services.SeatService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Optional<Seat> findById(UUID seatId) {
        return seatRepository.findById(seatId);
    }

    @Override
    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    public void deleteById(UUID seatId) {
        seatRepository.deleteById(seatId);
    }

    @Override
    public List<Seat> findSeatsByCinemaRoom(UUID cinemaRoomId) {
        return seatRepository.findSeatsByCinemaRoomId(cinemaRoomId);
    }
    
}
