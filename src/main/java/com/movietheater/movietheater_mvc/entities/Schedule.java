package com.movietheater.movietheater_mvc.entities;

import java.util.List;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "SCHEDULE_TIMW")
    private String scheduleTime;

    @ManyToMany(mappedBy = "schedules", fetch = FetchType.LAZY)
    private List<Movie> movies;

}
