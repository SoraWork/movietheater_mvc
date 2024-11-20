package com.movietheater.movietheater_mvc.entities;

import java.util.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID movieId;

    @Column(name = "ACTOR")
    private String actor;

    @Column(name = "CINEMA_ROOM_ID")
    private UUID cinemaRoomId;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DIRECTOR")
    private String director;

    @Column(name = "DURATION")
    private int duration;

    @Column(name = "FROM_DATE")
    private LocalDate fromDate;

    @Column(name = "TO_DATE")
    private LocalDate toDate;

    @Column(name = "VERSION")
    private String version;

    @Column(name = "MOVIE_NAME_ENGLISH")
    private String movieNameEnglish;

    @Column(name = "MOVIE_NAME_VN")
    private String movieNameVn;

    @Column(name = "LARGE_IMAGE")
    private String largeImage;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "movie_schedule", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "schedule_id"))
    private Set<Schedule> schedules;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "movie_type", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<Type> types;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "movie_date", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "show_date_id"))
    private Set<ShowDate> showDates;

    public void generateShowDates() {
        if (fromDate != null && toDate != null) {
            Set<ShowDate> showDates = new HashSet<>();
    
            // Duyệt qua từng ngày từ fromDate đến toDate
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                ShowDate showDate = new ShowDate();
                showDate.setShowDate(date);
                showDate.setDateName(date.getDayOfWeek().name());
    
                showDates.add(showDate);
            }
    
            this.showDates = showDates; 
        }
    }

}
