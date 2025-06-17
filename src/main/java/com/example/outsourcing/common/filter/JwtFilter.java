package com.example.outsourcing.common.filter;

import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain
    ) throws ServletException, IOException {

        String bearerJwt = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerJwt == null) {
            // 인증이 필요없으면 다음으로 넘김
            chain.doFilter(request, response);
            return;
        }

        try {
            String jwt = jwtUtil.substringToken(bearerJwt);

            // JWT 유효성 검사와 claims 추출
            if (handleInvalidClaims(jwt, response)) return;

            setAuthentication(jwt);

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            sendResponse(response, ExceptionCode.INVALID_JWT_SIGNATURE, e);
        } catch (ExpiredJwtException e) {
            sendResponse(response, ExceptionCode.EXPIRED_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            sendResponse(response, ExceptionCode.UNSUPPORTED_JWT_TOKENS, e);
        } catch (Exception e) {
            sendResponse(response, ExceptionCode.INVALID_JWT_TOKEN, e);
        }
    }

    private boolean handleInvalidClaims(String jwt, HttpServletResponse response) throws IOException {
        Claims claims = jwtUtil.extractClaims(jwt);
        if (claims == null) {
            log.error("잘못된 JWT 토큰입니다.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
            return true;
        }
        return false;
    }

    private void setAuthentication(String jwt) {
        Long id = jwtUtil.getUserIdFromToken(jwt);
        String username = jwtUtil.getUsernameFromToken(jwt);
        UserRole userRole = jwtUtil.getUserRoleFromToken(jwt);
        AuthUser authUser = new AuthUser(id, username, "", List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name())));

        // SecurityContext에 인증 토큰 저장
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities()));
    }

    private void sendResponse(HttpServletResponse response, ExceptionCode code, Exception e) throws IOException {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(code.getMessage(), code);
        String responseBody = new ObjectMapper().writeValueAsString(errorResponseDto);

        log.error("{}", code.getHttpStatus(), e);

        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

}