package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movietheater.movietheater_mvc.entities.Member;


public interface MemberRepository extends JpaRepository<Member, UUID>{

}
