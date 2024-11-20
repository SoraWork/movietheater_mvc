package com.movietheater.movietheater_mvc.dtos.ticket;



import com.movietheater.movietheater_mvc.dtos.movie.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private MovieDTO movieDTO;
    private String selectedSeats;
    private int totalPrice;   
    private String schedule;
    private String time;

    
}
