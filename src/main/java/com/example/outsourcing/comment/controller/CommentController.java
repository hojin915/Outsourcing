package com.example.outsourcing.comment.controller;

import com.example.outsourcing.comment.dto.*;
import com.example.outsourcing.comment.service.CommentService;
import com.example.outsourcing.common.dto.ResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
public class CommentController {

    // CommentService 의존성 주입(DI)
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성 컨트롤러
    // POST /api/tasks/{taskId}/comments
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentCreated(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long taskId,
            @RequestBody CommentRequestDto requestDto
    ) {
        // 서비스 레이어의 commentCreated메서드에 매개변수 주입
        CommentDataDto response = commentService.commentCreated(user.getId(), taskId, requestDto.getContent());

        // response객체 생성
        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>("댓글이 생성되었습니다.", response);

        // 반환
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 태스크 댓글 전체 조회 컨트롤러
    // GET /api/tasks/{taskId}/comments?page=0&size=10
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<ResponseDto<CommentListResponseDto>> commentFindAll(
            @PathVariable Long taskId,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal AuthUser user
    ) {

        // 서비스 레이어의 commentFindAll 메서드 호출
        CommentListResponseDto commentPageResponse = commentService.commentFindAll(taskId, pageable, user);

        // response객체 생성
        ResponseDto<CommentListResponseDto> responseDto = new ResponseDto<>(
                "댓글 조회가 완료되었습니다.",
                commentPageResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 단건 조회 컨트롤러
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentFindById(@PathVariable Long commentId) {


        CommentDataDto commentFindById = commentService.commentFindById(commentId);

        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 단건 조회가 완료되었습니다.",
                commentFindById
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 수정 컨트롤러
    @PatchMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentUpdate(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {


        CommentDataDto response = commentService.commentUpdate(user.getId(), taskId, commentId, requestDto.getContent());

        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 수정이 완료되었습니다.",
                response
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제 컨트롤러
    //DELETE /api/tasks/{taskId}/comments/{commentId}
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<CommentDataDto>> commentDelete(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long taskId,
            @PathVariable Long commentId
    ) {
        commentService.commentDelete(user.getId(), taskId, commentId);

        ResponseDto<CommentDataDto> responseDto = new ResponseDto<>(
                "댓글 삭제가 완료되었습니다.",
                null
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 댓글 검색 기능
    @GetMapping("/{taskId}/comments/search")
    public ResponseEntity<ResponseDto<CommentSearchResponseDto>> commentFindTaskSearch(
            @PathVariable Long taskId,
            @PageableDefault Pageable pageable,
            @RequestParam("search") CommentSearchRequestDto requestDto,
            @AuthenticationPrincipal AuthUser user) {


        CommentSearchResponseDto commentPageResponse = commentService.commentFindTaskSearch(taskId, pageable, requestDto.getSearch(), user);

        ResponseDto<CommentSearchResponseDto> responseDto = new ResponseDto<>(
                "댓글 검색이 완료되었습니다.",
                commentPageResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 전체 댓글 검색 기능
    @GetMapping("/comments/search")
    public ResponseEntity<ResponseDto<CommentAllSearchResponseDto>> commentfindAllSearch(
            @RequestParam("search") String searchKeyword,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal AuthUser user
    ) {
        CommentAllSearchResponseDto commentSearchResponse = commentService.commentfindAllSearch(pageable, searchKeyword, user);


        // response객체 생성
        ResponseDto<CommentAllSearchResponseDto> responseDto = new ResponseDto<>(
                "댓글 검색이 완료되었습니다.",
                commentSearchResponse
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}