package com.example.outsourcing.activitylog.service;

import com.example.outsourcing.activitylog.dto.ActivityLogResponseDto;
import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public void saveActivityLog(ActivityLog activityLog) {
        activityLogRepository.save(activityLog);
    }

    public Page<ActivityLogResponseDto> getUserActivities(
            Long userId,
            String activityType,
            Long targetId,
            String sortBy,
            String sortOrder,
            Pageable pageable) {
        // 정렬기준
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        // 페이징
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<ActivityLog> activityLogs;

        if (activityType != null && targetId != null) {
            activityLogs = activityLogRepository.findByUserIdAndActivityTypeAndTargetId(userId, activityType, targetId, sortedPageable);
        } else if (activityType != null) {
            activityLogs = activityLogRepository.findByUserIdAndActivityType(userId, activityType, sortedPageable);
        } else if (targetId != null) {
            activityLogs = activityLogRepository.findByUserIdAndTargetId(userId, targetId, sortedPageable);
        } else {
            activityLogs = activityLogRepository.findByUserId(userId, sortedPageable);
        }


        return activityLogs.map(ActivityLogResponseDto::fromEntity);
    }

}












