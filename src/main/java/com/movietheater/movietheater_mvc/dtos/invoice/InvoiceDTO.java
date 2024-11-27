package com.movietheater.movietheater_mvc.dtos.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.movietheater.movietheater_mvc.entities.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private UUID id;

    private LocalDate bookingDate;

    private String movieName;

    private LocalDate scheduleShow;

    private String scheduleShowTime;

    private BigDecimal totalMoney;

    private String seat;

    private boolean status;

    private UUID accountId;

    private Account account;

}
