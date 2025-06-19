package com.example.outsourcing.manager.service;

import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.manager.dto.ManagerRequestDto;
import com.example.outsourcing.manager.dto.ManagerResponseDto;
import com.example.outsourcing.manager.repository.ManagerRepository;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {
    @InjectMocks
    private ManagerService managerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ManagerRepository managerRepository;

    private String username;
    private String email;
    private String password;
    private String name;
    private User user;

    private Task task;

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

        task = new Task();
        task.setId(1L);
        task.setUser(user);
    }

    @Test
    public void manager_등록_정상작동_테스트() {
        // given
        Long taskId = 1L;
        Long userId = 1L;
        Long targetUserId = 2L;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        User targetUser = new User();
        ReflectionTestUtils.setField(targetUser, "id", targetUserId);

        ManagerRequestDto request = new ManagerRequestDto(targetUserId);

        when(userRepository.findByUsernameOrElseThrow(username)).thenReturn(user);
        when(userRepository.findByIdOrElseThrow(targetUserId)).thenReturn(targetUser);
        when(taskRepository.findByIdOrElseThrow(taskId)).thenReturn(task);
        when(managerRepository.findByTaskAndUser(task, targetUser)).thenReturn(Optional.empty());


        // when
        ManagerResponseDto response = managerService.registerManager(username, taskId, request);

        // then
        assertEquals(taskId, response.getTaskId());
        assertEquals(targetUserId, response.getManagerId());
    }
}
