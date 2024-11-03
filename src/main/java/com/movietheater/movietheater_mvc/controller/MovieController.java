package com.movietheater.movietheater_mvc.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movietheater.movietheater_mvc.dtos.messages.Message;
import com.movietheater.movietheater_mvc.dtos.movie.MovieCreateDTO;
import com.movietheater.movietheater_mvc.dtos.movie.MovieDTO;
import com.movietheater.movietheater_mvc.dtos.schedule.ScheduleDTO;
import com.movietheater.movietheater_mvc.dtos.type.TypeDTO;
import com.movietheater.movietheater_mvc.entities.Schedule;
import com.movietheater.movietheater_mvc.repositories.ScheduleRepository;
import com.movietheater.movietheater_mvc.repositories.TypeRepository;
import com.movietheater.movietheater_mvc.services.CinemaRoomService;
import com.movietheater.movietheater_mvc.services.MovieService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manage/movies")
public class MovieController {
    private final MovieService movieService;
    private final CinemaRoomService cinemaRoomService;
    private final TypeRepository typeRepository;
    private final ScheduleRepository scheduleRepository;

    public MovieController(MovieService movieService, CinemaRoomService cinemaRoomService, TypeRepository typeRepository, ScheduleRepository scheduleRepository) {
        this.typeRepository = typeRepository;
        this.scheduleRepository = scheduleRepository;
        this.cinemaRoomService = cinemaRoomService;
        this.movieService = movieService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "movieNameEnglish") String sortBy, // Xac dinh truong sap xep
            @RequestParam(required = false, defaultValue = "asc") String order, // Xac dinh chieu sap xep
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            Model model) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        // Search recipe by keyword and paging
        var movies = movieService.findAll(keyword, pageable);
        model.addAttribute("movies", movies);

        // Passing keyword to view
        model.addAttribute("keyword", keyword);

        // Passing total pages to view
        model.addAttribute("totalPages", movies.getTotalPages());

        // Passing total elements to view
        model.addAttribute("totalElements", movies.getTotalElements());

        // Passing current sortBy to view
        model.addAttribute("sortBy", sortBy);

        // Passing current order to view
        model.addAttribute("order", order);

        // Limit page
        model.addAttribute("pageLimit", 3);

        // Passing current page to view
        model.addAttribute("page", page);

        // Passing current size to view
        model.addAttribute("pageSize", size);

        // Passing pageSizes to view
        model.addAttribute("pageSizes", new Integer[] { 5, 10, 15, 20, 25 });

        // Get message from redirect
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", new Message());
        }
        return "manage/movies/index";
    }



    @GetMapping("/create")
    public String create(Model model) {
        var movieCreateDTO = new MovieCreateDTO();
        model.addAttribute("movieCreateDTO", movieCreateDTO);

        var cinemarooms=cinemaRoomService.findAll();
        model.addAttribute("cinemarooms", cinemarooms);

        var types=typeRepository.findAll();
        model.addAttribute("types", types);

        var schedules=scheduleRepository.findAll();
        schedules.sort(Comparator.comparing(Schedule::getScheduleTime));  
        model.addAttribute("schedules", schedules);

        return "manage/movies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid MovieCreateDTO movieCreateDTO,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieCreateDTO", movieCreateDTO);

            var cinemarooms=cinemaRoomService.findAll();
            model.addAttribute("cinemarooms", cinemarooms);

            var types=typeRepository.findAll();
            model.addAttribute("types", types);

            var schedules=scheduleRepository.findAll();
            model.addAttribute("schedules", schedules);
            return "manage/movies/create";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                // Create folder if not exist following format:
                // src/main/resources/static/images/movies/year/month/day
                LocalDateTime date = LocalDateTime.now();
                Path folder = Paths.get("src/main/resources/static/images/movies/" + date.getYear() + "/"
                        + date.getMonthValue() + "/" + date.getDayOfMonth());
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                // Create file name following format: originalFileName + epochTime + extension
                String originalFileName = imageFile.getOriginalFilename();
                // Convert date to string epoch time
                Long epochTime = Instant.now().getEpochSecond();
                String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "-" + epochTime
                        + originalFileName.substring(originalFileName.lastIndexOf("."));
                Path path = Paths.get(folder.toString(), fileName);
                Files.write(path, bytes);
                movieCreateDTO.setLargeImage(folder.toString().replace("src\\main\\resources\\static", "") + "/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                Message errorMessage = new Message("error", "Failed to upload image");
                model.addAttribute("message", errorMessage);
                var cinemarooms=cinemaRoomService.findAll();
                model.addAttribute("cinemarooms", cinemarooms);
    
                var types=typeRepository.findAll();
                model.addAttribute("types", types);
    
                var schedules=scheduleRepository.findAll();
                model.addAttribute("schedules", schedules);

                bindingResult.rejectValue("image", "image", "Failed to upload image");
                return "manage/movies/create";
            }
        }

        var result = movieService.create(movieCreateDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to create movie");
            model.addAttribute("message", errorMessage);

            var cinemarooms=cinemaRoomService.findAll();
            model.addAttribute("cinemarooms", cinemarooms);

            var types=typeRepository.findAll();
            model.addAttribute("types", types);

            var schedules=scheduleRepository.findAll();
            model.addAttribute("schedules", schedules);
            return "manage/movies/create";
        }

        var successMessage = new Message("success", "Movie created successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/movies";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var movieDTO = movieService.findById(id);
        List<String> typeNames = movieDTO.getTypes().stream()
        .map(TypeDTO::getTypeName)
        .collect(Collectors.toList());
        model.addAttribute("selectedTypeNames", typeNames);

        model.addAttribute("movieDTO", movieDTO);

        var cinemarooms=cinemaRoomService.findAll();
        model.addAttribute("cinemarooms", cinemarooms);

        var types=typeRepository.findAll();
        model.addAttribute("types", types);

        var schedules=scheduleRepository.findAll();
        List<String> scheduleTimes = movieDTO.getSchedules().stream()
            .map(ScheduleDTO::getScheduleTime)
            .collect(Collectors.toList());
        model.addAttribute("selectedScheduleTimes", scheduleTimes);
        schedules.sort(Comparator.comparing(Schedule::getScheduleTime));  
        model.addAttribute("schedules", schedules);

        return "manage/movies/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id,
            @ModelAttribute @Valid MovieDTO movieDTO,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieDTO", movieDTO);

            var cinemarooms=cinemaRoomService.findAll();
            model.addAttribute("cinemarooms", cinemarooms);

            var types=typeRepository.findAll();
            model.addAttribute("types", types);

            var schedules=scheduleRepository.findAll();
            model.addAttribute("schedules", schedules);

            return "manage/movies/edit";
        }

        var oldMovie = movieService.findById(id);

        if (imageFile.getOriginalFilename().isEmpty()) {
            movieDTO.setLargeImage(oldMovie.getLargeImage());
        } else {
            try {
                byte[] bytes = imageFile.getBytes();
                // Create folder if not exist following format:
                // src/main/resources/static/images/movies/year/month/day
                LocalDateTime date = LocalDateTime.now();
                Path folder = Paths.get("src/main/resources/static/images/movies/" + date.getYear() + "/"
                        + date.getMonthValue() + "/" + date.getDayOfMonth());
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                // Create file name following format: originalFileName + epochTime + extension
                String originalFileName = imageFile.getOriginalFilename();
                // Convert date to string epoch time
                Long epochTime = Instant.now().getEpochSecond();
                String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "-" + epochTime
                        + originalFileName.substring(originalFileName.lastIndexOf("."));
                Path path = Paths.get(folder.toString(), fileName);
                Files.write(path, bytes);
                movieDTO.setLargeImage(folder.toString().replace("src\\main\\resources\\static", "") + "/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                Message errorMessage = new Message("error", "Failed to upload image");
                model.addAttribute("message", errorMessage);

                var cinemarooms=cinemaRoomService.findAll();
                model.addAttribute("cinemarooms", cinemarooms);
    
                var types=typeRepository.findAll();
                model.addAttribute("types", types);
    
                var schedules=scheduleRepository.findAll();
                model.addAttribute("schedules", schedules);

                bindingResult.rejectValue("image", "image", "Failed to upload image");
                return "manage/movies/edit";
            }
        }

        var result = movieService.update(id, movieDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to update movie");
            model.addAttribute("message", errorMessage);

            var cinemarooms=cinemaRoomService.findAll();
                model.addAttribute("cinemarooms", cinemarooms);
    
            var types=typeRepository.findAll();
            model.addAttribute("types", types);
    
            var schedules=scheduleRepository.findAll();
            model.addAttribute("schedules", schedules);

            return "manage/movies/edit";

        }

        var successMessage = new Message("success", "Movie updated successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/movies";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        var result = movieService.deleteById(id);

        if (!result) {
            var errorMessage = new Message("error", "Failed to delete movie");
            redirectAttributes.addFlashAttribute("message", errorMessage);
        } else {
            var successMessage = new Message("success", "Movie deleted successfully");
            redirectAttributes.addFlashAttribute("message", successMessage);
        }
        return "redirect:/manage/movies";
    }
}
