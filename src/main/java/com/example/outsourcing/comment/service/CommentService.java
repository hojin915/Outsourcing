package com.example.outsourcing.comment.service;

import com.example.outsourcing.comment.dto.CommentDataDto;
import com.example.outsourcing.comment.dto.CommentDeleteDto;
import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import com.example.outsourcing.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        commentRepository.save(newComment);

        // 반환 객체 생성 후 반환
        return new CommentDataDto(
                newComment.getCommentId(),
                newComment.getTask().getId(),
                newComment.getUser().getId(),
                newComment.getUser().getUsername(),
                newComment.getComment(),
                newComment.getCreatedAt(),
                newComment.getUpdatedAt()
        );
    }

    // 태스크 댓글 전체 조회 비지니스 로직
    @Transactional
    public List<CommentDataDto> commentFindAll(Long taskId) {

        // 태스크가 존재하지 않을 때 예외처리
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        return commentRepository.findAllByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(task.getId())
                .stream()
                .map(CommentDataDto::toDto) // Comment클래스에 정의한 toDto 메서드를 사용해 Comment객체를 CommentDataDto타입으로 변환
                .toList();
    }

    // 댓글 단건 조회 비지니스 로직
    @Transactional
    public CommentDataDto commentFindById(Long commentId) {

        Comment findByIdComment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId);

        // 조회한 댓글이 존재하지 않을 경우 예외처리
        if (findByIdComment == null) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        return CommentDataDto.toDto(findByIdComment);
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

        return CommentDataDto.toDto(commentUpdate);
    }

    // 댓글 삭제 비지니스 로직
    @Transactional
    public CommentDeleteDto commentdelete(Long userId, Long commentId) {

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

        return CommentDeleteDto.toDto(comment);
    }

    // 태스크별 댓글 검색 비지니스 로직
    @Transactional
    public List<CommentDataDto> commentFindTaskSearch(Long taskId, String search) {
        // 태스크가 존재하지 않을 때 예외처리
        taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(search == null || search.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        return commentRepository.findByTaskIdAndSearch(taskId, search)
                .stream()
                .map(CommentDataDto::toDto)
                .toList();
    }

    // 전체 댓글 검색 비지니스 로직
    @Transactional
    public List<CommentDataDto> commentfindAllSearch(String search) {

        // 입력받은 값이 null이거나 내용이 공백일 경우 예외처리
        if(search == null || search.trim().isEmpty()) {
            throw new CustomException(COMMENT_BAD_REQUEST);
        }

        return commentRepository.findAllSearch(search)
                .stream()
                .map(CommentDataDto::toDto)
                .toList();
    }
}