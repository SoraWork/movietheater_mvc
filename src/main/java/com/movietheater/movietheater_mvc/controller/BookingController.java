package com.movietheater.movietheater_mvc.controller;

import java.time.LocalDate;
import java.util.List;
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
    private final ShowDateService showDateService;
    private final CinemaRoomService cinemaRoomService;
    private final MemberService memberService;
    private final BookingService bookingService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
  

    public BookingController(MovieService movieService, ShowDateService showDateService, CinemaRoomService cinemaRoomService, 
        MemberService memberService, BookingService bookingService,     
        ScheduleService scheduleService, SeatService seatService) {
        this.scheduleService = scheduleService;
        this.seatService = seatService;
        this.bookingService = bookingService;
        this.memberService = memberService;
        this.cinemaRoomService = cinemaRoomService;
        this.showDateService = showDateService;
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

        // Nếu không có date, sử dụng ngày hôm nay
        if (date == null || date.isEmpty()) {
            LocalDate today = LocalDate.now();
            date = today.toString(); // Format mặc định là yyyy-MM-dd
        }

        String cinemaRoomName = movie.getCinemaRoomName();
        var cinema = cinemaRoomService.findByRoomName(cinemaRoomName);
        
        // Lấy danh sách ghế trong phòng chiếu đó
        List<Seat> seats = cinemaRoomService.getSeatsByCinemaRoomName(cinemaRoomName);
        
        model.addAttribute("seats", seats);
        model.addAttribute("cinema", cinema);
        model.addAttribute("scheduleTime", scheduleTime);
        model.addAttribute("date", date); // Gán lại giá trị date vào model
        
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
        // Lấy thông tin người dùng
        String currentUsername = userDetails.getUsername();
        Member member = memberService.findByUsername(currentUsername);
        model.addAttribute("member", member);

        // Lấy thông tin phim theo movieId
        var movie = movieService.findById(movieId);

        // Tạo đối tượng chứa thông tin mua vé (có thể là một DTO)
        TicketDTO ticketDTO = new TicketDTO(movie, selectedSeats, totalPrice, schedule, time);

        // Thêm thông tin vào model để hiển thị trên trang buy
        model.addAttribute("ticket", ticketDTO);

        // Trả về trang buy để hiển thị thông tin vé
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

        // Chuyển đổi movieId từ String sang UUID
        UUID movieUuid = UUID.fromString(movieId);

    

        // Lấy thông tin người dùng từ CustomUserDetails
        String currentUsername = userDetails.getUsername();
        Member member = memberService.findByUsername(currentUsername);
            

        // Thực hiện gọi service để đặt vé (Giả sử bạn đã tạo service)
        boolean success = bookingService.bookTickets(movieUuid, selectedSeats, totalPrice, schedule, time, currentUsername);

        // Thêm thông báo thành công hoặc thất bại vào model
        if (success) {
            model.addAttribute("message", "Booking successful!");
        } else {
            model.addAttribute("message", "Booking failed!");
        }

        // Chuyển hướng đến trang xác nhận
        return "home/bookingConfirmation";
    }

}
