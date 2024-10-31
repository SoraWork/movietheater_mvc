package com.movietheater.movietheater_mvc.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "SCORE")
    private int score;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id")
    private Account account;

}
