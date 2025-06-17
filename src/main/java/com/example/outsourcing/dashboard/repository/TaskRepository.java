package com.example.outsourcing.dashboard.repository;

import com.example.outsourcing.dashboard.Task;
import com.example.outsourcing.dashboard.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * 
     * @return 삭제되지않은 총 태스크 개수
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.deleted = false")
    Long getTotalCount();

    /**
     * 태스크 상태와 태스크 상태별 태스크의 개수를 조회하는 쿼리
     * @return 리스트에 object(status, long)으로 저장
     */
    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.deleted = false GROUP BY t.status")
    List<Object[]> countTaskByStatus();

    /**
     * 삭제되지 않은 Done 상태의 태스크 개수조회
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.deleted = false AND t.status = DONE")
    Long getCountDoneTasks();

    /**
     * 현재시간이 마감일을 지난상태이며 TODO, IN_PROGRESS 상태인 태스크 개수 조회
     * @return 기한 초과된 태스크 개수
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.deleted = false" +
            "AND(t.status = TODO OR t.status = IN_PROGRESS)" +
            "AND t.endDate < :currentTime")
    Long getCountOverdueTasks(@Param("currentTime") LocalDateTime currentTime);

    /**
     *
     * @param status
     * @return 파라미터 상태에 해당하는 태스크를 우선사항에따라 조회
     */
    List<Task> findByStatusOrderByPriorityAsc(TaskStatus status);
}
