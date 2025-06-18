package com.example.outsourcing.common.aspect;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import com.example.outsourcing.activitylog.service.ActivityLogService;
import com.example.outsourcing.common.entity.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActionLoggingAspect {

    private final ActivityLogService activityLogService;

    // Pointcut: *Service 패키지 내의 모든 메서드에 AOP 적용
    @Pointcut("execution(* com.example.outsourcing.*.*.*Service.*(..)) " +
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

//        Long targetId = extractTargetId(joinPoint.getArgs());

        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();

        String activityType = null;

        // activityType
        if (methodName != null) {
            activityType = methodName.toUpperCase();
        }

        Object result = null;

        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            Long executionTimeMs = endTime - startTime;

            Long userIdToLog = null;
            if (methodName.equals("signup") || methodName.equals("login")) {
                userIdToLog = -1L;
            } else if (loggedInUserId != null && !loggedInUserId.equals("anonymous")) {
                try {
                    userIdToLog = Long.valueOf(loggedInUserId);
                } catch (NumberFormatException e) {
                    log.error("Error converting loggedInUserId to Long: " + loggedInUserId, e);
                }
            }



            for(int i = 0; i < method.getParameters().length; i++){
                Parameter parameter = method.getParameters()[i];
                log.info(parameter.toString());
                if(parameter.isAnnotationPresent(PathVariable.class)){
                    String paramName = parameter.getName();
                    Object value = args[i];
                    log.info("param: {} | value: {}", paramName, value);
                }
            }

            // null 체크 및 로그 기록
            if (userIdToLog != null && activityType != null) {
                ActivityLog activityLog = ActivityLog.builder()
                        .requestTime(requestTime)
                        .userId(userIdToLog)
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


