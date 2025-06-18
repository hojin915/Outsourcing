package com.example.outsourcing.common.aspect;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.service.ActivityLogService;
import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.common.entity.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActionLoggingAspect {

    private final ActivityLogService activityLogService;

    // Pointcut: *Service 패키지 내의 모든 메서드에 AOP 적용
    @Pointcut("execution(* com.example.outsourcing.*.*.*Service*.*(..)) " +
            "&& !execution(* com.example.outsourcing.activitylog.service.ActivityLogService.*(..))")
    public void ServiceMethods() {
    }

    @Around("ServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 시작시간
        long startTime = System.currentTimeMillis();

        //작업 시간
        LocalDateTime requestTime = LocalDateTime.now();
        // 현재 요청의 IP 주소를 가져오는 메서드
        String clientIpAddress = getClientIpAddress();
        // HTTP 메서드 (GET, POST 등)
        String requestMethod = getRequestMethod();
        // RequestUrl 메서드
        String requestUrl = getRequestUrl();
        // 현재 로그인된 사용자 ID 추출
        String loggedInUserId = getCurrentLoggedInUserId();

        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        // activityType
        String activityType = (methodName != null) ? methodName.toUpperCase() : null;

        Object result = null;
        Long targetId = null;

        try {
            result = joinPoint.proceed();
            targetId = extractTargetIdFromResponse(result);
        } finally {
            long endTime = System.currentTimeMillis();
            Long executionTimeMs = endTime - startTime;

            Long userIdToLog = null;
            if (methodName.equals("signup") || methodName.equals("login")) {
                userIdToLog = -1L; // 회원가입, 로그인 시 초기 사용자 ID는 -1 을로 반환
            } else if (loggedInUserId != null && !loggedInUserId.equals("anonymous")) {
                try {
                    userIdToLog = Long.valueOf(loggedInUserId);
                } catch (NumberFormatException e) {
                    log.error("Error converting loggedInUserId to Long: " + loggedInUserId, e);
                }
            }
            // 필요한 정보가 모두 있을 때만 활동 로그 저장
            if (userIdToLog != null && activityType != null) {
                ActivityLog activityLog = ActivityLog.builder()
                        .requestTime(requestTime)
                        .userId(userIdToLog)
                        .ipAddress(clientIpAddress)
                        .requestMethod(requestMethod)
                        .requestUrl(requestUrl)
                        .activityType(activityType)
                        .targetId(targetId)
                        .executionTimeMs(executionTimeMs)
                        .build();

                activityLogService.saveActivityLog(activityLog);

            } else {
                String missingInfo = "경고: 활동 로그 저장 실패 - 필수 정보 누락. " +
                        "로그인된 사용자 ID: " + loggedInUserId +
                        ", 활동 유형: " + activityType +
                        ", 대상 ID: " + (targetId != null ? targetId : "null") +
                        ", 실행 시간: " + executionTimeMs +
                        ", 메서드: " + joinPoint.getSignature().getName();
                log.warn(missingInfo);
            }
        }
        return result;
    }

    private Long extractTargetIdFromResponse(Object responseObject) {
        if (responseObject instanceof TargetIdentifiable) {
            Long extractedId = ((TargetIdentifiable) responseObject).getTargetId();
            log.debug("TargetIdentifiable 인터페이스를 통해 targetId 추출 결과 확인 : {}", extractedId);
            return extractedId;
        }
        return null;
    }


    private String getCurrentLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthUser) {
            AuthUser authUser = (AuthUser) authentication.getPrincipal();
            return String.valueOf(authUser.getId());
        }
        return null;
    }

    private String getClientIpAddress() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String xForwardedFor = request.getHeader("x-forwarded-for");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            String ip = xForwardedFor.split(",")[0].trim();
            return ip;
        }

        return request.getRemoteAddr();
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


