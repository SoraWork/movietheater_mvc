package com.movietheater.movietheater_mvc.services.impl;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.dtos.auth.RegisterDTO;
import com.movietheater.movietheater_mvc.entities.Account;
import com.movietheater.movietheater_mvc.entities.Member;
import com.movietheater.movietheater_mvc.repositories.AccountRepository;
import com.movietheater.movietheater_mvc.repositories.MemberRepository;
import com.movietheater.movietheater_mvc.repositories.RoleRepository;
import com.movietheater.movietheater_mvc.services.AuthService;
import com.movietheater.movietheater_mvc.services.impl.util.CustomUserDetails;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    public AuthServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UUID save(RegisterDTO registerDTO) {
        var existingUser = existsByUsername(registerDTO.getUsername());
        if (existingUser) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        var user = new Account();
        user.setUsername(registerDTO.getUsername());;
        user.setFullName(registerDTO.getFullName());
        user.setDateOfBirth(registerDTO.getDateOfBirth());
        user.setGender(registerDTO.getGender());
        user.setIdentityCard(registerDTO.getIdentityCard());
        user.setEmail(registerDTO.getEmail());
        user.setAddress(registerDTO.getAddress());
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        var role = roleRepository.findByRoleName("CUSTOMER")
                .orElseThrow(() -> new IllegalArgumentException("Role CUSTOMER does not exist"));

        user.setRole(role);

        accountRepository.save(user);

        var member = new Member();
        member.setScore(0);
        member.setAccount(user);

        memberRepository.save(member);

        return user.getId();
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
    
        Set<GrantedAuthority> grantedAuthorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toSet());
    
        System.out.println(grantedAuthorities);
        return new CustomUserDetails(account.getFullName(), account.getPassword(), grantedAuthorities);
    }
    
}
