package com.example.outsourcing.activitylog.repository;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    Page<ActivityLog> findByUserId(Long userId, Pageable pageable);

    Page<ActivityLog> findByUserIdAndActivityType(Long userId, String activityType, Pageable pageable);

    Page<ActivityLog> findByUserIdAndTargetId(Long userId, Long targetId, Pageable pageable);

    Page<ActivityLog> findByUserIdAndActivityTypeAndTargetId(Long userId, String activityType, Long targetId, Pageable pageable);
}