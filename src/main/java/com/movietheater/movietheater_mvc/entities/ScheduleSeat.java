package com.movietheater.movietheater_mvc.entities;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_seats")
public class ScheduleSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "MOVIE_ID")
    private UUID movieId;

    @Column(name = "SCHEDULE_ID")
    private UUID scheduleId;

    @Column(name = "SEAT_ID")
    private UUID seatId;

    @Column(name = "SEAT_COLUMN")
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private int seatRow;

    @Column(name = "SEAT_STATUS")
    private boolean seatStatus;

    @Column(name = "SEAT_TYPE")
    private boolean seatType;
}
