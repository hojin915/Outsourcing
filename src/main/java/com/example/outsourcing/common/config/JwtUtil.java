package com.example.outsourcing.common.config;

import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 3 * 60 * 60 * 1000L; // 30분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long userId, String username, UserRole userRole) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // JWT 반환
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new UnauthorizedException("Not Found Token");
    }

    // JWT 에서 페이로드 반환
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public UserRole getUserRoleFromToken(String token) {
        return UserRole.of(extractClaims(token).get("userRole", String.class));
    }

}