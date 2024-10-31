package com.movietheater.movietheater_mvc.entities;

import java.util.List;
import java.util.UUID;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "show_dates")
public class ShowDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "SHOW_DATE")
    private LocalDate showDate;

    @Column(name = "DATE_NAME")
    private String dateName;

    @ManyToMany(mappedBy = "showDates", fetch = FetchType.LAZY)
    private List<Movie> movies;

}
