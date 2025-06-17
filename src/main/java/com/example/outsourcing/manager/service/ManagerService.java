package com.example.outsourcing.manager.service;

import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.common.exception.exceptions.NotFoundException;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import com.example.outsourcing.manager.dto.ManagerRequestDto;
import com.example.outsourcing.manager.dto.ManagerResponseDto;
import com.example.outsourcing.manager.entity.Manager;
import com.example.outsourcing.manager.repository.ManagerRepository;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.task.repository.TaskRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ManagerRepository managerRepository;

    public ManagerResponseDto registerManager(String username, Long taskId, ManagerRequestDto requestDto) {
        // 토큰 유저 정보로 유저 찾을 수 없을시 예외처리
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        // 등록하려는 유저 찾을 수 없을시 예외처리
        User targetUser = userRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new NotFoundException("등록할 유저를 찾을 수 없습니다."));

        // 일정을 찾을 수 없을시 예외처리
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("등록할 일정을 찾을 수 없습니다."));

        isValidManagerRequest(user, task, targetUser);

        Manager manager = new Manager(task, targetUser);
        managerRepository.save(manager);

        return new ManagerResponseDto(manager);
    }

    public ManagerResponseDto deleteManager(String username, Long taskId, ManagerRequestDto requestDto) {
        // 토큰 유저 정보로 유저 찾을 수 없을시 예외처리
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        // 등록하려는 유저 찾을 수 없을시 예외처리
        User targetUser = userRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new NotFoundException("등록할 유저를 찾을 수 없습니다."));

        // 일정을 찾을 수 없을시 예외처리
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("등록할 일정을 찾을 수 없습니다."));

        isValidManagerRequest(user, task, targetUser);

        // 취소하려는 담당자 없으면 예외처리, 있으면 삭제
        Manager manager = managerRepository.findByTask(task)
                .orElseThrow(() -> new CustomException(ExceptionCode.MANAGER_NOT_FOUND));
        managerRepository.delete(manager);

        return new ManagerResponseDto(manager);
    }

    // 공통 검증 부분
    private void isValidManagerRequest(User user, Task task, User targetUser) {
        // 삭제된 유저일 때 예외처리
        if(user.isDeleted() || targetUser.isDeleted()) {
            throw new UnauthorizedException("탈퇴한 유저입니다.");
        }

        // 본인이 본인을 task 의 담당자로 등록하려는 경우 예외처리
        if(targetUser.getId().equals(user.getId())){
            throw new IllegalArgumentException("본인은 담당자로 등록할 수 없습니다");
        }

        // 다른사람이 생성한 Task 담당자 등록시 예외처리
        if(!user.getId().equals(task.getUser().getId())){
            throw new UnauthorizedException("본인이 생성한 업무가 아닙니다");
        }
    }
}
