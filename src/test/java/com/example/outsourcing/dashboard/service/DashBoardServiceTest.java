package com.example.outsourcing.dashboard.service;

import com.example.outsourcing.dashboard.Task;
import com.example.outsourcing.dashboard.TaskStatus;
import com.example.outsourcing.dashboard.dto.OverdueCountDto;
import com.example.outsourcing.dashboard.dto.TaskDoneRatioDto;
import com.example.outsourcing.dashboard.dto.TaskStatusCountsDto;
import com.example.outsourcing.dashboard.dto.TotalCountsDto;
import com.example.outsourcing.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashBoardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DashBoardService dashBoardService;


    @DisplayName("삭제되지않은 총 태스크 개수 조회")
    @Test
    void getTotalCount() {
        //given
        when(taskRepository.getTotalCount()).thenReturn(10L);
        //when
        TotalCountsDto result = dashBoardService.getTotalCount();
        //then
        assertEquals(10L, result.getTotalCount());
        verify(taskRepository, times(1)).getTotalCount();
    }

    @DisplayName("태스크 상태별 태스크 개수 조회")
    @Test
    void getTaskStatusCounts() {
        // given
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{TaskStatus.TODO, 8L},
                new Object[]{TaskStatus.IN_PROGRESS, 7L},
                new Object[]{TaskStatus.DONE, 4L}
        );
        when(taskRepository.countTaskByStatus()).thenReturn(mockResult);

        //when
        TaskStatusCountsDto result = dashBoardService.getTaskStatusCounts();

        //then
        assertEquals(8L, result.getTodo());
        assertEquals(7L, result.getInProgress());
        assertEquals(4L, result.getDone());

        verify(taskRepository, times(1)).countTaskByStatus();
    }

    @DisplayName("전체 태스크중 DONE인 태스크의 비율 조회")
    @Test
    void getDoneRatio() {
        //given
        when(taskRepository.getTotalCount()).thenReturn(10L);
        when(taskRepository.getCountDoneTasks()).thenReturn(3L);

        //when
        TaskDoneRatioDto result = dashBoardService.getDoneRatio();

        //then
        assertEquals("30.00",result.getCompletionRatio());

    }

    @DisplayName("마감일을 지났고 완료되지 않은 태스크 개수 조회")
    @Test
    void getOverdueCount() {
        //given
        Long expectedCount = 7L;
        when(taskRepository.getCountOverdueTasks(any(LocalDateTime.class))).thenReturn(expectedCount);

        //when
        OverdueCountDto result = dashBoardService.getOverdueCount();

        //then
        assertEquals(expectedCount, result.getOverdueCount());
    }

    @DisplayName("TODO 상태의 태스크를 우선순위기준으로 정렬")
    @Test
    void getTodoSortedByPriority() {
        //given
        Task task1 = new Task();
        ReflectionTestUtils.setField(task1, "id", 1L);
        ReflectionTestUtils.setField(task1, "status", TaskStatus.TODO);
        ReflectionTestUtils.setField(task1, "priority", 1);
        ReflectionTestUtils.setField(task1, "title", "TaskFirst");

        Task task2 = new Task();
        ReflectionTestUtils.setField(task2, "id", 2L);
        ReflectionTestUtils.setField(task2, "status", TaskStatus.TODO);
        ReflectionTestUtils.setField(task2, "priority", 2);
        ReflectionTestUtils.setField(task2, "title", "TaskSecond");

        List<Task> mockTasks = Arrays.asList(task1, task2);

        //when
        when(taskRepository.findByStatusOrderByPriorityAsc(TaskStatus.TODO)).thenReturn(mockTasks);
        List<Task> result = dashBoardService.getTodoSortedByPriority();

        //then확인불가
    }

}