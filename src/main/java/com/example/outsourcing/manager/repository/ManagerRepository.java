package com.example.outsourcing.manager.repository;

import com.example.outsourcing.manager.entity.Manager;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByTaskAndUser(Task task, User targetUser);

    @Modifying
    @Query("UPDATE Manager m SET m.isDeleted = true, m.deletedAt = CURRENT_TIMESTAMP WHERE m.user.id = :userId")
    void softDeleteManagersByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Manager m SET m.isDeleted = true, m.deletedAt = CURRENT_TIMESTAMP WHERE m.task.id IN :taskIds")
    void softDeleteManagersByTaskIds(@Param("taskIds") List<Long> taskIds);
}
