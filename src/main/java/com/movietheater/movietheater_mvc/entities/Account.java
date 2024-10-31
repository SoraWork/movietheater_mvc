package com.movietheater.movietheater_mvc.entities;

import java.util.Set;
import java.time.LocalDate;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "IDENTITY_CARD")
    private String identityCard;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "REGISTER_DATE")
    private LocalDate registerDate;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "account")
    private Set<Member> members;

    @OneToMany(mappedBy = "account")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "account")
    private Set<Invoice> invoices;
    
}
