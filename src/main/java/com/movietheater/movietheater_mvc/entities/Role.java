package com.movietheater.movietheater_mvc.entities;

import java.util.Set;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<Account> accounts;

}
