package com.movietheater.movietheater_mvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movietheater.movietheater_mvc.entities.Member;


public interface MemberRepository extends JpaRepository<Member, UUID>{

    Member findByAccount_Email(String email);

    Member findByAccount_IdentityCard(String identityCard);

    @Query("SELECT m FROM Member m JOIN m.account a WHERE a.username = :username")
    Member findByAccount_Username(@Param("username") String username);

}
