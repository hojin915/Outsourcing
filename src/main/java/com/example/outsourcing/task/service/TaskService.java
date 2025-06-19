package com.example.outsourcing.task.service;

import com.example.outsourcing.comment.service.CommentService;
import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.exception.AccessDeniedException;
import com.example.outsourcing.manager.service.ManagerService;
import com.example.outsourcing.task.dto.*;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.dto.response.UserSummaryResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final CommentService commentService;
    private final ManagerService managerService;

    @Transactional
    public ResponseDto<TaskResponseDto> createTask(CreateTaskRequestDto RequestDto, AuthUser authUser) {
        Task.Status status = RequestDto.getStatus() != null ? RequestDto.getStatus() : Task.Status.TODO;

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));

        Task task = Task.builder()
                .title(RequestDto.getTitle())
                .content(RequestDto.getDescription())
                .priority(RequestDto.getPriority())
                .dueDate(RequestDto.getDueDate())
                .status(status)
                .startDate(status == Task.Status.IN_PROGRESS ? LocalDateTime.now() : null)
                .user(user)
                .build();
        task.addManagers(user);
        Task saved = taskRepository.save(task);

        TaskResponseDto responseDto = TaskResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getContent())
                .dueDate(saved.getDueDate())
                .priority(saved.getPriority())
                .status(saved.getStatus())
                .assigneeId(saved.getUser().getId())
                .assignee(UserSummaryResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
                )
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .startDate(saved.getStartDate())
                .build();

        return new ResponseDto<>("Task가 생성되었습니다.", responseDto);
    }

    public ResponseDto<List<TaskResponseDto>> getAllTasks() {
        List<Task> tasks = taskRepository.findByIsDeletedFalse();

        List<TaskResponseDto> taskResponseDtos = tasks.stream()
                .map(task -> TaskResponseDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getContent())
                        .dueDate(task.getDueDate())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .assigneeId(task.getUser().getId())
                        .assignee(UserSummaryResponseDto.builder()
                                .id(task.getUser().getId())
                                .username(task.getUser().getUsername())
                                .name(task.getUser().getName())
                                .email(task.getUser().getEmail())
                                .build()
                        )
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .startDate(task.getStartDate())
                        .build()
                )
                .collect(Collectors.toList());

        return new ResponseDto<>("요청이 성공적으로 처리되었습니다.", taskResponseDtos);
    }

    public ResponseDto<TaskSearchResponseDto> getTasks(String status, String keyword, int page, int size, String search, Integer assigneeId, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Task.Status taskStatus = null;
        if (status != null) {
            taskStatus = Task.Status.valueOf(status.toUpperCase());
        }

        Page<Task> taskPage = taskRepository.findByCondition(taskStatus, keyword, pageable);

        Page<TaskResponseDto> responseDtoPage = taskPage.map(task -> {
            return TaskResponseDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getContent())
                    .dueDate(task.getDueDate())
                    .priority(task.getPriority())
                    .status(task.getStatus())
                    .assigneeId(task.getUser().getId())
                    .assignee(UserSummaryResponseDto.builder()
                            .id(task.getUser().getId())
                            .username(task.getUser().getUsername())
                            .name(task.getUser().getName())
                            .email(task.getUser().getEmail())
                            .build()
                    )
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .startDate(task.getStartDate())
                    .build();
        });

        TaskSearchResponseDto searchResponse = TaskSearchResponseDto.fromPage(responseDtoPage);
        searchResponse.setTargetId(authUser.getId());

        return new ResponseDto<>("Task 목록을 조회했습니다.", searchResponse);
    }

    public ResponseDto<TaskResponseDto> getTaskBIyd(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));
        TaskResponseDto build = TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getContent())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(task.getUser().getId())
                .assignee(UserSummaryResponseDto.builder()
                        .id(task.getUser().getId())
                        .username(task.getUser().getUsername())
                        .name(task.getUser().getName())
                        .email(task.getUser().getEmail())
                        .build()
                )
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .startDate(task.getStartDate())
                .build();
        return new ResponseDto<>("Task를 조회했습니다.", build);
    }
    @Transactional
    public ResponseDto<TaskResponseDto> updateTask(Long id, UpdateTaskRequestDto dto, Long currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));

        if (!task.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("다른 사용자의 태스크는 수정할 수 없습니다.");
        }

        task.setTitle(dto.getTitle());
        task.setContent(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        // 담당자 id 수정 적용

        if (dto.getStatus() == Task.Status.IN_PROGRESS && task.getStartDate() == null) {
            task.setStartDate(LocalDateTime.now());
        }

        Task saved = taskRepository.save(task);

        TaskResponseDto build = TaskResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getContent())
                .dueDate(saved.getDueDate())
                .priority(saved.getPriority())
                .status(saved.getStatus())
                .assigneeId(saved.getUser().getId())
                .assignee(UserSummaryResponseDto.builder()
                        .id(saved.getUser().getId())
                        .username(saved.getUser().getUsername())
                        .name(saved.getUser().getName())
                        .email(saved.getUser().getEmail())
                        .build()
                )
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .startDate(saved.getStartDate())
                .build();
        return new ResponseDto<>("Task가 수정되었습니다.", build);
    }


    @Transactional
    public ResponseDto<TaskResponseDto> updateTaskStatus(Long id, Task.Status newStatus, Long currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));

        if (task.isDeleted()) {
            throw new AccessDeniedException("삭제된 태스크는 상태를 변경할 수 없습니다.");
        }

        if (!task.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("다른 사용자의 태스크는 수정할 수 없습니다.");
        }

        if (!task.getStatus().canTransitionTo(newStatus)) {
            throw new AccessDeniedException("상태 변경이 허용되지 않습니다.");
        }

        task.setStatus(newStatus);
        if (newStatus == Task.Status.IN_PROGRESS && task.getStartDate() == null) {
            task.setStartDate(LocalDateTime.now());
        }

        Task saved = taskRepository.save(task);

        TaskResponseDto build = TaskResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getContent())
                .dueDate(saved.getDueDate())
                .priority(saved.getPriority())
                .status(saved.getStatus())
                .assigneeId(saved.getUser().getId())
                .assignee(UserSummaryResponseDto.builder()
                        .id(saved.getUser().getId())
                        .username(saved.getUser().getUsername())
                        .name(saved.getUser().getName())
                        .email(saved.getUser().getEmail())
                        .build()
                )
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .startDate(saved.getStartDate())
                .build();
        return new ResponseDto<>("작업 상태가 업데이트되었습니다.", build);
    }

    @Transactional
    public ResponseDto<TaskResponseDto> deleteTask(Long taskId, Long currentUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));

        if (task.isDeleted()) {
            throw new AccessDeniedException("이미 삭제된 태스크입니다.");
        }

        if (!task.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("다른 사용자의 태스크는 삭제할 수 없습니다.");
        }

        task.softDelete();
        List<Long> taskIds = new ArrayList<>();
        taskIds.add(task.getId());
        softDeleteTasksConnections(taskIds);

        Task deletedTask = taskRepository.save(task);
        TaskResponseDto build = TaskResponseDto.builder()
                .id(deletedTask.getId())
                .title(deletedTask.getTitle())
                .description(deletedTask.getContent())
                .dueDate(deletedTask.getDueDate())
                .priority(deletedTask.getPriority())
                .status(deletedTask.getStatus())
                .assigneeId(deletedTask.getUser().getId())
                .assignee(UserSummaryResponseDto.builder()
                        .id(deletedTask.getUser().getId())
                        .username(deletedTask.getUser().getUsername())
                        .name(deletedTask.getUser().getName())
                        .email(deletedTask.getUser().getEmail())
                        .build()
                )
                .createdAt(deletedTask.getCreatedAt())
                .updatedAt(deletedTask.getUpdatedAt())
                .startDate(deletedTask.getStartDate())
                .build();
        return new ResponseDto<>("Task가 삭제되었습니다.", build);
    }

    public List<Long> findTaskIdsByUserId(Long userId) {
        return taskRepository.findTaskIdsByUserId(userId);
    }

    public void softDeleteTasksConnections(List<Long> taskIds) {
        managerService.softDeleteManagers(taskIds);
        commentService.softDeleteComments(taskIds);
    }
}