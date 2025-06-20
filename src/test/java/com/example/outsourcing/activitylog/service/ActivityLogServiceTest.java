package com.example.outsourcing.activitylog.service;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.repository.ActivityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {
    @Mock
    private ActivityLogRepository activityLogRepository;

    @InjectMocks
    private ActivityLogService activityLogService;

    private ActivityLog activityLog;

    @BeforeEach
    void setUp() {
        activityLog=ActivityLog.builder()
                .id(12L)
                .userId(6L)
                .requestTime(LocalDateTime.now())
                .ipAddress("0.0.0.0.0")
                .requestMethod("POST")
                .requestUrl("/api/asdf/a")
                .activityType("CREATE_TASK")
                .targetId(6L)
                .executionTimeMs(65L)
                .build();

    }

    @Test
    @DisplayName("활동 로그 저장 테스트")
    void saveActivityLog() {

        // when
        activityLogService.saveActivityLog(activityLog);

        // then
        verify(activityLogRepository, times(1)).save(activityLog);
        // 다른 save 호출은 없었는지 확인합니다.
        verifyNoMoreInteractions(activityLogRepository);
    }
}