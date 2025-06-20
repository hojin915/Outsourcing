package com.example.outsourcing.task.controller;

import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.task.dto.*;
import com.example.outsourcing.task.service.TaskService;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.service.UserQueryService;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaskController {
    private final UserQueryService userQueryService;
    private final TaskService taskService;

    // Task 생성
    @PostMapping("/api/tasks")
    public ResponseEntity<ResponseDto<TaskResponseDto>> createTask(
            @RequestBody @Valid CreateTaskRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        ResponseDto<TaskResponseDto> responseDto = taskService.createTask(requestDto, authUser);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/users")
    public ResponseEntity<ResponseDto<List<UserProfileResponseDto>>> getAllUsers (
    ) {
        List<UserProfileResponseDto> responseData = userQueryService.getAllUsers()
                .stream()
                .map(UserProfileResponseDto::new)
                .toList();
        ResponseDto<List<UserProfileResponseDto>> response =  new ResponseDto<>("요청처리", responseData);
        return ResponseEntity.ok(response);
    }

    // Task 목록 조회
    @GetMapping("/api/tasks")
    public ResponseEntity<ResponseDto<TaskSearchResponseDto> > getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer assigneeId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ResponseDto<TaskSearchResponseDto> tasks = taskService.getTasks(status, keyword, page, size, search ,assigneeId, authUser);
        return ResponseEntity.ok(tasks);
    }

    // Task 상세 조회
    // GET /api/tasks/{taskId}
    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponseDto>> getTaskBIyd(
            @PathVariable Long taskId
    ) {
        ResponseDto<TaskResponseDto> task = taskService.getTaskBIyd(taskId);
        return ResponseEntity.ok(task);
    }

    // Task 수정
    // PUT /api/tasks/{taskId}
    @PutMapping("/api/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponseDto>> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ResponseDto<TaskResponseDto> updatedTask = taskService.updateTask(taskId, dto, authUser.getId());
        return ResponseEntity.ok(updatedTask);
    }

    // Task 상태 업데이트
    // PATCH /api/tasks/{taskId}/status
    @PatchMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<ResponseDto<TaskResponseDto>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody ChangeStatusRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ResponseDto<TaskResponseDto> updatedTask = taskService.updateTaskStatus(taskId, dto.getStatus(), authUser.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/api/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponseDto>> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ResponseDto<TaskResponseDto> deletedTask = taskService.deleteTask(taskId, authUser.getId());
        return ResponseEntity.ok(deletedTask);
    }

}