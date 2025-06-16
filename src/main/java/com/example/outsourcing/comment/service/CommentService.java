package com.example.outsourcing.comment.service;

import com.example.outsourcing.comment.dto.CommentDataDto;
import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CommentService {

    // CommentRepository 의존성 주입(DI)
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 생성 비지니스 로직
    @Transactional
    public CommentDataDto commentCreated(Long userId, Long taskId, String comment) {

        // 유저 예외처리
        User user = new User();

        // 댓글 생성할 테스크 호출
        // 태스크 예외처리 (댓글 생성하려는 태스크가 존재하지 않을 경우)
        Task task = new Task();

        // 댓글 생성
        Comment newComment = new Comment(task, user, comment);

        // DB에 생성된 댓글 저장
        commentRepository.save(newComment);

        // 반환 객체 생성 후 반환
        return new CommentDataDto(
                newComment.getCommentId(),
                newComment.getTask().getId(),
                newComment.getUser().getId(),
                newComment.getComment(),
                newComment.getCreatedAt(),
                newComment.getModifiedAt()
        );
    }

    // 태스크 댓글 전체 조회 비지니스 로직
    @Transactional
    public List<CommentDataDto> commentFindAll(Long taskId) {
        // 태스크 예외처리 (댓글 조회하려는 태스크가 존재하지 않을 경우)

        return commentRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId)
                .stream()
                .map(CommentDataDto::toDto) // Comment클래스에 정의한 toDto 메서드를 사용해 Comment객체를 CommentDataDto타입으로 변환
                .toList();
    }

    // 댓글 단건 조회 비지니스 로직
    @Transactional
    public CommentDataDto commentFindById(Long commentId) {

        Comment findByIdComment = commentRepository.findById(commentId).get();

        return CommentDataDto.toDto(findByIdComment);
    }
}
