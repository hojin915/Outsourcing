package com.example.outsourcing.manager.controller;

import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.manager.dto.ManagerRequestDto;
import com.example.outsourcing.manager.dto.ManagerResponseDto;
import com.example.outsourcing.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ResponseDto<ManagerResponseDto>> registerManager(
            @AuthenticationPrincipal(expression = "username") String username,
            @PathVariable Long taskId,
            @RequestBody ManagerRequestDto requestDto
    ){
        ManagerResponseDto responseData = managerService.registerManager(
                username, taskId, requestDto
        );
        ResponseDto<ManagerResponseDto> response = new ResponseDto<>("매니저 등록을 완료했습니다.", responseData);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<ManagerResponseDto>> deleteManager(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long taskId,
            @RequestBody ManagerRequestDto requestDto
    ){
        ManagerResponseDto responseData = managerService.deleteManager(
                authUser.getUsername(), taskId, requestDto
        );
        ResponseDto<ManagerResponseDto> response = new ResponseDto<>("매니저 등록을 취소했습니다", responseData);
        return ResponseEntity.ok(response);
    }
}