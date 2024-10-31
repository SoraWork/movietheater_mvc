package com.movietheater.movietheater_mvc.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ADD_SCORE")
    private int addScore;

    @Column(name = "BOOKING_DATE")
    private LocalDate bookingDate;

    @Column(name = "MOVIE_NAME")
    private String movieName;

    @Column(name = "SCHEDULE_SHOW")
    private LocalDate scheduleShow;

    @Column(name = "SCHEDULE_SHOW_TIME")
    private String scheduleShowTime;

    @Column(name = "TOTAL_MONEY")
    private BigDecimal totalMoney;

    @Column(name = "USE_SCORE")
    private int useScore;

    @Column(name = "SEAT")
    private String seat;
    
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id")
    private Account account;
}
