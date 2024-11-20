package com.movietheater.movietheater_mvc.services;

import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomCreateDTO;
import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomDTO;
import com.movietheater.movietheater_mvc.entities.Seat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;


public interface CinemaRoomService {
    List<CinemaRoomDTO> findAll();

    Page<CinemaRoomDTO> findAll(String keyword, Pageable pageable);

    CinemaRoomDTO findById(UUID id);

    CinemaRoomDTO findByRoomName(String cinemaRoomName);

    CinemaRoomDTO create(CinemaRoomCreateDTO cinemaRoomCreateDTO);

    CinemaRoomDTO update(UUID id, CinemaRoomDTO cinemaRoomDTO);

    boolean deleteById(UUID id);

    List<Seat> getSeatsByCinemaRoomId(UUID cinemaRoomId);

    List<Seat> getSeatsByCinemaRoomName(String cinemaRoomName);

    Boolean updateSeatTypes(List<Seat> selectedSeatsList);

}
