package com.example.outsourcing.user.repository;

import com.example.outsourcing.common.exception.exceptions.NotFoundException;
import com.example.outsourcing.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Override
    @NonNull
    Optional<User> findById(@NonNull Long userId);

    // 아이디 재사용 가능한 경우 사용
    default User findByUsernameOrElseThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException("유저를 찾을 수 없습니다."));
    }

    // 아이디 재사용 가능한 경우 사용
    @Query("SELECT u FROM User u where u.username = :username AND u.isDeleted = false")
    Optional<User> findByUsernameIsNotDeleted(@Param("username") String username);
}
