package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movietheater.movietheater_mvc.entities.Ticket;

@Repository

public interface TicketRepository extends JpaRepository<Ticket, UUID>{

}
