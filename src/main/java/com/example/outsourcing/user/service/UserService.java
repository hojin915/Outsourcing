package com.example.outsourcing.user.service;

import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignupResponseDto signup(UserSignupRequestDto request) {
        String username = request.getUsername();
        String email = request.getEmail();
        // 이메일 중복, 탈퇴한 이메일 예외처리
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // 아이디 중복시 예외처리
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        String name = request.getName();
        String password = passwordEncoder.encode(request.getPassword());
        User user = new User(username, email, password, name, UserRole.USER);

        User savedUser = userRepository.save(user);

        return new UserSignupResponseDto(savedUser);
    }

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 아이디는 재사용 가능한 경우
        // User user = userRepository.findByUsernameIsNotDeleted(username)
        // 아이디 없을 경우 예외처리
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        // 탈퇴한 유저인 경우 예외처리
        if(user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 아이디입니다.");
        }

        // 비밀번호 틀릴시 예외처리
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new UnauthorizedException("비밀번호가 틀렸습니다.");
        }

        // 토큰 생성해서 반환
        String token = jwtUtil.createToken(user.getId(), user.getUsername(),user.getUserRole());

        return new UserLoginResponseDto(token);
    }

    public UserProfileResponseDto getProfile(String username) {
        User totalUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        return new UserProfileResponseDto(totalUser);
    }

    // 연관 관계 삭제 비즈니스 로직에서 처리중
    // 대량의 경우 DB 에서 처리가 유리, n + 1 해결 위해서 고려할것
    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if(user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 유저입니다.");
        }

        // 연관된 entity 각각 softDelete
        user.softDelete();
        // 삭제 안하고 미할당 or 삭제 하고 미할당 확인하기
        user.getTasks().forEach(SoftDeleteEntity::softDelete);
        user.getComments().forEach(SoftDeleteEntity::softDelete);

        // taskRepository.softDeleteTasksByUserId(user.getId());
        // commentRepository.softDeleteCommentsByUserId(user.getId());

        // DB로 전환시 taskRepository, commentRepository 에 추가
        // @Modifying
        // @Query("UPDATE Task t SET t.isDeleted = true, t.deletedAt = CURRENT_TIMESTAMP WHERE t.user.id = :userId")
        // void softDeleteTaksByUserId(@Param("userId") Long userId);
        // @Modifying
        // @Query("UPDATE Comment c SET c.isDeleted = true, c.deletedAt = CURRENT_TIMESTAMP WHERE c.user.id = :userId")
        // void softDeleteTaksByUserId(@Param("userId") Long userId);
    }
}