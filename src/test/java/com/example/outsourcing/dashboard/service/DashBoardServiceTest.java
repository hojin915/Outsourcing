package com.example.outsourcing.dashboard.service;

import com.example.outsourcing.dashboard.dto.*;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
        CountOverdueTaskDto overdueTaskCount = dashBoardService.getOverdueTaskCount();

        //then
        assertEquals(expectedCount, overdueTaskCount.getCountOverdueTask());
        verify(taskRepository, times(1)).countOverdueTasks(Task.Status.TODO, Task.Status.IN_PROGRESS);
    }

    @DisplayName("TODO 상태의 태스크를 우선순위기준으로 정렬")
    @Test
    void getTodoSortedByPriority() {
        //given
        User testUser = new User();
        ReflectionTestUtils.setField(testUser, "id", 10L);

        Task task1 = new Task();
        ReflectionTestUtils.setField(task1, "id", 1L);
        ReflectionTestUtils.setField(task1, "status", Task.Status.TODO);
        ReflectionTestUtils.setField(task1, "priority", Task.Priority.LOW );
        ReflectionTestUtils.setField(task1, "title", "우선순위낮은할일");
        ReflectionTestUtils.setField(task1,"user",testUser);

        Task task2 = new Task();
        ReflectionTestUtils.setField(task2, "id", 2L);
        ReflectionTestUtils.setField(task2, "status", Task.Status.TODO);
        ReflectionTestUtils.setField(task2, "priority", Task.Priority.MEDIUM);
        ReflectionTestUtils.setField(task2, "title", "우선순위중간할일");
        ReflectionTestUtils.setField(task2,"user",testUser);

        Task task3 = new Task();
        ReflectionTestUtils.setField(task3, "id", 3L);
        ReflectionTestUtils.setField(task3, "status", Task.Status.TODO);
        ReflectionTestUtils.setField(task3, "priority", Task.Priority.HIGH);
        ReflectionTestUtils.setField(task3, "title", "우선순위높은할일");
        ReflectionTestUtils.setField(task3,"user",testUser);

        List<Task> mockTasks = Arrays.asList(task3, task2, task1);
        when(taskRepository.findTaskSortedByPriority(Task.Status.TODO)).thenReturn(mockTasks);

        //when
        PriorityTaskForTargetIdDto priorityTaskForTargetIdDto = dashBoardService.todoSortedByPriority();

        //then
        assertEquals(Task.Priority.HIGH,priorityTaskForTargetIdDto.getTasksList().get(0).getPriority());
        assertEquals(Task.Priority.MEDIUM,priorityTaskForTargetIdDto.getTasksList().get(1).getPriority());
        assertEquals(Task.Priority.LOW,priorityTaskForTargetIdDto.getTasksList().get(2).getPriority());
        verify(taskRepository,times(1)).findTaskSortedByPriority(Task.Status.TODO);
    }
    @DisplayName("TODO 상태의 태스크를 우선순위기준으로 정렬")
    @Test
    void getIN_PROGRESSSortedByPriority() {
        //given
        User testUser = new User();
        ReflectionTestUtils.setField(testUser, "id", 10L);

        Task task1 = new Task();
        ReflectionTestUtils.setField(task1, "id", 1L);
        ReflectionTestUtils.setField(task1, "status", Task.Status.IN_PROGRESS);
        ReflectionTestUtils.setField(task1, "priority", Task.Priority.LOW );
        ReflectionTestUtils.setField(task1, "title", "우선순위낮은할일");
        ReflectionTestUtils.setField(task1,"user",testUser);

        Task task2 = new Task();
        ReflectionTestUtils.setField(task2, "id", 2L);
        ReflectionTestUtils.setField(task2, "status", Task.Status.IN_PROGRESS);
        ReflectionTestUtils.setField(task2, "priority", Task.Priority.MEDIUM);
        ReflectionTestUtils.setField(task2, "title", "우선순위중간할일");
        ReflectionTestUtils.setField(task2,"user",testUser);

        Task task3 = new Task();
        ReflectionTestUtils.setField(task3, "id", 3L);
        ReflectionTestUtils.setField(task3, "status", Task.Status.IN_PROGRESS);
        ReflectionTestUtils.setField(task3, "priority", Task.Priority.HIGH);
        ReflectionTestUtils.setField(task3, "title", "우선순위높은할일");
        ReflectionTestUtils.setField(task3,"user",testUser);

        List<Task> mockTasks = Arrays.asList(task3, task2, task1);
        when(taskRepository.findTaskSortedByPriority(Task.Status.IN_PROGRESS)).thenReturn(mockTasks);

        //when
        PriorityTaskForTargetIdDto priorityTaskForTargetIdDto = dashBoardService.inProgressSortedByPriority();

        //then
        assertEquals(Task.Priority.HIGH,priorityTaskForTargetIdDto.getTasksList().get(0).getPriority());
        assertEquals(Task.Priority.MEDIUM,priorityTaskForTargetIdDto.getTasksList().get(1).getPriority());
        assertEquals(Task.Priority.LOW,priorityTaskForTargetIdDto.getTasksList().get(2).getPriority());
        verify(taskRepository,times(1)).findTaskSortedByPriority(Task.Status.IN_PROGRESS);
    }
}