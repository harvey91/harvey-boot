package com.harvey.system.aspect;

import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.LogOpService;
import com.harvey.system.utils.ip.AddressUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

/**
 * 操作日志 AOP切面
 *
 * @author Harvey
 * @date 2024-11-21 22:38
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OpLogAspect {
    private final LogOpService logOpService;

    ThreadLocal<Long> currentTimeThread = new ThreadLocal<>();

    @Pointcut("execution(public * com.harvey.*.controller..*.*(..))")
    public void logPointcut() {
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param jp joinPoint
     */
    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        Object result;
        currentTimeThread.set(System.currentTimeMillis());
        result = jp.proceed();
        long duration = System.currentTimeMillis() - currentTimeThread.get();
        currentTimeThread.remove();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String fullMethod = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        String remoteAddr = request.getRemoteAddr();
//        // 类方法
        log.info("class_method = {}", fullMethod);
//        // 参数
//        log.info("args = {}", Arrays.asList(joinPoint.getArgs()));
        LogOpDto logOpDto = LogOpDto.builder()
                .userId(SecurityUtil.getUserId())
                .requestUri(request.getRequestURL().toString())
                .method(fullMethod)
                .ip(remoteAddr)
                .location(AddressUtils.getRealAddressByIP(remoteAddr))
                .duration(duration)
                .build();
        logOpService.saveLogOp(logOpDto);
        if (duration < 1000) {
            log.info("afterController method: " + fullMethod + ", run time = " + duration + " ms");
        } else {
            log.info("afterControllerSlow method: " + fullMethod + ", run time = " + duration + " ms");
        }
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {


    }
}
