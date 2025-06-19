package com.example.outsourcing.activitylog.controller;


import com.example.outsourcing.activitylog.dto.ActivityLogResponseDto;
import com.example.outsourcing.activitylog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    // 유저 id기준으로 로그 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Page<ActivityLogResponseDto>> getUserActivities(
            @PathVariable Long userId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "requestTime") String sortBy,    // ("requestTime", "activityType")
            @RequestParam(defaultValue = "desc") String sortOrder, // ("asc", "desc")
            Pageable pageable
    ){

        Page<ActivityLogResponseDto> activityLogs = activityLogService.getUserActivities(
                userId,
                activityType,
                targetId,
                sortBy,
                sortOrder,
                pageable
        );
        return ResponseEntity.ok(activityLogs);
    }
}
