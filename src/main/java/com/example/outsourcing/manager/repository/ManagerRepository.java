package com.example.outsourcing.manager.repository;

import com.example.outsourcing.manager.entity.Manager;
import com.example.outsourcing.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByTask(Task task);
}
