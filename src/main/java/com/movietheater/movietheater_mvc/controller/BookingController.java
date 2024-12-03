package com.movietheater.movietheater_mvc.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movietheater.movietheater_mvc.dtos.ticket.TicketDTO;
import com.movietheater.movietheater_mvc.entities.Member;
import com.movietheater.movietheater_mvc.entities.ScheduleSeat;
import com.movietheater.movietheater_mvc.entities.Seat;
import com.movietheater.movietheater_mvc.services.BookingService;
import com.movietheater.movietheater_mvc.services.CinemaRoomService;
import com.movietheater.movietheater_mvc.services.MemberService;
import com.movietheater.movietheater_mvc.services.MovieService;
import com.movietheater.movietheater_mvc.services.ScheduleService;
import com.movietheater.movietheater_mvc.services.SeatService;
import com.movietheater.movietheater_mvc.services.ShowDateService;
import com.movietheater.movietheater_mvc.services.impl.util.CustomUserDetails;

@Controller
public class BookingController {
    private final MovieService movieService;
    private final CinemaRoomService cinemaRoomService;
    private final MemberService memberService;
    private final BookingService bookingService;

    public BookingController(MovieService movieService, ShowDateService showDateService,
            CinemaRoomService cinemaRoomService,
            MemberService memberService, BookingService bookingService,
            ScheduleService scheduleService, SeatService seatService) {
        this.bookingService = bookingService;
        this.memberService = memberService;
        this.cinemaRoomService = cinemaRoomService;
        this.movieService = movieService;
    }

    @GetMapping("/movies/{id}")
    public String showMovie(
            @PathVariable UUID id,
            Model model,
            @RequestParam(value = "time", required = false, defaultValue = "defaultTime") String scheduleTime,
            @RequestParam(value = "date", required = false) String date) {

        var movie = movieService.findById(id);
        model.addAttribute("movie", movie);

        if (date == null || date.isEmpty()) {
            LocalDate today = LocalDate.now();
            date = today.toString();
        }
        String cinemaRoomName = movie.getCinemaRoomName();
        var cinema = cinemaRoomService.findByRoomName(cinemaRoomName);

        List<Seat> seats = cinemaRoomService.getSeatsByCinemaRoomName(cinemaRoomName);

        List<ScheduleSeat> bookedSeats = bookingService.findBookedSeatsByMovieAndScheduleAndDate(
                id,
                scheduleTime,
                LocalDate.parse(date));

        Map<String, Boolean> seatStatusMap = new HashMap<>();
        for (Seat seat : seats) {
            String seatKey = seat.getSeatRow() + seat.getSeatColumn();
            seatStatusMap.put(seatKey, false);
        }
        for (ScheduleSeat bookedSeat : bookedSeats) {
            String seatKey = bookedSeat.getSeatRow() + bookedSeat.getSeatColumn();
            seatStatusMap.put(seatKey, true);
        }
        System.out.println(seats);
        System.out.println(seatStatusMap);

        model.addAttribute("seats", seats);
        model.addAttribute("seatStatusMap", seatStatusMap);
        model.addAttribute("cinema", cinema);
        model.addAttribute("scheduleTime", scheduleTime);
        model.addAttribute("date", date);

        return "home/movie";
    }

    @PostMapping("/movies/buy")
    public String processBuyTicket(
            @RequestParam UUID movieId,
            @RequestParam String selectedSeats,
            @RequestParam int totalPrice,
            @RequestParam String schedule,
            @RequestParam String time,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        String currentUsername = userDetails.getUsername();
        Member member = memberService.findByUsername(currentUsername);
        model.addAttribute("member", member);

        var movie = movieService.findById(movieId);

        TicketDTO ticketDTO = new TicketDTO(movie, selectedSeats, totalPrice, schedule, time);

        model.addAttribute("ticket", ticketDTO);

        return "home/buy";
    }

    @PostMapping("/movies/confirmbooking")
    public String confirmBooking(
            @RequestParam String movieId,
            @RequestParam String schedule,
            @RequestParam String time,
            @RequestParam String selectedSeats,
            @RequestParam int totalPrice,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        UUID movieUuid = UUID.fromString(movieId);

        String currentUsername = userDetails.getUsername();

        boolean success = bookingService.bookTickets(movieUuid, selectedSeats, totalPrice, schedule, time,
                currentUsername);

        if (success) {
            model.addAttribute("message", "Booking successful!");
        } else {
            model.addAttribute("message", "Booking failed!");
        }

        return "redirect:/manager/histories";
    }

}
