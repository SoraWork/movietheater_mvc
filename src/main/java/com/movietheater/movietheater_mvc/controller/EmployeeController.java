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

import com.movietheater.movietheater_mvc.dtos.employee.EmployeeCreateDTO;
import com.movietheater.movietheater_mvc.dtos.employee.EmployeeDTO;
import com.movietheater.movietheater_mvc.dtos.messages.Message;
import com.movietheater.movietheater_mvc.services.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manage/employees")
public class EmployeeController {  

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    // @GetMapping
    // public String index(
    //         @RequestParam(required = false) String keyword,
    //         @RequestParam(required = false, defaultValue = "") String sortBy, 
    //         @RequestParam(required = false, defaultValue = "asc") String order, 
    //         @RequestParam(required = false, defaultValue = "0") Integer page,
    //         @RequestParam(required = false, defaultValue = "10") Integer size,
    //         Model model) {
    //     Pageable pageable = null;

    //     if (order.equals("asc")) {
    //         pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
    //     } else {
    //         pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
    //     }

    //     // Search category by keyword and paging
    //     var employees = employeeService.findAll(keyword, pageable);
    //     model.addAttribute("employees", employees);

    //     // Passing keyword to view
    //     model.addAttribute("keyword", keyword);

    //     // Passing total pages to view
    //     model.addAttribute("totalPages", employees.getTotalPages());

    //     // Passing total elements to view
    //     model.addAttribute("totalElements", employees.getTotalElements());

    //     // Passing current sortBy to view
    //     model.addAttribute("sortBy", sortBy);

    //     // Passing current order to view
    //     model.addAttribute("order", order);

    //     // Limit page
    //     model.addAttribute("pageLimit", 3);

    //     // Passing current page to view
    //     model.addAttribute("page", page);

    //     // Passing current size to view
    //     model.addAttribute("pageSize", size);

    //     // Passing pageSizes to view
    //     model.addAttribute("pageSizes", new Integer[] { 10, 20, 30, 50, 100 });

    //     // Get message from redirect
    //     if (!model.containsAttribute("message")) {
    //         model.addAttribute("message", new Message());
    //     }
    //     return "manage/employees/index";
    // }

    @GetMapping
    public String index(Model model) {
        var employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "manage/employees/index";
    }


    @GetMapping("/create")
    public String create(Model model) {
        var employeeCreateDTO = new EmployeeCreateDTO();
        model.addAttribute("employeeCreateDTO", employeeCreateDTO);

        return "manage/employees/create";
    }

     @PostMapping("/create")
    public String create(@Valid @ModelAttribute EmployeeCreateDTO employeeCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeCreateDTO", employeeCreateDTO);
            return "manage/categories/create";
        }

        var result = employeeService.create(employeeCreateDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to create employee");
            model.addAttribute("message", errorMessage);
            return "manage/employees/create";
        }

        var successMessage = new Message("success", "Employee created successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/employees";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var employeeDTO = employeeService.findById(id);
        model.addAttribute("employeeDTO", employeeDTO);

        return "manage/employees/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id,
            @ModelAttribute @Valid EmployeeDTO employeeDTO,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeDTO", employeeDTO);
            return "manage/employees/edit";
        }

        var result = employeeService.update(id, employeeDTO);

        if (result == null) {
            var errorMessage = new Message("error", "Failed to update employee");
            model.addAttribute("message", errorMessage);
            return "manage/employees/edit";

        }

        var successMessage = new Message("success", "employee updated successfully");
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/manage/employees";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        var result = employeeService.deleteById(id);

        if (!result) {
            var errorMessage = new Message("error", "Failed to delete employee");
            redirectAttributes.addFlashAttribute("message", errorMessage);
        } else {
            var successMessage = new Message("success", "employee deleted successfully");
            redirectAttributes.addFlashAttribute("message", successMessage);
        }
        return "redirect:/manage/employees";
    }
}
