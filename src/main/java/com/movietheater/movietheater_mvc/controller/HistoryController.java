package com.movietheater.movietheater_mvc.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movietheater.movietheater_mvc.dtos.invoice.InvoiceDTO;
import com.movietheater.movietheater_mvc.dtos.messages.Message;
import com.movietheater.movietheater_mvc.entities.Member;
import com.movietheater.movietheater_mvc.services.InvoiceService;
import com.movietheater.movietheater_mvc.services.MemberService;
import com.movietheater.movietheater_mvc.services.impl.util.CustomUserDetails;

@Controller
@RequestMapping("/manager/histories")
public class HistoryController {

    private final MemberService memberService;
    private final InvoiceService invoiceService;

    public HistoryController(InvoiceService invoiceService, MemberService memberService) {
        this.memberService = memberService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String history(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String currentUsername = userDetails.getUsername();
        Member member = memberService.findByUsername(currentUsername);

        if (member == null) {
            return "error/404";
        }

        UUID accountId = member.getAccount().getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDTO> invoices = invoiceService.findByMemberIdAndKeyword(accountId, keyword, pageable);

        model.addAttribute("invoices", invoices);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", invoices.getTotalPages());
        model.addAttribute("totalElements", invoices.getTotalElements());
        model.addAttribute("pageLimit", 3);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageSizes", new Integer[] { 5, 10, 15, 20, 25 });

        return "manager/histories/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        var result = invoiceService.deleteById(id);

        if (!result) {
            var errorMessage = new Message("error", "Failed to delete employee");
            redirectAttributes.addFlashAttribute("message", errorMessage);
        } else {
            var successMessage = new Message("success", "employee deleted successfully");
            redirectAttributes.addFlashAttribute("message", successMessage);
        }
        return "redirect:/manager/histories";
    }

}
