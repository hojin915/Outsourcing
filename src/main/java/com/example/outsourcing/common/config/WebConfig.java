package com.example.outsourcing.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. 허용할 도메인 설정
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",// React 개발 서버
                "http://localhost:3001",// 추가 개발 서버
                "http://localhost:3100",
                "https://myapp.com"// 운영 서버
        ));

        // 2. 허용할 HTTP 메서드 설정
        config.setAllowedMethods(List.of(
                "GET",// 조회 (데이터를 가져올 때)
                "POST",// 생성 (새로운 데이터를 만들 때)
                "PUT",// 전체 수정 (데이터를 완전히 바꿀 때)
                "DELETE",// 삭제 (데이터를 지울 때)
                "PATCH",// 부분 수정 (데이터의 일부만 바꿀 때)
                "OPTIONS"// 사전 요청 (브라우저가 자동으로 보내는 요청)
        ));

        // 3. 허용할 헤더 설정
        config.setAllowedHeaders(List.of("*"));// 모든 헤더 허용

        // 4. 응답에서 노출할 헤더 설정
        config.setExposedHeaders(List.of(
                "Authorization",// JWT 토큰 헤더
                "Content-Type"
        ));

        // 5. 인증 정보 허용 (쿠키, Authorization 헤더 등)
        config.setAllowCredentials(true);

        // 6. 모든 경로에 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// /** = 모든 경로

        return new CorsFilter(source);
    }
}