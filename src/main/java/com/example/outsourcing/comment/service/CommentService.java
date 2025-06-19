package com.example.outsourcing.comment.service;

import com.example.outsourcing.comment.dto.*;
import com.example.outsourcing.comment.dto.CommentListResponseDto;
import com.example.outsourcing.comment.dto.CommentSearchResponseDto;
import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.outsourcing.common.exception.exceptions.ExceptionCode.*;


@Service
public class CommentService {

    // CommentRepository 의존성 주입(DI)
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    // 댓글 생성 비지니스 로직
    @Transactional
    public CommentDataDto commentCreated(Long userId, Long taskId, String comment) {

        // 유저 예외처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 태스크 예외처리
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(comment == null || comment.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        // 댓글 생성
        Comment newComment = new Comment(task, user, comment);

        // DB에 생성된 댓글 저장
        Comment saveComment = commentRepository.save(newComment);

        // 반환 객체 생성 후 반환
        // Comment객체를 CommentDataDto객체로 변환하며 targetId 삽입
        return CommentDataDto.toDto(saveComment, saveComment.getCommentId());
    }

    // 태스크 댓글 전체 조회 비지니스 로직
    @Transactional
    public CommentListResponseDto  commentFindAll(Long taskId, int page, int size, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 태스크가 존재하지 않을 때 예외처리
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        List<CommentListResponseDto> commentList =
                commentRepository.findAllByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(task.getId())
                .stream()
                .map(comment -> CommentListResponseDto.fromEntity(comment))
                .toList();

        return CommentListResponseDto.fromList(commentList, authUser.getId());
    }

    // 댓글 단건 조회 비지니스 로직
    @Transactional
    public CommentDataDto commentFindById(Long commentId) {

        Comment findByIdComment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId);

        // 조회한 댓글이 존재하지 않을 경우 예외처리
        if (findByIdComment == null) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        return CommentDataDto.toDto(findByIdComment, findByIdComment.getCommentId());
    }

    // 댓글 수정 비지니스 로직
    @Transactional
    public CommentDataDto commentUpdate(Long userId, Long commentId, String comment) {

        Comment commentUpdate = commentRepository.findByCommentIdAndIsDeletedFalse(commentId);

        if (commentUpdate == null) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(comment == null || comment.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        // 입력받은 값이 기존 값과 같을 경우 예외처리
        if(comment.equals(commentUpdate.getComment())) {
            throw new CustomException(COMMENT_UPDATED_BAD_REQUEST);
        }

        // 내가 작성한 댓글이 아닐 경우 예외처리
        if (!userId.equals(commentUpdate.getUser().getId())) {
            throw new CustomException(COMMENT_AUTHOR_MISMATCH);
        }

        commentUpdate.setComment(comment);

        return CommentDataDto.toDto(commentUpdate, commentUpdate.getCommentId());
    }

    // 댓글 삭제 비지니스 로직
    @Transactional
    public CommentDeleteDto commentdelete(Long userId, Long taskId, Long commentId) {

        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId);

        // 댓글이 존재하지 않을 경우 예외처리
        if (comment == null) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        // 내가 작성한 댓글이 아닐 경우 예외처리
        if (!userId.equals(comment.getUser().getId())) {
            throw new CustomException(COMMENT_AUTHOR_MISMATCH);
        }

        comment.setDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());

        return CommentDeleteDto.toDto(comment, comment.getCommentId());
    }

    // 태스크별 댓글 검색 비지니스 로직
    @Transactional
    public CommentSearchResponseDto commentFindTaskSearch(Long taskId, String search, AuthUser authUser) { // AuthUser 파라미터 추가
        // 태스크가 존재하지 않을 때 예외처리
        taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(search == null || search.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        List<CommentSearchResponseDto> searchResults = commentRepository.findByTaskIdAndSearch(taskId, search)
                .stream()
                .map(comment -> CommentSearchResponseDto.fromEntity(comment)) // 개별 댓글 DTO 생성
                .toList();

        return CommentSearchResponseDto.fromList(searchResults, authUser.getId());

    }

    // 전체 댓글 검색 비지니스 로직
    @Transactional
    public CommentAllSearchResponseDto commentfindAllSearch(String search, AuthUser authUser) {

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(search == null || search.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        List<CommentAllSearchResponseDto> searchResults = commentRepository.findAllSearch(search)
                .stream()
                .map(comment -> CommentAllSearchResponseDto.fromEntity(comment))
                .toList();

        return CommentAllSearchResponseDto.fromList(searchResults, authUser.getId());
    }

    public void softDeleteComments(List<Long> taskIds) {
        commentRepository.softDeleteCommentsByTaskIds(taskIds);
    }
}