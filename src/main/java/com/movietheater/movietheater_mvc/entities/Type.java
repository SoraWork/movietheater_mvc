package com.movietheater.movietheater_mvc.entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @ManyToMany(mappedBy = "types", fetch = FetchType.LAZY)
    private Set<Movie> movies;

}