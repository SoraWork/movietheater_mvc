package com.movietheater.movietheater_mvc.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.movietheater.movietheater_mvc.entities.Member;
import com.movietheater.movietheater_mvc.services.MemberService;
import com.movietheater.movietheater_mvc.services.impl.util.CustomUserDetails;

@Controller
@RequestMapping("/manager/users")
public class UserController {

    private final MemberService memberService;

    public UserController(MemberService memberService) {

        this.memberService = memberService;
    }

    @GetMapping("/edit")
    public String edit(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        Member member = memberService.findByUsername(currentUsername);
        if (member == null) {
            System.out.println("Không tìm thấy member với username: " + currentUsername);
        }

        model.addAttribute("member", member);
        return "manager/users/edit";
    }

    @PostMapping("/edit")
    public String edit(Model model,
            @ModelAttribute Member member,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (member.getAccount().getFullName() == null || member.getAccount().getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("account.fullName", "account.fullName.empty", "Tên không được để trống");
        }
        if (member.getAccount().getPassword() == null || member.getAccount().getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("account.password", "account.password.empty", "Password không được để trống");
        }
        if (member.getAccount().getDateOfBirth() == null) {
            bindingResult.rejectValue("account.dateOfBirth", "account.dateOfBirth.empty",
                    "DateOfBirth không được để trống");
        }
        if (member.getAccount().getGender() == null || member.getAccount().getGender().trim().isEmpty()) {
            bindingResult.rejectValue("account.gender", "account.gender.empty", "Gender không được để trống");
        }
        if (member.getAccount().getEmail() == null || member.getAccount().getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("account.email", "account.email.empty", "Email không được để trống");
        }
        if (member.getAccount().getPhoneNumber() == null || member.getAccount().getPhoneNumber().trim().isEmpty()) {
            bindingResult.rejectValue("account.phoneNumber", "account.phoneNumber.empty",
                    "PhoneNumber không được để trống");
        }
        if (member.getAccount().getAddress() == null || member.getAccount().getAddress().trim().isEmpty()) {
            bindingResult.rejectValue("account.address", "account.address.empty", "Address không được để trống");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("member", member);
            return "manager/users/edit";
        }

        var result = memberService.update(member, userDetails.getUsername());
        if (result == null) {
            model.addAttribute("member", member);
            return "manager/users/edit";
        }

        return "redirect:/manager/users/edit";

    }

}
