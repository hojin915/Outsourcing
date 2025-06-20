package com.example.outsourcing.task.service;

import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskServiceImpl;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private static final String USERNAME = "authTestUser";
    private static final String EMAIL = "authtestuser@example.com";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "auth test user ";
    private static final UserRole USER_ROLE = UserRole.USER;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("태스크 생성")
    @Test
    void createTask() {
        //given
        User user = new User(USERNAME, EMAIL, PASSWORD, NAME, USER_ROLE);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        AuthUser authUser = new AuthUser(1L, "user", "", List.of());

        CreateTaskRequestDto dto = new CreateTaskRequestDto();
        dto.setTitle("Test Title");
        dto.setDescription("Some Content");
        dto.setPriority(Task.Priority.HIGH);
        dto.setDueDate(LocalDateTime.now().plusDays(1));


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Task savedTask = Task.builder()
                .id(100L)
                .title(dto.getTitle())
                .content(dto.getDescription())
                .priority(dto.getPriority())
                .dueDate(dto.getDueDate())
                .status(Task.Status.TODO)
                .user(user)
                .build();

        when(taskRepository.save(any())).thenReturn(savedTask);

        // when
        ResponseDto<TaskResponseDto> task = taskServiceImpl.createTask(dto, authUser);

        // then

        assertThat(task.getData().getId()).isEqualTo(100L);

    }
}