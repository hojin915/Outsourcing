package com.example.outsourcing.user.service;

import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private String username;
    private String email;
    private String password;
    private String name;
    private User user;

    // 유저 세팅
    @BeforeEach
    void setUp() {
        username = "TestUserName";
        email = "test@example.com";
        password = "password";
        name = "TestName";
        user = new User(
                username,
                email,
                password,
                name,
                UserRole.USER);
        // User ID setter 만들지 않기 때문에 강제 주입
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    public void auth_회원가입_정상처리() {
        // given
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn(password);

        UserSignupRequestDto request = new UserSignupRequestDto(username, email, password, name);

        // when
        UserSignupResponseDto response = userService.signup(request);

        // then
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals(name, response.getName());
        assertEquals(UserRole.USER, response.getUserRole());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void auth_회원가입_이메일_중복_예외처리() {
        // given
        UserSignupRequestDto request = new UserSignupRequestDto(username, email, password, name);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.signup(request));
        assertEquals(ExceptionCode.ALREADY_EXISTS_EMAIL, exception.getExceptionCode());
    }

    @Test
    public void auth_회원가입_아이디_중복_예외처리() {
        // given
        UserSignupRequestDto request = new UserSignupRequestDto(username, email, password, name);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.signup(request));
        assertEquals(ExceptionCode.ALREADY_EXISTS_USERNAME, exception.getExceptionCode());
    }

    @Test
    public void auth_로그인_정상처리() {
        // given
        UserLoginRequestDto request = new UserLoginRequestDto(username, password);
        String testToken = UUID.randomUUID().toString();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).thenReturn(testToken);

        // when
        UserLoginResponseDto response = userService.login(request);

        // then
        assertEquals(testToken, response.getToken());
        verify(jwtUtil).createToken(anyLong(), anyString(), any(UserRole.class));
    }

    @Test
    public void auth_아이디_없을시_예외처리() {
        // given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.findByUsernameOrElseThrow(username));
        assertEquals(ExceptionCode.USER_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    public void auth_삭제된_유저시_예외처리() {
        // given
        ReflectionTestUtils.setField(user, "isDeleted", true);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.commonUserCheck(user, password));
        assertEquals(ExceptionCode.DELETED_USER, exception.getExceptionCode());
    }

    @Test
    public void auth_비밀번호_틑릴시_예외처리() {
        // given
        ReflectionTestUtils.setField(user, "isDeleted", false);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.commonUserCheck(user, password));
        assertEquals(ExceptionCode.WRONG_PASSWORD, exception.getExceptionCode());
    }

    @Test
    public void auth_프로필_조회_정상작동() {
        // given

        // when

        // then

    }
}