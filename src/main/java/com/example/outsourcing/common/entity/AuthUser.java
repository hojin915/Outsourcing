package com.example.outsourcing.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;

}