package com.example.outsourcing;

import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserLoginResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import com.example.outsourcing.user.service.UserService;
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
        assertEquals("이미 존재하는 이메일입니다", exception.getMessage());
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
        assertEquals("이미 존재하는 아이디입니다", exception.getMessage());
    }

    @Test
    public void auth_로그인_정상처리() {
        // given
        UserLoginRequestDto request = new UserLoginRequestDto(username, password);
        String testToken = UUID.randomUUID().toString();
        when(userRepository.findByUsernameOrElseThrow(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).thenReturn(testToken);

        // when
        UserLoginResponseDto response = userService.login(request);

        // then
        assertEquals(testToken, response.getToken());
        verify(jwtUtil).createToken(anyLong(), anyString(), any(UserRole.class));
    }

    @Test
    public void auth_로그인_아이디_없을시_예외처리() {
        // given
        UserLoginRequestDto request = new UserLoginRequestDto(username, password);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.login(request));
        assertEquals("유저를 찾을 수 없습니다", exception.getMessage());
    }
}