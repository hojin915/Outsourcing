package com.example.outsourcing.manager.service;

import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.manager.dto.ManagerRequestDto;
import com.example.outsourcing.manager.dto.ManagerResponseDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {
    @InjectMocks
    private ManagerService managerService;

    @Mock
    private UserRepository userRepository;

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
    public void manager_등록_정상작동_테스트() {
        // given
        Long taskId = 1L;
        Long targetUserId = 3L;
        ManagerRequestDto request = new ManagerRequestDto(targetUserId);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // when
        ManagerResponseDto response = managerService.registerManager(username, taskId, request);

        // then
        assertEquals(taskId, response.getTaskId());
        assertEquals(targetUserId, response.getManagerId());
    }
}
