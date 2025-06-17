package com.example.outsourcing.task.service;

import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto RequestDto) {
        Task task = Task.builder()
                .title(RequestDto.getTitle())
                .content(RequestDto.getContent())
                .priority(RequestDto.getPriority())
                .dueDate(RequestDto.getDueDate())
                .status(Task.Status.TODO)
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
}