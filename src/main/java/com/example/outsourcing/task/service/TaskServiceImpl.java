package com.example.outsourcing.task.service;

import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
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

    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto RequestDto) {
        Task.Status status = RequestDto.getStatus() != null ? RequestDto.getStatus() : Task.Status.TODO;

        Task task = Task.builder()
                .title(RequestDto.getTitle())
                .content(RequestDto.getContent())
                .priority(RequestDto.getPriority())
                .dueDate(RequestDto.getDueDate())
                .status(Task.Status.TODO)
                .startDate(status == Task.Status.IN_PROGRESS ? LocalDateTime.now() : null)
                .build();
        Task saved = taskRepository.save(task);

        return TaskResponseDto.builder()
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
    }

    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

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

        return taskPage.map(task -> TaskResponseDto.builder()
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
        );
    }

    public void updateTaskStatus(Long id, Task.Status newStatus, Long currentUserId) {
        Task task = taskRepository.findById(taskId)
    }

}