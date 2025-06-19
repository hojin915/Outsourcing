package com.example.outsourcing.comment.repository;

import com.example.outsourcing.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(Long taskId, Pageable pageable);

    Comment findByCommentIdAndIsDeletedFalse(Long commentId);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.task.id = :taskId " +
            "AND c.isDeleted = false " +
            "AND LOWER(c.comment) " +
            "LIKE LOWER(CONCAT('%',:search,'%')) " +
            "ORDER BY c.createdAt DESC ")
    Page<Comment> findByTaskIdAndSearch  (Long taskId, Pageable pageable, String search);

    @Query("SELECT c" +
            " FROM Comment c " +
            "WHERE c.isDeleted = false " +
            "AND LOWER(c.comment) " +
            "LIKE LOWER(CONCAT('%',:search,'%')) " +
            "ORDER BY c.createdAt DESC ")
    Page<Comment> findAllSearch  (Pageable pageable, String search);

    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.deletedAt = CURRENT_TIMESTAMP WHERE c.user.id = :userId")
    void softDeleteCommentsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.deletedAt = CURRENT_TIMESTAMP WHERE c.task.id IN :taskIds")
    void softDeleteCommentsByTaskIds(@Param("taskIds") List<Long> taskIds);
}