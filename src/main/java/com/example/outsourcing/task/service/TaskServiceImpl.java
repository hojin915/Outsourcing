package com.example.outsourcing.task.service;

import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.exception.AccessDeniedException;
import com.example.outsourcing.manager.service.ManagerService;
import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto RequestDto, AuthUser authUser) {
        Task.Status status = RequestDto.getStatus() != null ? RequestDto.getStatus() : Task.Status.TODO;

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));

        Task task = Task.builder()
                .title(RequestDto.getTitle())
                .content(RequestDto.getContent())
                .priority(RequestDto.getPriority())
                .dueDate(RequestDto.getDueDate())
                .status(Task.Status.TODO)
                .startDate(status == Task.Status.IN_PROGRESS ? LocalDateTime.now() : null)
                .user(user)
                .build();
        task.addManagers(user);
        Task saved = taskRepository.save(task);

        TaskResponseDto responseDto = TaskResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .priority(saved.getPriority())
                .status(saved.getStatus())
                .dueDate(saved.getDueDate())
                .startDate(saved.getStartDate())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
        responseDto.setTargetId(saved.getId());

        return responseDto;
    }

    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findByIsDeletedFalse();

        return tasks.stream()
                .map(task -> TaskResponseDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .content(task.getContent())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .dueDate(task.getDueDate())
                        .startDate(task.getStartDate())
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .build()
                )
                .toList();
    }

    public Page<TaskResponseDto> getTasks(String status, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Task.Status taskStatus = null;
        if (status != null) {
            taskStatus = Task.Status.valueOf(status.toUpperCase());
        }

        Page<Task> taskPage = taskRepository.findByCondition(taskStatus, keyword, pageable);

        return taskPage.map(task -> {
            TaskResponseDto responseDto = TaskResponseDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .content(task.getContent())
                    .priority(task.getPriority())
                    .status(task.getStatus())
                    .dueDate(task.getDueDate())
                    .startDate(task.getStartDate())
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .build();
            responseDto.setTargetId(task.getId());
            return responseDto;
        });
    }

    public void updateTaskStatus(Long id, Task.Status newStatus, Long currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));

        if (!task.getUser().getId().equals(currentUserId)){
            throw new AccessDeniedException("다른 사용자의 태스크는 수정할 수 없습니다.");
        }

        if (!task.getStatus().canTransitionTo(newStatus)){
            throw new AccessDeniedException("상태 변경이 허용되지 않습니다.");
        }

        task.setStatus(newStatus);
        if (newStatus == Task.Status.IN_PROGRESS && task.getStartDate() == null) {
            task.setStartDate(LocalDateTime.now());
        }

        taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long currentUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다."));

        if (!task.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("다른 사용자의 태스크는 삭제할 수 없습니다.");
        }

        task.softDelete();
        taskRepository.save(task);
    }

    public List<Long> findTaskIdsByUserId(Long userId) {
        return taskRepository.findTaskIdsByUserId(userId);
    }
}