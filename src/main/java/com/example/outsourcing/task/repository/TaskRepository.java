package com.example.outsourcing.task.repository;

import com.example.outsourcing.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 상태로 필터링
    List<Task> findByStatus(Task.Status status);

    // 제목 또는 내용
    List<Task> findByTitleContainingOrContentContaining(String title, String content);
}
