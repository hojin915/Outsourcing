package com.example.outsourcing.activitylog.repository;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}