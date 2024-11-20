package com.movietheater.movietheater_mvc.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.movietheater.movietheater_mvc.entities.Movie;
import com.movietheater.movietheater_mvc.entities.Schedule;
import com.movietheater.movietheater_mvc.services.CinemaRoomService;
import com.movietheater.movietheater_mvc.services.MemberService;
import com.movietheater.movietheater_mvc.services.MovieService;
import com.movietheater.movietheater_mvc.services.ShowDateService;



@Controller
@RequestMapping("/")
public class HomeController {
    private final MovieService movieService;
  

    public HomeController(MovieService movieService, ShowDateService showDateService, CinemaRoomService cinemaRoomService, MemberService memberService) {
        this.movieService = movieService;
    }
    @GetMapping
    public String index(Model model) {
       var movies = movieService.findAll();
       model.addAttribute("movies", movies);
        return "home/index";
    } 

    @GetMapping("about")
    public String about() {
        return "home/about";
    }

    @GetMapping("contact")
    public String contact() {
        return "home/contact";
    }

    @GetMapping("/showtime")
    public String showtimes(Model model) {
        // Tạo danh sách ngày chiếu (showDates)
        List<LocalDate> showDates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            showDates.add(today.plusDays(i));  // Thêm các ngày chiếu vào danh sách
        }

        // Tạo bản đồ các bộ phim theo từng ngày chiếu
        Map<LocalDate, Set<Movie>> moviesByDate = new LinkedHashMap<>();
        for (LocalDate date : showDates) {
            Set<Movie> uniqueMovies = new LinkedHashSet<>(movieService.getMoviesByShowDate(date));
            moviesByDate.put(date, uniqueMovies);  // Lưu các bộ phim cho từng ngày chiếu
        }

        // Định dạng lại danh sách các ngày chiếu theo định dạng đẹp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        List<String> formattedDates = showDates.stream()
                                                .map(date -> date.format(formatter))
                                                .collect(Collectors.toList());

        // Sắp xếp lịch chiếu của từng bộ phim theo giờ
        for (Set<Movie> movies : moviesByDate.values()) {
            for (Movie movie : movies) {
                Set<Schedule> sortedSchedules = movie.getSchedules().stream()
                    .sorted(Comparator.comparing(schedule -> LocalTime.parse(schedule.getScheduleTime())))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
                movie.setSchedules(sortedSchedules);  // Cập nhật lại danh sách lịch chiếu của bộ phim
            }
        }

        // Truyền các giá trị vào Model để sử dụng trong HTML
        model.addAttribute("showDates", showDates);         // Ngày chiếu
        model.addAttribute("moviesByDate", moviesByDate);   // Các bộ phim theo ngày
        model.addAttribute("formattedDates", formattedDates);  // Ngày chiếu đã định dạng

        return "home/showtime";  // Trả về tên trang view
    }

}
