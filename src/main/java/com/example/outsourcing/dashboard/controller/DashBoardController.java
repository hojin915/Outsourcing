package com.example.outsourcing.dashboard.controller;


import com.example.outsourcing.dashboard.Task;
import com.example.outsourcing.dashboard.dto.OverdueCountDto;
import com.example.outsourcing.dashboard.dto.TaskDoneRatioDto;
import com.example.outsourcing.dashboard.dto.TaskStatusCountsDto;
import com.example.outsourcing.dashboard.dto.TotalCountsDto;
import com.example.outsourcing.dashboard.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dashboards")
public class DashBoardController {

    private final DashBoardService dashBoardService;

//URL : /api/dashboards/tasks/total-count
//URL : /api/dashboards/tasks/status-counts
//URL : /api/dashboards/tasks/completion-rate

    /**
     * 
     * @return 전체 태스크중 삭제되지 않은 태스크의 개수 조회
     */
    @GetMapping("/tasks/total-count")
    public ResponseEntity<TotalCountsDto> getTotalTaskCount() {
        TotalCountsDto resultDto = dashBoardService.getTotalCount();
        return ResponseEntity.ok().body(resultDto);
    }

    /**
     * 
     * @return TODO, IN_PROGRESS, DONE 상태별 테스크 개수 조회 
     */
    @GetMapping("/tasks/status-counts")
    public ResponseEntity<TaskStatusCountsDto> getTaskStatusCounts() {
        TaskStatusCountsDto resultDto = dashBoardService.getTaskStatusCounts();
        return ResponseEntity.ok().body(resultDto);
    }

    /**
     * 전체 태스크 중 완료된 태스크의 비율을 조회
     * @return 완료율(소수점 둘째 자리까지 포매팅)
     */
    @GetMapping("/tasks/completion-rate")
    public ResponseEntity<TaskDoneRatioDto> getDoneRatio() {
        TaskDoneRatioDto resultDto = dashBoardService.getDoneRatio();
        return ResponseEntity.ok().body(resultDto);
    }

    /**
     * 현재 시간이 마감일을 지났지만 아직 완료되지 않은 태스크 수
     * 상태가 TODO 또는 IN_PROGRESS인 테스크 중에서 조회
     * @return
     */
    @GetMapping("/tasks/overdue-count")
    public ResponseEntity<OverdueCountDto> getOverdueCount() {
        OverdueCountDto resultDto = dashBoardService.getOverdueCount();
        return ResponseEntity.ok().body(resultDto);
    }


    /**
     *
     * @return TODO 상태의 태스크 목록을 우선순위에따라 정렬하여 반환
     */
    @GetMapping("/tasks/sorted-priority/todo")
    public ResponseEntity<List<Task>> getTodoSortedByPriority () {
        List<Task> todoTasks = dashBoardService.getTodoSortedByPriority();
        return ResponseEntity.ok().body(todoTasks);
    }

    /**
     *
     * @return IN-PROGRESS 상태의 태스크 목록을 우선순위에 따라 정렬하여 반환
     */
    @GetMapping("/tasks/sorted-priority/in-progress")
    public ResponseEntity<List<Task>> getInProgressSortedByPriority () {
        List<Task> inProgressTasks = dashBoardService.getInProgressSortedByPriority();
        return ResponseEntity.ok().body(inProgressTasks);
    }

//    - **내 태스크 요약**
//    - [ ]  **오늘의 태스크:**
//        1. **TODO 상태**의 태스크 목록 확인
//        2. **IN_PROGRESS 상태**의 태스크 목록 확인
//        3. 태스크를 **우선순위(priority) 기준으로 정렬하여 보기**
}
