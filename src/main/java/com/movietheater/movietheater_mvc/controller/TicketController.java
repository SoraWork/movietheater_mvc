package com.movietheater.movietheater_mvc.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.movietheater.movietheater_mvc.entities.Invoice;
import com.movietheater.movietheater_mvc.services.InvoiceService;

@Controller
@RequestMapping("/manage/tickets")
public class TicketController {
    private final InvoiceService invoiceService;

    public TicketController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String getAllBookings(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Invoice> invoices = (keyword == null || keyword.trim().isEmpty()) ? invoiceService.findAll(pageable)
                : invoiceService.searchInvoices(keyword, pageable);

        model.addAttribute("invoices", invoices.getContent());

        // Thêm các thuộc tính vào Model để sử dụng trong view
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", invoices.getTotalPages());
        model.addAttribute("totalElements", invoices.getTotalElements());
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageSizes", new Integer[] { 5, 10, 15, 20, 25 });
        model.addAttribute("pageLimit", 3);

        return "/manage/tickets/index";
    }

    @PostMapping("/update-status")
    public String updateInvoiceStatus(
            @RequestParam("invoiceId") UUID invoiceId,
            @RequestParam("status") boolean status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {

        invoiceService.updateStatus(invoiceId, status);

        String validKeyword = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword;

        return "redirect:/manage/tickets?keyword=" + validKeyword
                + "&page=" + page
                + "&size=" + size;
    }

    

}
