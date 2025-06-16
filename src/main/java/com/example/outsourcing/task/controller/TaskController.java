package com.example.outsourcing.task.controller;

import com.example.outsourcing.task.dto.CreateTaskRequestDto;
import com.example.outsourcing.task.dto.TaskResponseDto;
import com.example.outsourcing.task.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
