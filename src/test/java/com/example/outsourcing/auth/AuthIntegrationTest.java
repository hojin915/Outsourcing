package com.example.outsourcing.auth;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.user.dto.request.UserLoginRequestDto;
import com.example.outsourcing.user.dto.request.UserSignupRequestDto;
import com.example.outsourcing.user.dto.response.UserProfileResponseDto;
import com.example.outsourcing.user.dto.response.UserSignupResponseDto;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USERNAME = "authTestUser";
    private static final String EMAIL = "authtestuser@example.com";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "auth test user ";
    private static final UserRole USER_ROLE = UserRole.USER;

    @DisplayName("만료된 토큰이 들어오면 예외가 발생한다")
    @Test
    void exception_test_EXPIRED_JWT_TOKEN() throws Exception {
        // 1. given
        // 임의의 데이터로 만든 만료 토큰
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiItMSIsInVzZXJuYW1lIjoi66eM66OM65CcIO2GoO2BsOqwkiIsInVzZXJSb2xlIjoiVVNFUiIsImV4cCI6MTc1MDIyNTk1OCwiaWF0IjoxNzUwMjI5NTU4fQ.z5_-K3LAUN6ZEwnHS6fFMAiCnoTJH0wjZEe88XfixSo";

        // 2. when
        ResultActions getProfile = mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token));

        String getProfileAsString = getProfile.andReturn()
                .getResponse()
                .getContentAsString();

        String data = objectMapper.readTree(getProfileAsString).toString();

        ErrorResponseDto response = objectMapper.readValue(data, ErrorResponseDto.class);

        // 3. then
        getProfile.andExpect(status().isUnauthorized());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals(ExceptionCode.EXPIRED_JWT_TOKEN.getMessage(), response.getMessage());
        assertEquals(ExceptionCode.EXPIRED_JWT_TOKEN.toString(), response.getErrorCode());
    }

    @DisplayName("유효하지 않은 토큰이 들어오면 예외가 발생한다")
    @Test
    void exception_test_INVALID_JWT_SIGNATURE() throws Exception {
        // 1. given
        String token = "asease.asease.asease";

        // 2. when
        ResultActions getProfile = mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token));

        String getProfileAsString = getProfile.andReturn()
                .getResponse()
                .getContentAsString();

        String data = objectMapper.readTree(getProfileAsString).toString();

        ErrorResponseDto response = objectMapper.readValue(data, ErrorResponseDto.class);

        // 3. then
        getProfile.andExpect(status().isUnauthorized());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals(ExceptionCode.INVALID_JWT_SIGNATURE.getMessage(), response.getMessage());
        assertEquals(ExceptionCode.INVALID_JWT_SIGNATURE.toString(), response.getErrorCode());
    }

    @DisplayName("토큰이 없으면 예외가 발생한다")
    @Test
    void exception_test_UNAUTHORIZED_API_REQUEST() throws Exception {
        // 1. given

        // 2. when
        ResultActions getProfile = mockMvc.perform(get("/api/users/me"));

        String getProfileAsString = getProfile.andReturn()
                .getResponse()
                .getContentAsString();

        String data = objectMapper.readTree(getProfileAsString).toString();

        ErrorResponseDto response = objectMapper.readValue(data, ErrorResponseDto.class);

        // 3. then
        getProfile.andExpect(status().isUnauthorized());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals(ExceptionCode.UNAUTHORIZED_API_REQUEST.getMessage(), response.getMessage());
        assertEquals(ExceptionCode.UNAUTHORIZED_API_REQUEST.toString(), response.getErrorCode());
    }

    @DisplayName("토큰발급 확인 및 프로필 조회 성공")
    @Test
    void checkTokenAndGetProfile() throws Exception {
        // given
        UserSignupResponseDto signupResult = doSignup();
        String token = getTokenByLogin(signupResult.getUsername());

        // 2. when
        ResultActions getProfile = mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token));

        String getProfileAsString = getProfile.andReturn()
                .getResponse()
                .getContentAsString();

        String data = objectMapper.readTree(getProfileAsString)
                .get("data")
                .toString();

        UserProfileResponseDto response = objectMapper.readValue(data, UserProfileResponseDto.class);

        // 3. then
        getProfile.andExpect(status().isOk());
        assertThat(response.getId()).isGreaterThan(0L);
        assertEquals(USERNAME, response.getUsername());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(USER_ROLE, response.getUserRole());
    }

    @DisplayName("토큰발급 확인 및 회원탈퇴 성공")
    @Test
    void checkTokenAndWithdraw() throws Exception {
        // 1. given
        UserSignupResponseDto signupResult = doSignup();
        String token = getTokenByLogin(signupResult.getUsername());
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto(PASSWORD);

        // 2. when
        ResultActions withdraw = mockMvc.perform(post("/api/auth/withdraw")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)));

        // 3. then
        Optional<User> user = userRepository.findByEmail(EMAIL);

        withdraw.andExpect(status().isOk());
        assertTrue(user.isPresent());
        assertTrue(user.get().isDeleted());
    }

    private UserSignupResponseDto doSignup() throws Exception {
        UserSignupRequestDto requestDto = new UserSignupRequestDto(USERNAME, EMAIL, PASSWORD, NAME);

        String signupAsString = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String data = objectMapper.readTree(signupAsString)
                .get("data")
                .toString();

        return objectMapper.readValue(data, UserSignupResponseDto.class);
    }

    private String getTokenByLogin(String username) throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto(username, PASSWORD);

        // xxx : 프론트 연결 api로 변경시 같이 변경 필요
        String signupAsString = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(signupAsString)
                .get("data")
                .get("token")
                .asText();
    }


}