package com.example.outsourcing.dashboard.service;

import com.example.outsourcing.dashboard.dto.TaskDoneRatioDto;
import com.example.outsourcing.dashboard.dto.TaskStatusCountsDto;
import com.example.outsourcing.dashboard.dto.TotalCountsDto;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DashBoardService {

    private final TaskRepository taskRepository;

    /**
     * @return 삭제되지않은 총 테스크의 개수 조회
     */
    @Transactional(readOnly = true)
    public TotalCountsDto getTotalCount() {
        Long totalCount = taskRepository.getTotalCount();
        return new TotalCountsDto(totalCount);
    }

    /**
     * TODO, IN_PROGRESS, DONE 상태별 테스크 개수 조회
     * @return TaskStatusCountsDto 객체
     */
    @Transactional(readOnly = true)
    public TaskStatusCountsDto getTaskStatusCounts() {
        //쿼리결과 Object=(enum : Long)
        List<Object[]> results = taskRepository.countTaskByStatus();

        //쿼리결과를 키 값 쌍으로 저장하기 위한 hashmap 초기화
        Map<Task.Status, Long> statusCountMap = new HashMap<>();
        for(Task.Status status : Task.Status.values()) {
            statusCountMap.put(status, 0L);
        }

        //results 를 돌며  statusCountMap에 담는 for문
        for (Object[] enum_Long : results) {
            Task.Status status = (Task.Status) enum_Long[0];
            Long count = (Long) enum_Long[1];
            statusCountMap.put(status, count);
        }

        return TaskStatusCountsDto.builder()
                .todo(statusCountMap.get(Task.Status.TODO))
                .inProgress(statusCountMap.get(Task.Status.IN_PROGRESS))
                .done(statusCountMap.get(Task.Status.DONE))
                .build();
    }

    /**
     * 전체 태스크 중 완료된 태스크의 비율을 조회
     * 계산식: (DONE 상태 태스크 수 / 전체 태스크 수) × 100 (소수점 둘째 자리까지 표시)
     */
    @Transactional(readOnly = true)
    public TaskDoneRatioDto getDoneRatio() {

        Long totalTasks = taskRepository.getTotalCount();
        Long doneTasks = taskRepository.getCountDoneTasks();

        double doneRatio = 0.00;

        if (totalTasks !=null && totalTasks > 0) {
            doneRatio = ((double) doneTasks / totalTasks) * 100;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedRatio = decimalFormat.format(doneRatio);

        return new TaskDoneRatioDto(formattedRatio);
    }

    /**
     *마감일을지난 TODO 또는 IN_PROGRESS 상태인 태스크 개수 조회
     *
     * @return OverdueCountDto
     */
    @Transactional(readOnly = true)
    public Long getOverdueTaskCount() {
        return taskRepository.countOverdueTasks(Task.Status.TODO, Task.Status.IN_PROGRESS);
    }
    /**
     *TODO 상태의 태스크 목록을 우선순위 기준으로 정렬
     */
    @Transactional(readOnly = true)
    public List<Task> getTodoSortedByPriority() {
        return taskRepository.findByStatusOrderByPriorityDesc(Task.Status.TODO);
    }
    /**
     * IN_PROGRESS 상태의 태스크 목록을 우선순위 기준으로 정렬
     */
    @Transactional(readOnly = true)
    public List<Task> getInProgressSortedByPriority() {
        return taskRepository.findByStatusOrderByPriorityDesc(Task.Status.IN_PROGRESS);
    }

}
