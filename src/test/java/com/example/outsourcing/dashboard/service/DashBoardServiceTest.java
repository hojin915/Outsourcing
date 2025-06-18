package com.example.outsourcing.dashboard.service;

import com.example.outsourcing.dashboard.dto.TaskDoneRatioDto;
import com.example.outsourcing.dashboard.dto.TaskStatusCountsDto;
import com.example.outsourcing.dashboard.dto.TotalCountsDto;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                new Object[]{Task.Status.TODO, 8L},
                new Object[]{Task.Status.IN_PROGRESS, 7L},
                new Object[]{Task.Status.DONE, 4L}
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

    @DisplayName("기한초과된 태스크의 개수 조회")
    @Test
    void getOverdueCount() {
        //given
        Long expectedCount = 7L;
        when(taskRepository.countOverdueTasks(Task.Status.TODO, Task.Status.IN_PROGRESS)).thenReturn(expectedCount);

        //when
        Long count = dashBoardService.getOverdueTaskCount();

        //then
        assertEquals(expectedCount, count);
        verify(taskRepository, times(1)).countOverdueTasks(Task.Status.TODO, Task.Status.IN_PROGRESS);


    }
}