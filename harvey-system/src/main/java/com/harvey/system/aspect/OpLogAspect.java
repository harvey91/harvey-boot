package com.harvey.system.aspect;

import com.harvey.system.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

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
    private final LogService logService;

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
        currentTimeThread.set(System.currentTimeMillis());
        Object result = jp.proceed();
        saveLog(jp, 1, "");
        ;

        return result;
    }

    /**
     * 配置异常通知
     *
     * @param jp
     * @param e
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint jp, Throwable e) {
        saveLog(jp, 2, e.getMessage());
        ;
    }

    private void saveLog(JoinPoint jp, Integer result, String detail) {
        // 执行时长ms
        long duration = System.currentTimeMillis() - currentTimeThread.get();
        currentTimeThread.remove();

        // 类+方法
        String fullMethod = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        String params = Arrays.asList(jp.getArgs()).toString();
        Tag tag = jp.getTarget().getClass().getAnnotation(Tag.class);
        Operation operation = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(Operation.class);
        if (!ObjectUtils.isEmpty(tag) && !ObjectUtils.isEmpty(operation)) {
            // 获取类注解Tag的name值 + 方法注解Operation的summary值
            String module = tag.name() + "-" + operation.summary();
            // 模块，类方法，参数
            log.info("module = {}, class_method = {}, args = {}", module, fullMethod, params);
            logService.saveLogOp(module, fullMethod, params, duration, result, detail);
        } else {
            log.info("class_method = {}, args = {}", fullMethod, params);
        }
        if (duration > 1000) {
            // 标记慢请求
            log.info("afterControllerSlow method: " + fullMethod + ", run time = " + duration + " ms");
//        } else {
//            log.info("afterController method: " + fullMethod + ", run time = " + duration + " ms");
        }
    }
}
