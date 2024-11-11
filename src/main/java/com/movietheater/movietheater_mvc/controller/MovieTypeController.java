package com.movietheater.movietheater_mvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.movietheater.movietheater_mvc.entities.Movie;
import com.movietheater.movietheater_mvc.services.MovieService;

@Controller
@RequestMapping("/movietype")
public class MovieTypeController {
    private final MovieService movieService;

    public MovieTypeController(MovieService movieService) {
        this.movieService = movieService;
    }
    @GetMapping("/genre/{genreName}")
    public String showMoviesByGenre(@PathVariable String genreName, Model model) {
        List<Movie> movies = movieService.getMoviesByTypeName(genreName);
        model.addAttribute("movies", movies);
        return "movietype/genre"; 
    }

}
