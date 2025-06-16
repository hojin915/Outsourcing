package com.example.outsourcing.comment.controller;

import com.example.outsourcing.comment.dto.CommentDataDto;
import com.example.outsourcing.comment.dto.CommentRequestDto;
import com.example.outsourcing.comment.dto.CommentResponseDto;
import com.example.outsourcing.comment.service.CommentService;
import com.example.outsourcing.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    // CommentService 의존성 주입(DI)
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성 컨트롤러
    @PostMapping("/{task_id}")
    public ResponseEntity<CommentResponseDto> commentCreated (@AuthenticationPrincipal User user,
                                                              @PathVariable("task_id") Long taskId,
                                                              @RequestBody CommentRequestDto requestDto) {

        Long userId = user.getId();

        // 서비스 레이어의 commentCreated메서드에 매개변수 주입
        CommentDataDto response = commentService.commentCreated(userId, taskId, requestDto.getComment());

        // response객체 생성
        CommentResponseDto responseDto = new CommentResponseDto(
                true,
                "댓글 생성이 완료되었습니다.",
                response,
                LocalDateTime.now());

        // 반환
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 태스크 댓글 전체 조회 컨트롤러
    @GetMapping("/{task_id}")
    public ResponseEntity<CommentResponseDto> commentFindAll (@PathVariable("task_id") Long taskId) {

        // 서비스 레이어의 commentFindAll 메서드 호출
        List<CommentDataDto> commentFindAll = commentService.commentFindAll(taskId);

        // response객체 생성
        CommentResponseDto responseDto = new CommentResponseDto(
                true,
                "댓글 조회가 완료되었습니다.",
                commentFindAll,
                LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 단건 조회 컨트롤러
    @GetMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> commentFindById (@PathVariable("comment_id") Long commentId) {

        commentService.commentFindById(commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
