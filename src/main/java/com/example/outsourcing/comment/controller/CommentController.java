package com.example.outsourcing.comment.controller;

import com.example.outsourcing.comment.dto.*;
import com.example.outsourcing.comment.service.CommentService;
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
    public ResponseEntity<CommentResponseDto> commentCreated (@AuthenticationPrincipal(expression = "username") String username,
                                                              @PathVariable("task_id") Long taskId,
                                                              @RequestBody CommentRequestDto requestDto) {

        // 서비스 레이어의 commentCreated메서드에 매개변수 주입
        CommentDataDto response = commentService.commentCreated(username, taskId, requestDto.getComment());

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
    public ResponseEntity<CommentFindAllResponseDto> commentFindAll (@PathVariable("task_id") Long taskId) {

        // 서비스 레이어의 commentFindAll 메서드 호출
        List<CommentDataDto> commentFindAll = commentService.commentFindAll(taskId);

        // response객체 생성
        CommentFindAllResponseDto responseDto = new CommentFindAllResponseDto(
                true,
                "댓글 조회가 완료되었습니다.",
                commentFindAll,
                LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 단건 조회 컨트롤러
    @GetMapping("/comment/{comment_id}")
    public ResponseEntity<CommentResponseDto> commentFindById (@PathVariable("comment_id") Long commentId) {

        commentService.commentFindById(commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 수정 컨트롤러
    @PatchMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> commentUpdate (@PathVariable("comment_id") Long commentId,
                                                             @RequestBody CommentRequestDto requestDto) {

        CommentDataDto response = commentService.commentUpdate(commentId, requestDto.getComment());

        CommentResponseDto responseDto = new CommentResponseDto(
                true,
                "댓글 수정이 완료되었습니다.",
                response,
                LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제 컨트롤러
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<CommentDeleteResponseDto> commentDelete (@PathVariable("comment_id") Long commentId) {

        CommentDeleteDto response = commentService.commentdelete(commentId);

        CommentDeleteResponseDto responseDto = new CommentDeleteResponseDto(
                true,
                "댓글 삭제가 완료되었습니다.",
                response,
                LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
