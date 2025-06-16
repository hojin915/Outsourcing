package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                false,
                "인증되지 않은 URL 요청입니다. : " + authException.getMessage(),
                null,
                status.getReasonPhrase(),
                LocalDateTime.now().toString()
        );
        String responseBody = objectMapper.writeValueAsString(errorResponseDto);

        log.error("UNAUTHORIZED: Not Authenticated Request - Uri : {}", request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}