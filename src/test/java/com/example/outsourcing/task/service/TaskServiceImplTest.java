//package com.example.outsourcing.task.service;
//
//import com.example.outsourcing.common.entity.AuthUser;
//import com.example.outsourcing.common.exception.AccessDeniedException;
//import com.example.outsourcing.task.dto.CreateTaskRequestDto;
//import com.example.outsourcing.task.entity.Task;
//import com.example.outsourcing.task.repository.TaskRepository;
//import com.example.outsourcing.user.entity.User;
//import com.example.outsourcing.user.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.*;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TaskServiceImplTest {
//
//    @InjectMocks
//    private TaskServiceImpl taskServiceImpl;
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//
//    @DisplayName("태스크 생성")
//    @Test
//    void createTask() {
//        //given
//        AuthUser authUser = new AuthUser(1L, "user", "", List.of(), 1L);
//
//        CreateTaskRequestDto dto = new CreateTaskRequestDto();
//        dto.setTitle("Test Title");
//        dto.setContent("Some Content");
//        dto.setPriority(Task.Priority.HIGH);
//        dto.setDueDate(LocalDateTime.now().plusDays(1));
//
//        User user = User.builder().id(1L).username("user").build();
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Task savedTask = Task.builder()
//                .id(100L)
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .priority(dto.getPriority())
//                .dueDate(dto.getDueDate())
//                .status(Task.Status.TODO)
//                .user(user)
//                .build();
//
//        when(taskRepository.save(any())).thenReturn(savedTask);
//
//        // when
//        var result = taskServiceImpl.createTask(dto, authUser);
//
//        // then
//
//        assertThat(result).isEqualTo(100L);
//
//    }
//}