package com.example.outsourcing.user.service;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignupResponseDto signup(UserSignupRequestDto request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        String name = request.getName();
        User user = new User(username, email, password, name);

        // 이메일 중복시 예외처리, 커스텀 exception 변경
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User savedUser = userRepository.save(user);

        return new UserSignupResponseDto(savedUser);
    }

    public UserLoginResponseDto login(@Valid UserLoginRequestDto request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 아이디 없을 경우 예외처리, 커스텀 exception 변경
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"));

        // 비밀번호 틀릴시 예외처리, 커스텀 exception 변경
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Incorrect password");
        }

        // jwt 완성되면 추가
        // String token = jwtUtil.createToken()
        String token = "temp";

        return new UserLoginResponseDto(token);
    }
}