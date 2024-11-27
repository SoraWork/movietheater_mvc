package com.movietheater.movietheater_mvc.controller;

import java.util.*;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomCreateDTO;
import com.movietheater.movietheater_mvc.dtos.cinemaroom.CinemaRoomDTO;
import com.movietheater.movietheater_mvc.dtos.messages.Message;
import com.movietheater.movietheater_mvc.entities.Seat;
import com.movietheater.movietheater_mvc.repositories.SeatRepository;
import com.movietheater.movietheater_mvc.services.CinemaRoomService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manage/cinemarooms")
public class CinemaRoomController {

    private final CinemaRoomService cinemaRoomService;
    private final SeatRepository seatRepository;

    public CinemaRoomController(CinemaRoomService cinemaRoomService, SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
        this.cinemaRoomService = cinemaRoomService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "cinemaRoomName") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            Model model) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        var cinemaRooms = cinemaRoomService.findAll(keyword, pageable);
        model.addAttribute("cinemaRooms", cinemaRooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", cinemaRooms.getTotalPages());

        model.addAttribute("totalElements", cinemaRooms.getTotalElements());

        model.addAttribute("sortBy", sortBy);

        model.addAttribute("order", order);

        model.addAttribute("pageLimit", 3);

        model.addAttribute("page", page);

        model.addAttribute("pageSize", size);

        model.addAttribute("pageSizes", new Integer[] { 5, 10, 15, 20, 25 });

        if (!model.containsAttribute("message")) {
            model.addAttribute("message", new Message());
        }
        return "manage/cinemarooms/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        var cinemaRoomCreateDTO = new CinemaRoomCreateDTO();
        model.addAttribute("cinemaRoomCreateDTO", cinemaRoomCreateDTO);

        return "manage/cinemarooms/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute CinemaRoomCreateDTO cinemaRoomCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cinemaRoomCreateDTO", cinemaRoomCreateDTO);
            return "manage/cinemarooms/create";
        }

        var result = cinemaRoomService.create(cinemaRoomCreateDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to create cinema room");
            model.addAttribute("message", errorMessage);
            return "manage/cinemarooms/create";
        }

        var successMessage = new Message("success", "Cinema room created successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/cinemarooms";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var cinemaRoomDTO = cinemaRoomService.findById(id);
        model.addAttribute("cinemaRoomDTO", cinemaRoomDTO);
        return "manage/cinemarooms/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id,
            @ModelAttribute @Valid CinemaRoomDTO cinemaRoomDTO,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cinemaRoomDTO", cinemaRoomDTO);
            return "manage/cinemarooms/edit";
        }

        var result = cinemaRoomService.update(id, cinemaRoomDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to update cinema room");
            model.addAttribute("message", errorMessage);
            return "manage/cinemarooms/edit";

        }

        var successMessage = new Message("success", "Cinema room updated successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/cinemarooms";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        var result = cinemaRoomService.deleteById(id);

        if (!result) {
            var errorMessage = new Message("error", "Failed to delete cinema room");
            redirectAttributes.addFlashAttribute("message", errorMessage);
        } else {
            var successMessage = new Message("success", "Cinema room deleted successfully");
            redirectAttributes.addFlashAttribute("message", successMessage);
        }
        return "redirect:/manage/cinemarooms";
    }

    @GetMapping("/{id}")
    public String showCinemaRoomDetail(@PathVariable UUID id, Model model) {

        CinemaRoomDTO cinemaRoom = cinemaRoomService.findById(id);

        List<Seat> seats = cinemaRoomService.getSeatsByCinemaRoomId(id);

        model.addAttribute("cinemaRoom", cinemaRoom);
        model.addAttribute("seats", seats);

        return "manage/cinemarooms/detail";
    }

    @PostMapping("/{id}")
    public String handleSelectedSeats(@PathVariable UUID id,
            @RequestParam("selectedSeats") String selectedSeats,
            Model model,
            RedirectAttributes redirectAttributes) {

        String[] seatIds = selectedSeats.split(",");

        List<Seat> selectedSeatsList = new ArrayList<>();

        for (String seatId : seatIds) {
            if (!seatId.isEmpty()) {

                Seat existingSeat = seatRepository.findById(UUID.fromString(seatId))
                        .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));

                selectedSeatsList.add(existingSeat);
            }
        }

        boolean result = cinemaRoomService.updateSeatTypes(selectedSeatsList);

        if (result == false) {
            var errorMessage = new Message("error", "Failed to update seat types");
            model.addAttribute("message", errorMessage);
            return "manage/cinemarooms/create";
        }

        var successMessage = new Message("success", "Seat types updated successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);

        return "redirect:/manage/cinemarooms";
    }

}
