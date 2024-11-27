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

    public HomeController(MovieService movieService, ShowDateService showDateService,
            CinemaRoomService cinemaRoomService, MemberService memberService) {
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

        List<LocalDate> showDates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            showDates.add(today.plusDays(i));
        }

        Map<LocalDate, Set<Movie>> moviesByDate = new LinkedHashMap<>();
        for (LocalDate date : showDates) {
            Set<Movie> uniqueMovies = new LinkedHashSet<>(movieService.getMoviesByShowDate(date));
            moviesByDate.put(date, uniqueMovies);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        List<String> formattedDates = showDates.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());

        for (Set<Movie> movies : moviesByDate.values()) {
            for (Movie movie : movies) {
                Set<Schedule> sortedSchedules = movie.getSchedules().stream()
                        .sorted(Comparator.comparing(schedule -> LocalTime.parse(schedule.getScheduleTime())))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                movie.setSchedules(sortedSchedules);
            }
        }

        model.addAttribute("showDates", showDates);
        model.addAttribute("moviesByDate", moviesByDate);
        model.addAttribute("formattedDates", formattedDates);

        return "home/showtime";
    }

}
