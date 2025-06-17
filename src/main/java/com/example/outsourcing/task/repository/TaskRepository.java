package com.example.outsourcing.task.repository;

import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

   @Query("""
    SELECT t FROM Task t
    WHERE (:status IS NULL OR t.status = :status)
      AND (
        (:keyword IS NULL OR TRIM(:keyword) = '')
        OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY t.createdAt DESC
""")
   Page<Task> findByCondition(@Param("status") Task.Status status,
                              @Param("keyword") String keyword,
                              Pageable pageable);
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    default Task findByIdOrElseThrow(Long taskId) {
        return findById(taskId)
                .orElseThrow(() ->
                        new CustomException(ExceptionCode.TASK_NOT_FOUND));
    }

    @Modifying
    @Query("UPDATE Task t SET t.isDeleted = true, t.deletedAt = CURRENT_TIMESTAMP WHERE t.user.id = :userId")
    void softDeleteTasksByUserId(@Param("userId") Long userId);

    // 상태로 필터링
    List<Task> findByStatus(Task.Status status);

    // 제목 또는 내용
    List<Task> findByTitleContainingOrContentContaining(String title, String content);



}
