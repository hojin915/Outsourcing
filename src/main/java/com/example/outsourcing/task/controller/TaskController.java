package com.example.outsourcing.task.controller;

import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestBody @Valid CreateTaskRequestDto requestDto
            ){
        TaskResponseDto responseDto = taskService.createTask(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/tasks")
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
}
