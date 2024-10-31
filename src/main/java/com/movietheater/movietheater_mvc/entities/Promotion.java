package com.movietheater.movietheater_mvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "DETAIL")
    private String detail;

    @Column(name = "DISCOUNT_LEVEL")
    private int discountLevel;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "TITLE")
    private String title;
}
