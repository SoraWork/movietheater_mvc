package com.movietheater.movietheater_mvc.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomCreateDTO;
import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomDTO;
import com.movietheater.movietheater_mvc.entities.CinemaRoom;
import com.movietheater.movietheater_mvc.entities.Seat;
import com.movietheater.movietheater_mvc.repositories.CinemaRoomRepository;
import com.movietheater.movietheater_mvc.repositories.SeatRepository;
import com.movietheater.movietheater_mvc.services.CinemaRoomService;

import jakarta.persistence.criteria.Predicate;
@Service
@Transactional
public class CinemaRoomServiceImpl implements CinemaRoomService {
    private final CinemaRoomRepository cinemaRoomRepository;
    private final SeatRepository seatRepository;

    public CinemaRoomServiceImpl(CinemaRoomRepository cinemaRoomRepository, SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    @Override
    public List<CinemaRoomDTO> findAll() {
       // Get List of entities
        var cinemaRooms = cinemaRoomRepository.findAll();

        // Convert entities to DTOs
        var CinemaRoomDTOs = cinemaRooms.stream().map(cinemaRoom -> {
            var CinemaRoomDTO = new CinemaRoomDTO();
            CinemaRoomDTO.setId(cinemaRoom.getId());
            CinemaRoomDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
            CinemaRoomDTO.setSeatQuantity(cinemaRoom.getSeatQuantity());
            return CinemaRoomDTO;
        }).toList();

        return CinemaRoomDTOs;
    }

    @Override
    public Page<CinemaRoomDTO> findAll(String keyword, Pageable pageable) {
        // Find category by keyword
        Specification<CinemaRoom> specification = (root, query, criteriaBuilder) -> {
            // Nếu keyword là null thì trả về null
            if (keyword == null) {
                return null;
            }

            // Chuyển đổi keyword thành Integer nếu có thể
            Integer seatQuantityKeyword;
            try {
                seatQuantityKeyword = Integer.valueOf(keyword);
            } catch (NumberFormatException e) {
                seatQuantityKeyword = null; // Không thể chuyển đổi thành Integer
            }

            // WHERE LOWER(cinemaRoomName) LIKE %keyword%
            Predicate namePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("cinemaRoomName")),
                "%" + keyword.toLowerCase() + "%"
            );

            Predicate seatQuantityPredicate = null;
            // Kiểm tra xem seatQuantityKeyword có khác null không
            if (seatQuantityKeyword != null) {
                // WHERE seatQuantity = seatQuantityKeyword
                seatQuantityPredicate = criteriaBuilder.equal(root.get("seatQuantity"), seatQuantityKeyword);
            }

            // Kết hợp các predicate
            if (seatQuantityPredicate != null) {
                return criteriaBuilder.or(namePredicate, seatQuantityPredicate);
            } else {
                return namePredicate; // Chỉ trả về namePredicate nếu seatQuantity không hợp lệ
            }
        };

        var cinemarooms = cinemaRoomRepository.findAll(specification, pageable);

        // Chuyển đổi Page<CinemaRoom> thành Page<CinemaRoomDTO>
        var cinemaRoomDTOs = cinemarooms.map(cinemaRoom -> {
            var cinemaRoomDTO = new CinemaRoomDTO();
            cinemaRoomDTO.setId(cinemaRoom.getId());
            cinemaRoomDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
            cinemaRoomDTO.setSeatQuantity(cinemaRoom.getSeatQuantity());
            return cinemaRoomDTO;
        });

        return cinemaRoomDTOs;
    }


    @Override
    public CinemaRoomDTO findById(UUID id) {
        // Get entity by id
        var cinemaRoom = cinemaRoomRepository.findById(id).orElse(null);
        // Check if entity is null then return null
        if (cinemaRoom == null) {
            return null;
        }
        // Convert entity to DTO
        var CinemaRoomDTO = new CinemaRoomDTO();
        CinemaRoomDTO.setId(cinemaRoom.getId());
        CinemaRoomDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
        CinemaRoomDTO.setSeatQuantity(cinemaRoom.getSeatQuantity());
        return CinemaRoomDTO;

    }

    @Override
    public CinemaRoomDTO create(CinemaRoomCreateDTO cinemaRoomCreateDTO) {
        // Check if cinemaRoomCreateDTO is null then throw exception
        if (cinemaRoomCreateDTO == null) {
            throw new IllegalArgumentException("CinemaRoomCreateDTO cannot be null");
        }
        // Check if cinemaroom with the same name exists
        var cinemaRoomWithName = cinemaRoomRepository.findByCinemaRoomName(cinemaRoomCreateDTO.getCinemaRoomName());
        if (cinemaRoomWithName != null) {
            throw new IllegalArgumentException("Cinema room with the same name already exists");
        }
        // Convert DTO to entity
        var cinemaRoom = new CinemaRoom();
        cinemaRoom.setCinemaRoomName(cinemaRoomCreateDTO.getCinemaRoomName());
        cinemaRoom.setSeatQuantity(cinemaRoomCreateDTO.getSeatQuantity());
        // Tạo ghế tự động
        cinemaRoom.generateSeats();
        // Save entity
        var savedCinemaRoom = cinemaRoomRepository.save(cinemaRoom);
        // Convert entity to DTO
        var CinemaRoomDTO = new CinemaRoomDTO();
        CinemaRoomDTO.setId(savedCinemaRoom.getId());
        CinemaRoomDTO.setCinemaRoomName(savedCinemaRoom.getCinemaRoomName());
        CinemaRoomDTO.setSeatQuantity(savedCinemaRoom.getSeatQuantity());
        return CinemaRoomDTO;
    }

    @Override
    public CinemaRoomDTO update(UUID id, CinemaRoomDTO cinemaRoomDTO) {
         // Check if CinemaRoomDTO is null then throw exception
        if (cinemaRoomDTO == null) {
            throw new IllegalArgumentException("CinemaRoomDTO cannot be null");
        }
        // Get entity by id
        var cinemaRoom = cinemaRoomRepository.findById(id).orElse(null);
        // Check if entity is null then return null
        if (cinemaRoom == null) {
            return null;
        }
        //check if cinemaroom with the same name exists
        var cinemaRoomWithName = cinemaRoomRepository.findByCinemaRoomName(cinemaRoomDTO.getCinemaRoomName());
        if (cinemaRoomWithName != null && !cinemaRoomWithName.getId().equals(id)) {
            throw new IllegalArgumentException("Cinema room with the same name already exists");
        }
        // Update entity
        cinemaRoom.setCinemaRoomName(cinemaRoomDTO.getCinemaRoomName());
        cinemaRoom.setSeatQuantity(cinemaRoomDTO.getSeatQuantity());
        // Save entity
        var savedCinemaRoom = cinemaRoomRepository.save(cinemaRoom);
        // Convert entity to DTO
        var CinemaRoomDTO = new CinemaRoomDTO();
        CinemaRoomDTO.setId(savedCinemaRoom.getId());
        return CinemaRoomDTO;
    }

    @Override
    public boolean deleteById(UUID id) {
          // Check if entity exists
        var cinemaRoom = cinemaRoomRepository.findById(id).orElse(null);
        // Check if entity is null then return false
        if (cinemaRoom == null) {
            return false;
        }
        // Delete entity
        cinemaRoomRepository.delete(cinemaRoom);
        // Check if entity is deleted
        var isDeleted = cinemaRoomRepository.findById(id).orElse(null) == null;
        return isDeleted;
    }

    @Override
    public List<Seat> getSeatsByCinemaRoomId(UUID cinemaRoomId) {
         var cinemaRoom = cinemaRoomRepository.findById(cinemaRoomId)
            .orElseThrow(() -> new IllegalArgumentException("CinemaRoom not found"));
        return new ArrayList<>(cinemaRoom.getSeats());
    }
    @Override
    public Boolean updateSeatTypes(List<Seat> selectedSeatsList) {
        try {
            for (Seat seat : selectedSeatsList) {
                // Lấy ghế hiện tại từ cơ sở dữ liệu (nếu cần) dựa trên ID
                Seat existingSeat = seatRepository.findById(seat.getId())
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seat.getId()));
                
                // Đổi trạng thái seatType
                existingSeat.setSeatType(!existingSeat.isSeatType());
        
                // Lưu lại thay đổi
                seatRepository.save(existingSeat);
            }
            return true; 
        } catch (Exception e) {
            
            e.printStackTrace(); 
            return false; 
        }
    }

    @Override
    public CinemaRoomDTO findByRoomName(String cinemaRoomName) {
        var cinemaRoom = cinemaRoomRepository.findByCinemaRoomName(cinemaRoomName);
        if (cinemaRoom == null) {
            return null;
        }
        var CinemaRoomDTO = new CinemaRoomDTO();
        CinemaRoomDTO.setId(cinemaRoom.getId());
        CinemaRoomDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
        CinemaRoomDTO.setSeatQuantity(cinemaRoom.getSeatQuantity());
        return CinemaRoomDTO;
    }

    @Override
    public List<Seat> getSeatsByCinemaRoomName(String cinemaRoomName) {
        var cinemaRoom = cinemaRoomRepository.findByCinemaRoomName(cinemaRoomName);
        if (cinemaRoom == null) {
            return null;
        }
        return new ArrayList<>(cinemaRoom.getSeats());
    }

    

}
