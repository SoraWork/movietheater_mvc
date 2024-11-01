package com.movietheater.movietheater_mvc.dtos.cinemaroom;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaRoomDTO {

    private UUID id;
    @NotBlank
    @Length(min = 3, max = 50, message = "Cinema room name must be between 3 and 50 characters")
    private String cinemaRoomName;
    @Min(20)
    @Max(500)
    @NotNull
    private int seatQuantity;

}
