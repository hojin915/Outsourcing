package com.example.outsourcing.common.aspect;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.service.ActivityLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserActionLoggingAspect {

    private final ActivityLogService activityLogService;

    // Pointcut: *Service 패키지 내의 모든 메서드에 AOP 적용
    @Pointcut("execution(* com.example.outsourcing.user.service.*Service.*(..))")
    public void UserServiceMethods() {
    }

    @Around("UserServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 시작시간
        long startTime = System.currentTimeMillis();

        //작업 시간
        LocalDateTime requestTime = LocalDateTime.now();
        // 현재 요청의 IP 주소를 가져오는 메서드 : X-Forwarded-For:unknown
        String clientIpAddress = getClientIpAddress();
        // HTTP 메서드 (GET, POST 등)
        String requestMethod = getRequestMethod();
        // RequestUrl
        String requestUrl = getRequestUrl();
        // 현재 로그인된 사용자 ID 추출
        String loggedInUserId = getCurrentLoggedInUserId();

//        Long targetId = getTargetIdFromToken();
        String activityType = null;
        Object result = null;


        try {
            result = joinPoint.proceed();
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();

            // 메서드에 따라 활동 유형 설정
            if (methodName.equals("signup")) {
                activityType = "USER_SIGNUP";
            } else if (methodName.equals("login")) {
                activityType = "USER_LOGIN";
            } else if (methodName.equals("profile")) {
                activityType = "USER_PROFILE";
            }

        } finally {
            long endTime = System.currentTimeMillis();
            Long executionTimeMs = endTime - startTime;

            // null 체크 및 로그 기록
            if (loggedInUserId != null && !loggedInUserId.equals("anonymous") &&
                    activityType != null // && targetId != null
            ) {
                ActivityLog activityLog = ActivityLog.builder()
                        .requestTime(requestTime)
                        .userId(Long.valueOf(loggedInUserId))
                        .ipAddress(clientIpAddress)
                        .requestMethod(requestMethod)
                        .requestUrl(requestUrl)
                        .activityType(activityType)
//                        .targetId(targetId)
                        .executionTimeMs(executionTimeMs)
                        .build();
                activityLogService.saveActivityLog(activityLog);
            } else {
                String missingInfo = "경고: 활동 로그 저장 실패 - 필수 정보 누락. " +
                        "LoggedInUserId: " + loggedInUserId +
                        ", ActivityType: " + activityType +
//                        ", TargetId: " + targetId +
                        ", ExecutionTimeMs: " + executionTimeMs +
                        ", Method: " + joinPoint.getSignature().getName();
                log.warn(missingInfo);
            }
        }
        return result;
    }

    private Long getTargetIdFromToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Authorization 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        token = token.substring(7);

        // Base64 디코딩으로 페이로드 추출
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalStateException("잘못된 JWT 형식입니다");
        }

        try {
            // JWT 디코딩
            String payload = new String(Base64.getDecoder().decode(tokenParts[1]));

            // JSON에서 targetId 추출
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(payload);

            System.out.println("Decoded JWT Payload: " + jsonNode.toString());
            if (jsonNode.has("targetId")) {
                return jsonNode.get("targetId").asLong();
            } else {
                log.warn("targetId가 JWT에 존재하지 않습니다.");
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private String getCurrentLoggedInUserId() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 토큰 추출
        // Authorization 헤더가 없거나 Bearer로 시작하지 않으면 null 반환
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        token = token.substring(7);

        // Base64 디코딩으로 페이로드 추출
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalStateException("잘못된 JWT 형식입니다");
        }

        try {
            // JWT 디코딩
            String payload = new String(Base64.getDecoder().decode(tokenParts[1]));

            // JSON에서 사용자명 추출
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(payload);

            // jsonNode 내용을 출력하여 확인
            System.out.println("Decoded JWT Payload: " + jsonNode.toString());

            // id가 존재하는지 확인 후 반환
            if (jsonNode.has("id")) {
                return jsonNode.get("id").asText();
            } else {
                log.warn("id가 JWT에 존재하지 않습니다.");
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getClientIpAddress() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            if (request != null) { // 추가: request가 null인지 확인
                // X-Forwarded-For 헤더
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    // 여러 IP가 있는 경우 첫 번째 IP 반환
                    String ip = xForwardedFor.split(",")[0].trim();
                    return "X-Forwarded-For:" + ip;
                }
            }
        }
        return "X-Forwarded-For:unknown";
    }

    private String getRequestMethod() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            request.getRemoteAddr();
            return request.getMethod();
        }
        return "unknown";
    }

    private String getRequestUrl() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            return request.getRequestURI();
        }
        return "unknown";
    }
}


