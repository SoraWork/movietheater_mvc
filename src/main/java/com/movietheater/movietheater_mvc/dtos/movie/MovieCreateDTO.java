package com.movietheater.movietheater_mvc.dtos.movie;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomDTO;
import com.movietheater.movietheater_mvc.dtos.schedule.ScheduleDTO;
import com.movietheater.movietheater_mvc.dtos.type.TypeDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieCreateDTO {
    @NotBlank
    @Length(min = 2, max = 50, message = "Movie name must be between 2 and 50 characters")
    private String movieNameEnglish;

    @NotBlank
    @Length(min = 2, max = 50, message = "Movie name must be between 2 and 50 characters")
    private String movieNameVn;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotBlank
    @Length(max =255, message = "Actor must be less than 255 characters")
    private String actor;

    @NotBlank
    @Length(min = 2, max = 50, message = "Director must be between 2 and 50 characters")
    private String director;

    @NotNull
    private int duration;

    @NotBlank
    @Length(min = 2, max = 50, message = "Version must be between 2 and 50 characters")
    private String version;

    @NotEmpty(message = "Type is required")
    @Valid
    private Set<@NotBlank(message = "Type cannot be blank") String> typeName;

    private Set<TypeDTO> types;

    @NotEmpty(message = "Cinema Room is required")
    private String cinemaRoomName;
    
    private CinemaRoomDTO cinemaRooms;

    @NotEmpty(message = "Schedule is required")
    @Valid
    private Set<@NotBlank(message = "Schedule cannot be blank") String> scheduleTime;

    private Set<ScheduleDTO> schedules;

    @Length(max = 255, message = "Content must be less than 255 characters")
    private String content;

    @Length(max = 255, message = "Image must be less than 255 characters")
    private String largeImage;


}
