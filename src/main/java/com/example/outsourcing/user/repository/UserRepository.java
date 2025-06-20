package com.example.outsourcing.user.repository;

import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Override
    @NonNull
    Optional<User> findById(@NonNull Long userId);

    default User findByUsernameOrElseThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() ->
                        new CustomException(ExceptionCode.USER_NOT_FOUND));
    }

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() ->
                        new CustomException(ExceptionCode.USER_NOT_FOUND));
    }
}