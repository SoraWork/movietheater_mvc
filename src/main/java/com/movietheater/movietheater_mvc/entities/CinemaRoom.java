package com.movietheater.movietheater_mvc.entities;

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

    @OneToMany(mappedBy = "cinemaRoom")
    private Set<Seat> seats;

}
