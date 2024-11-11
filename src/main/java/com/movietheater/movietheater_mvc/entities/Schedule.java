package com.movietheater.movietheater_mvc.entities;

import java.time.LocalTime;
import java.util.Set;
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

    @Column(name = "SCHEDULE_TIME")
    private String scheduleTime;

    @ManyToMany(mappedBy = "schedules", fetch = FetchType.LAZY)
    private Set<Movie> movies;

    public LocalTime getScheduleTimeAsLocalTime() {
        return LocalTime.parse(this.scheduleTime);
    }

}
