package com.example.outsourcing.user.controller;

import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserSignupResponseDto> signup(
            @Valid @RequestBody UserSignupRequestDto requestDto
    ){
        UserSignupResponseDto responseDto = userService.signup(requestDto);
        // 1L -> userId로 변경
        URI location = URI.create("/api/users/" + 1L);
        return ResponseEntity.created(location).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(
            @Valid @RequestBody UserLoginRequestDto requestDto
    ){

        return ResponseEntity.ok(userService.login(requestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> profile(
            @AuthenticationPrincipal(expression = "username") String username
    ){
        return ResponseEntity.ok(userService.getProfile(username));
    }
}