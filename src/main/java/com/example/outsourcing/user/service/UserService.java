package com.example.outsourcing.user.service;

import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.manager.repository.ManagerRepository;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public UserSignupResponseDto signup(UserSignupRequestDto request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // 이메일 중복(탈퇴한 이메일 포함) 예외처리
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(ExceptionCode.ALREADY_EXISTS_EMAIL);
        }

        // 아이디 중복(탈퇴한 아이디 포함) 예외처리
        if(userRepository.existsByUsername(username)){
            throw new CustomException(ExceptionCode.ALREADY_EXISTS_USERNAME);
        }

        String name = request.getName();
        String password = passwordEncoder.encode(request.getPassword());
        User user = new User(username, email, password, name, UserRole.USER);

        User savedUser = userRepository.save(user);
        UserSignupResponseDto responseDto = new UserSignupResponseDto(savedUser);

        // targetId 삽입
        responseDto.setTargetId(savedUser.getId());

        return responseDto;
    }

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 아이디 없을 경우 예외처리
        User user = findByUsernameOrElseThrow(username);

        commonUserCheck(user, password);

        // 토큰 생성해서 반환
        String token = jwtUtil.createToken(user.getId(), user.getUsername(),user.getUserRole());

        UserLoginResponseDto responseDto = new UserLoginResponseDto(token);

        // targetId 삽입
        responseDto.setTargetId(user.getId());

        return responseDto;
    }

    public UserProfileResponseDto getProfile(String username) {
        User totalUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        UserProfileResponseDto responseDto = new UserProfileResponseDto(totalUser);

        // targetId 삽입
        responseDto.setTargetId(totalUser.getId());

        return responseDto;
    }

    @Transactional
    public void delete(String username, UserDeleteRequestDto request) {
        // 유저가 없을 시 예외처리
        User user = findByUsernameOrElseThrow(username);

        String password = request.getPassword();

        commonUserCheck(user, password);

        user.softDelete();
        taskRepository.softDeleteTasksByUserId(user.getId());
        commentRepository.softDeleteCommentsByUserId(user.getId());
    }

    // 공통 예외 처리
    private void commonUserCheck(User user, String password) {
        // 삭제된 유저시 예외처리
        if(user.isDeleted()) {
            throw new CustomException(ExceptionCode.DELETED_USER);
        }

        // 비밀번호 틀릴 시 예외처리
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(ExceptionCode.WRONG_PASSWORD);
        }
    }

    // 예외처리 Repository -> Service 이동
    // 기존 사용으로 인해 Repository 내부 메서드 유지
    public User findByUsernameOrElseThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }
}