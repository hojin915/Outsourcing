//package com.example.outsourcing.comment.service;
//
//import com.example.outsourcing.comment.dto.CommentDataDto;
//import com.example.outsourcing.comment.dto.CommentDeleteDto;
//import com.example.outsourcing.comment.entity.Comment;
//import com.example.outsourcing.comment.repository.CommentRepository;
//import com.example.outsourcing.common.enums.UserRole;
//import com.example.outsourcing.common.exception.exceptions.CustomException;
//import com.example.outsourcing.task.entity.Task;
//import com.example.outsourcing.task.repository.TaskRepository;
//import com.example.outsourcing.user.entity.User;
//import com.example.outsourcing.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.util.AssertionErrors.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CommentServiceTest {
//
//    @InjectMocks
//    CommentService commentService;
//
//    @Mock
//    CommentRepository commentRepository;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    TaskRepository taskRepository;
//
//    @Test
//    void 댓글_생성_서비스_단위_테스트() {
//
//        // given
//        Long userId = 1L;
//        Long taskId = 1L;
//        String testText = "테스트 댓글입니다.";
//
//        User user = new User("test", "test@test.test", "1Q2w3e4r!", "테스트", UserRole.USER);
//        Task task = new Task(taskId);
//        Comment comment = new Comment(task, user, testText);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
//        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
//
//        // when
//        CommentDataDto commentDataDto = commentService.commentCreated(userId, taskId, testText);
//
//        // then
//        assertNotNull("테스트 실패", commentDataDto.getComment());
//        assertEquals("댓글 불일치", testText, commentDataDto.getComment());
//    }
//
//    @Test
//    void 태스크별_댓글_전체_조회_서비스_단위_테스트() {
//
//        // given
//        Long taskId = 1L;
//        User user = new User("test", "test@test.test", "1Q2w3e4r!", "테스트", UserRole.USER);
//        Task task = new Task(taskId);
//
//        Comment comment1 = new Comment(task, user, "댓글1");
//        Comment comment2 = new Comment(task, user, "댓글2");
//
//        List<Comment> commentList = List.of(comment1, comment2);
//
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
//        when(commentRepository.findAllByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(taskId))
//                .thenReturn(commentList);
//
//        // when
//        List<CommentDataDto> commentFindAll = commentService.commentFindAll(taskId);
//
//        // then
//        assertTrue("리스트에 데이터가 없습니다.", commentFindAll.size() > 0);
//
//    }
//
//    @Test
//    void 댓글_단건_조회_서비스_단위_테스트() {
//
//        // given
//        Long userId = 1L;
//        Long taskId = 1L;
//        Long commentId = 1L;
//        String testText = "테스트 댓글입니다.";
//
//        User user = new User(userId);
//        Task task = new Task(taskId);
//        Comment comment = new Comment(task, user, testText);
//
//        when(commentRepository.findByCommentIdAndIsDeletedFalse(commentId)).thenReturn(comment);
//
//        // when
//        CommentDataDto commentFindById = commentService.commentFindById(commentId);
//
//        // then
//        assertNotNull("댓글이 존재하지 않습니다.", commentFindById);
//
//    }
//
//    @Test
//    void 댓글_수정_서비스_단위_테스트() {
//
//        // given
//        Long userId = 1L;
//        Long taskId = 1L;
//        Long commentId = 1L;
//        String testText = "테스트 수정 댓글입니다.";
//
//        User user = new User(userId);
//        Task task = new Task(taskId);
//        Comment comment = new Comment(task, user, "수정 전 댓글입니다.");
//
//        when(commentRepository.findByCommentIdAndIsDeletedFalse(commentId)).thenReturn(comment);
//
//        // when
//        CommentDataDto commentFindById = commentService.commentUpdate(userId, commentId, testText);
//
//        // then
//        assertEquals("수정되지 않았습니다.", commentFindById.getComment(), comment.getComment());
//
//    }
//
//    @Test
//    void 댓글_삭제_서비스_단위_테스트() {
//
//        // given
//        Long userId = 1L;
//        Long taskId = 1L;
//        Long commentId = 1L;
//        String testText = "테스트 댓글입니다.";
//
//        User user = new User(userId);
//        Task task = new Task(taskId);
//        Comment comment = new Comment(task, user, testText);
//
//        when(commentRepository.findByCommentIdAndIsDeletedFalse(commentId)).thenReturn(comment);
//
//        // when
//        CommentDeleteDto commentFindById = commentService.commentdelete(userId, commentId);
//
//        // then
//        assertEquals("수정되지 않았습니다.", commentFindById.getComment(), comment.getComment());
//
//    }
//
//    @Test
//    void 댓글_태스크별_검색_서비스_단위_테스트() {
//
//        // given
//        Long taskId = 1L;
//        String testSearch = "테스트 검색내용입니다.";
//        Task task = new Task(taskId);
//
//
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
//
//        // when
//        List<CommentDataDto> commentFindTaskSearch = commentService.commentFindTaskSearch(taskId, testSearch);
//
//        // then
//        assertNotNull("검색 결과가 존재하지 않습니다.", commentFindTaskSearch);
//    }
//
//    @Test
//    void 댓글_전체_검색_서비스_단위_테스트() {
//
//        // given
//        String testSearch = "테스트 검색내용입니다.";
//
//        // when
//        List<CommentDataDto> commentFindAllSearch = commentService.commentfindAllSearch(testSearch);
//
//        // then
//        assertNotNull("검색 결과가 존재하지 않습니다.", commentFindAllSearch);
//    }
//
//
//}