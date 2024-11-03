package com.movietheater.movietheater_mvc.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cinema_rooms")
public class CinemaRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "CINEMA_ROOM_NAME")
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY")
    private int seatQuantity;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats;

    public void generateSeats() {
        seats = new HashSet<>();
        int rows = 10; 
        String[] columns = {"A", "B", "C", "D", "E", "F"}; 
        for (int row = 1; row <= rows; row++) {
            for (String column : columns) {
                Seat seat = new Seat();
                seat.setSeatColumn(column);
                seat.setSeatRow(row);
                seat.setSeatStatus(true); 
                seat.setSeatType(row == 10); 
                seat.setCinemaRoom(this);
                seats.add(seat);
            }
        }
        this.seatQuantity = seats.size();
    }

}
