package com.example.outsourcing.comment.controller;

import com.example.outsourcing.comment.dto.*;
import com.example.outsourcing.comment.service.CommentService;
import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    // CommentService 의존성 주입(DI)
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성 컨트롤러
    @PostMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentCreated(@AuthenticationPrincipal AuthUser user,
                                                                      @PathVariable("task_id") Long taskId,
                                                                      @RequestBody CommentRequestDto requestDto) {


        // 서비스 레이어의 commentCreated메서드에 매개변수 주입
        CommentDataDto response = commentService.commentCreated(user.getId(), taskId, requestDto.getComment());

        // response객체 생성
        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 생성이 완료되었습니다.",
                response
        );

        // 반환
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ResponseDto<CommentListResponseDto>> commentFindAll(
            @PathVariable("task_id") Long taskId,
            @AuthenticationPrincipal AuthUser user
            ) {


        // 서비스 레이어의 commentFindAll 메서드 호출
        CommentListResponseDto commentListResponse = commentService.commentFindAll(taskId, user);

        // response객체 생성
        ResponseDto<CommentListResponseDto> responseDto = new ResponseDto<>(
                "댓글 조회가 완료되었습니다.",
                commentListResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 단건 조회 컨트롤러
    @GetMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentFindById(@PathVariable("comment_id") Long commentId) {


        CommentDataDto commentFindById = commentService.commentFindById(commentId);

        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 단건 조회가 완료되었습니다.",
                commentFindById
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 수정 컨트롤러
    @PatchMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentUpdate(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable("comment_id") Long commentId,
            @RequestBody CommentRequestDto requestDto) {


        CommentDataDto response = commentService.commentUpdate(user.getId(), commentId, requestDto.getComment());

        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 수정이 완료되었습니다.",
                response
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제 컨트롤러
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<CommentDeleteDto>> commentDelete(@AuthenticationPrincipal AuthUser user,
                                                                       @PathVariable("comment_id") Long commentId) {


        CommentDeleteDto response = commentService.commentdelete(user.getId(), commentId);

        ResponseDto<CommentDeleteDto> responseDto = new ResponseDto<>(
                "댓글 삭제가 완료되었습니다.",
                response
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 댓글 검색 기능
    @GetMapping("/tasks/{task_id}/comments/search")
    public ResponseEntity<ResponseDto<CommentSearchResponseDto>> commentFindTaskSearch(
            @PathVariable("task_id") Long taskId,
            @RequestBody CommentSearchRequestDto requestDto,
            @AuthenticationPrincipal AuthUser user ) {


        CommentSearchResponseDto commentSearchResponse = commentService.commentFindTaskSearch(taskId, requestDto.getSearch(), user);

        ResponseDto<CommentSearchResponseDto> responseDto = new ResponseDto<>(
                "댓글 검색이 완료되었습니다.",
                commentSearchResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 전체 댓글 검색 기능
    @GetMapping("/comments/search")
    public ResponseEntity<ResponseDto<CommentAllSearchResponseDto>> commentfindAllSearch(
            @RequestParam("search") String searchKeyword,
            @AuthenticationPrincipal AuthUser user
    ) {
        CommentAllSearchResponseDto commentSearchResponse = commentService.commentfindAllSearch(searchKeyword, user);


        // response객체 생성
        ResponseDto<CommentAllSearchResponseDto> responseDto = new ResponseDto<>(
                "댓글 검색이 완료되었습니다.",
                commentSearchResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}