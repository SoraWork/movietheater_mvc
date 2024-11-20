package com.movietheater.movietheater_mvc.services;

import java.util.UUID;

import com.movietheater.movietheater_mvc.dtos.member.MemberDTO;
import com.movietheater.movietheater_mvc.entities.Member;


public interface MemberService {
MemberDTO findById(UUID id);

Member findByUsername(String username);

Member update( Member member, String username);

}
