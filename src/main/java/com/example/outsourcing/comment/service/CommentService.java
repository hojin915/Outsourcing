package com.example.outsourcing.comment.service;

import com.example.outsourcing.comment.dto.CommentDataDto;
import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import com.example.outsourcing.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {

    // CommentRepository 의존성 주입(DI)
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserService userService, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    // 댓글 생성 비지니스 로직
    @Transactional
    public CommentDataDto commentCreated(String username, Long taskId, String comment) {

        // 유저 예외처리
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException()
                );


        // 댓글 생성할 태스크 호출
        // 태스크 예외처리 (댓글 생성하려는 태스크가 존재하지 않을 경우)
        Task task = taskRepository.findById(taskId);

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

    // 댓글 수정 비지니스 로직
    @Transactional
    public CommentDataDto commentUpdate(Long commentId, String comment) {

        Comment commentUpdate = commentRepository.findById(commentId).get();

        commentUpdate.setComment(comment);

        return CommentDataDto.toDto(commentUpdate);
    }
}
