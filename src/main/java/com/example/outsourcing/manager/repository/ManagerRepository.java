package com.example.outsourcing.manager.repository;

import com.example.outsourcing.manager.entity.Manager;
import com.example.outsourcing.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByTask(Task task);

    @Modifying
    @Query("UPDATE Manager m SET m.isDeleted = true, m.deletedAt = CURRENT_TIMESTAMP WHERE m.user.id = :taskId")
    void softDeleteManagersByTaskId(@Param("taskId") Long taskId);
}
