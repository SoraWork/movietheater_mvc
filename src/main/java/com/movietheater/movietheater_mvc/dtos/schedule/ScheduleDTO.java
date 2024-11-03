package com.movietheater.movietheater_mvc.dtos.schedule;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private UUID id;
    
    @NotBlank
    @Length(min = 2, max = 255, message = "Schedule time must be between 2 and 255 characters")
    private String scheduleTime;

}
