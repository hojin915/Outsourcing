package com.example.outsourcing.activitylog.service;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public void saveActivityLog(ActivityLog activityLog) {
        activityLogRepository.save(activityLog);
    }
}
