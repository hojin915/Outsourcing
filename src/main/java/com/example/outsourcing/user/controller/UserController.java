package com.example.outsourcing.user.controller;

import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserDeleteResponseDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<ResponseDto<UserSignupResponseDto>> signup(
            @Valid @RequestBody UserSignupRequestDto requestDto
    ) {
        UserSignupResponseDto responseDto = userService.signup(requestDto);
        URI location = URI.create("/api/users/" + responseDto.getId());

        ResponseDto<UserSignupResponseDto> response = new ResponseDto<>("회원가입이 완료되었습니다.", responseDto);
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto<UserLoginResponseDto>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto
    ) {
        UserLoginResponseDto responseDto = userService.login(requestDto);

        ResponseDto<UserLoginResponseDto> response = new ResponseDto<>("로그인이 완료되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me")
    public ResponseEntity<ResponseDto<UserProfileResponseDto>> profile(
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        UserProfileResponseDto responseDto = userService.getProfile(username);

        ResponseDto<UserProfileResponseDto> response = new ResponseDto<>("프로필 조회에 성공했습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/withdraw")
    public ResponseEntity<ResponseDto<UserDeleteResponseDto>> delete(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody UserDeleteRequestDto request
    ){
        UserDeleteResponseDto responseDto = userService.delete(username, request);

        ResponseDto<UserDeleteResponseDto> response = new ResponseDto<>("회원탈퇴가 완료되었습니다.", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}