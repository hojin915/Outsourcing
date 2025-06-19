package com.example.outsourcing.dashboard.controller;

import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.dashboard.dto.*;
import com.example.outsourcing.dashboard.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/dashboards")
public class DashBoardController {

    private final DashBoardService dashBoardService;

    /**
     *
     * @return 전체 태스크중 삭제되지 않은 태스크의 개수 조회
     */
    @GetMapping("/tasks/total-count")
    public ResponseEntity<ResponseDto<TotalCountsDto>> getTotalTaskCount(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TotalCountsDto resultDto = dashBoardService.getTotalCount(authUser.getId());

        return ResponseEntity.ok()
                .body(new ResponseDto<>("전체 태스크 개수", resultDto));
    }

    /**
     *
     * @return TODO, IN_PROGRESS, DONE 상태별 테스크 개수 조회
     */
    @GetMapping("/tasks/status-counts")
    public ResponseEntity<ResponseDto<TaskStatusCountsDto>> getTaskStatusCounts(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskStatusCountsDto resultDto = dashBoardService.getTaskStatusCounts(authUser.getId());
        return ResponseEntity.ok().body(new ResponseDto<>("상태별 태스크 개수",resultDto));
    }

    /**
     * 전체 태스크 중 완료된 태스크의 비율을 조회
     * @return 완료율(소수점 둘째 자리까지 포매팅)
     */
    @GetMapping("/tasks/completion-rate")
    public ResponseEntity<ResponseDto<TaskDoneRatioDto>> getDoneRatio(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        TaskDoneRatioDto resultDto = dashBoardService.getDoneRatio(authUser.getId());
        return ResponseEntity.ok().body(new ResponseDto<>("태스크 완료율",resultDto));
    }

    /**
     * 마감일을지난 TODO 또는 IN_PROGRESS 상태인 태스크 개수 조회
     * @return 마감기한이 지난 태스크 개수
     */
    @GetMapping("/tasks/overdue-count")
    public ResponseEntity<ResponseDto<CountOverdueTaskDto>> getOverdueCount(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CountOverdueTaskDto overdueTaskCount = dashBoardService.getOverdueTaskCount(authUser.getId());
        return ResponseEntity.ok().body(new ResponseDto<>("기한 초과된 태스크 개수",overdueTaskCount));
    }

    /**
     *
     * @return TODO 상태의 태스크 목록을 우선순위에따라 정렬하여 반환
     */
    @GetMapping("/tasks/sorted-priority/todo")
    public ResponseEntity<ResponseDto<PriorityTaskForTargetIdDto>> getTodoSortedByPriority (
            @AuthenticationPrincipal AuthUser authUser
    ) {
        PriorityTaskForTargetIdDto priorityTaskForTargetIdDto = dashBoardService.todoSortedByPriority(authUser.getId());
        return ResponseEntity.ok().body(new ResponseDto<>("TODO 태스크 (우선순위 정렬)",priorityTaskForTargetIdDto));
    }

    /**
     *
     * @return IN-PROGRESS 상태의 태스크 목록을 우선순위에 따라 정렬하여 반환
     */
    @GetMapping("/tasks/sorted-priority/in-progress")
    public ResponseEntity<ResponseDto<PriorityTaskForTargetIdDto>> getInProgressSortedByPriority (
            @AuthenticationPrincipal AuthUser authUser
    ) {
        PriorityTaskForTargetIdDto priorityTaskForTargetIdDto = dashBoardService.inProgressSortedByPriority(authUser.getId());
        return ResponseEntity.ok().body(new ResponseDto<>("IN_PROGRESS 테스크 (우선순위 정렬)", priorityTaskForTargetIdDto));
    }

}
