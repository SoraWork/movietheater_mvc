package com.movietheater.movietheater_mvc.entities;

import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "SEAT_COLUMN")
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private int seatRow;

    @Column(name = "SEAT_STATUS")
    private boolean seatStatus;

    @Column(name = "SEAT_TYPE")
    private boolean seatType;

    @ManyToOne
    @JoinColumn(name = "cinema_room_id", referencedColumnName = "id")
    private CinemaRoom cinemaRoom;

    // Constructor cho Seat
    public Seat(int row, char column) {
        this.seatRow = row;
        this.seatColumn = String.valueOf(column); 
    }

}
