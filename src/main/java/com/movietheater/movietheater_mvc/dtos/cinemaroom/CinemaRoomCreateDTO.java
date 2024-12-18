package com.movietheater.movietheater_mvc.dtos.cinemaroom;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaRoomCreateDTO {

    @NotBlank
    @Length(min = 3, max = 50, message = "Cinema room name must be between 3 and 50 characters")
    private String cinemaRoomName;
   
    private int seatQuantity;

}