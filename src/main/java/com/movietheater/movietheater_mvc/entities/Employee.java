package com.movietheater.movietheater_mvc.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id")
    private Account account;

}
