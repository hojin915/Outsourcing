package com.example.outsourcing.task.controller;

import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.task.dto.*;
import com.example.outsourcing.task.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestBody @Valid CreateTaskRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        TaskResponseDto responseDto = taskService.createTask(requestDto, authUser);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<TaskListResponseDto> getAllTasks(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskListResponseDto response = taskService.getAllTasks(authUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<TaskSearchResponseDto> getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskSearchResponseDto tasks = taskService.getTasks(status, keyword, page, size, authUser);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody ChangeStatusRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskResponseDto updatedTask = taskService.updateTaskStatus(id, dto.getStatus(), authUser.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDto> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskResponseDto deletedTask = taskService.deleteTask(id, authUser.getId());
        return ResponseEntity.ok(deletedTask);
    }

}