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

 List<Task> findByIsDeletedFalse();

 @Query("""
    SELECT t FROM Task t
    WHERE t.isDeleted = false
      AND (:status IS NULL OR t.status = :status)
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

    /**
     *
     * @return 삭제되지않은 총 태스크 개수
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.isDeleted = false")
    Long getTotalCount();

    /**
     * TODO, IN_PROGRESS, DONE 상태별 테스크 개수 조회
     * @return 리스트에 object(status, long)으로 저장
     */
    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.isDeleted = false GROUP BY t.status")
    List<Object[]> countTaskByStatus();

    /**
     * 전체 태스크 중 완료된 태스크 조회
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.isDeleted = false AND t.status= 'DONE' ")
    Long getCountDoneTasks();

    /**
     * 마감일을지난 TODO 또는 IN_PROGRESS 상태인 태스크 개수 조회
     * @return 기한 초과된 태스크 개수
     */
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.dueDate < CURRENT_TIMESTAMP " +
            "AND (t.status = :todoStatus OR t.status = :inProgressStatus) " +
            "AND t.isDeleted = FALSE")
    long countOverdueTasks(
            @Param("todoStatus") Task.Status todoStatus,
            @Param("inProgressStatus") Task.Status inProgressStatus
    );

    /**
     *
     * @param status
     * @return 파라미터 상태에 해당하는 태스크를 우선사항에따라 조회
     */
    List<Task> findByStatusOrderByPriorityDesc(Task.Status status);


}
