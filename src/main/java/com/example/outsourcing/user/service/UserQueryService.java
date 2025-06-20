package com.example.outsourcing.user.service;

import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User findByUsernameOrElseThrow(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if(user.isDeleted()){
                        throw new CustomException(ExceptionCode.DELETED_USERNAME);
                    }
                    return user;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }

    public User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    if(user.isDeleted()) {
                        throw new CustomException(ExceptionCode.DELETED_USER);
                    }
                    return user;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }
}
