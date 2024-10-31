package com.movietheater.movietheater_mvc.dtos.employee;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private UUID id;

    @NotBlank
    @Length(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank
    @Length(min = 2, max = 50, message = "Password must be between 2 and 50 characters")
    private String password;

    @NotBlank
    @Length(min = 2, max = 50, message = "Password must be between 2 and 50 characters")
    private String fullName;

    @NotNull
    @Past 
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotBlank
    @Length(min = 12, max = 12, message = "Identity card must be 12 characters")
    private String identityCard;

    @NotBlank
    @Length(min = 6, max = 50, message = "Email must be between 6 and 50 characters")
    private String email;

    @NotBlank
    @Length(min = 5, max = 50, message = "address must be between 6 and 50 characters")
    private String address;

    @NotBlank
    private String phoneNumber;

}
