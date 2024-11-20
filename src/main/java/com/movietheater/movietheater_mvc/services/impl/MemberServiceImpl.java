package com.movietheater.movietheater_mvc.services.impl;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.dtos.member.MemberDTO;
import com.movietheater.movietheater_mvc.entities.Member;
import com.movietheater.movietheater_mvc.repositories.MemberRepository;
import com.movietheater.movietheater_mvc.services.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDTO findById(UUID id) {
        // Get entity by id
        var member = memberRepository.findById(id).orElse(null);
        // Check if entity is null then return null
        if (member == null) {
            return null;
        }
        // Convert entity to DTO
        var memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUsername(member.getAccount().getUsername());
        memberDTO.setFullName(member.getAccount().getFullName());
        memberDTO.setDateOfBirth(member.getAccount().getDateOfBirth());
        memberDTO.setGender(member.getAccount().getGender());
        memberDTO.setEmail(member.getAccount().getEmail());
        memberDTO.setIdentityCard(member.getAccount().getIdentityCard());
        memberDTO.setPhoneNumber(member.getAccount().getPhoneNumber());
        memberDTO.setAddress(member.getAccount().getAddress());
        return memberDTO;
    }


    @Override
    public Member update(Member member, String username) {
        var memberupdate = memberRepository.findByAccount_Username(username);
        if (memberupdate == null) {
            return null;
        }
        memberupdate.getAccount().setFullName(member.getAccount().getFullName());
        memberupdate.getAccount().setDateOfBirth(member.getAccount().getDateOfBirth());
        memberupdate.getAccount().setGender(member.getAccount().getGender());
        memberupdate.getAccount().setEmail(member.getAccount().getEmail());
        memberupdate.getAccount().setPhoneNumber(member.getAccount().getPhoneNumber());
        memberupdate.getAccount().setAddress(member.getAccount().getAddress());
        memberupdate.getAccount().setPassword(passwordEncoder.encode(member.getAccount().getPassword()));
        return memberRepository.save(memberupdate); 
    }

    @Override
    public Member findByUsername(String username) {
        var member = memberRepository.findByAccount_Username(username);
        System.out.println(member);
        return member;
    }

}
