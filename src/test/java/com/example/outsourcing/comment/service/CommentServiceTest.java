package com.example.outsourcing.comment.service;

import com.example.outsourcing.comment.dto.CommentDataDto;
import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.comment.repository.CommentRepository;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @Test
    void 댓글_생성_서비스_단위_테스트() {

        // given
        Long userId = 1L;
        Long taskId = 1L;
        String testText = "테스트 댓글입니다.";

        User user = new User("test", "test@test.test", "1Q2w3e4r!", "테스트", UserRole.USER);
        Task task = new Task();
        Comment comment = new Comment(task, user, testText);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentDataDto commentDataDto = commentService.commentCreated(userId, taskId, testText);

        // then
        assertNotNull("테스트 실패", commentDataDto.getComment());
        assertEquals("댓글 불일치", testText, commentDataDto.getComment());
    }


}
