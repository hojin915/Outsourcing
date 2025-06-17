package com.example.outsourcing.common.filter;

import com.example.outsourcing.common.config.JwtUtil;
import com.example.outsourcing.common.entity.AuthUser;
import com.example.outsourcing.common.enums.UserRole;
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
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않는 JWT 토큰입니다.");
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

}