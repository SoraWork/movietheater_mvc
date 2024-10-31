package com.movietheater.movietheater_mvc.entities;

import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "TICKET_TYPE")
    private boolean ticketType;

}
