package com.example.outsourcing.task.controller;

import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.task.dto.ChangeStatusRequestDto;
import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TaskResponseDto>> getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<TaskResponseDto> tasks = taskService.getTasks(status,keyword, page, size);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody ChangeStatusRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        taskService.updateTaskStatus(id, dto.getStatus(), authUser.getId());
        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        taskService.deleteTask(id, authUser.getId());
        return ResponseEntity.ok("태스크가 삭제 되었습니다.");
    }
}