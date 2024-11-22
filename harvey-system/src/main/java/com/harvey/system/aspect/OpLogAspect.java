package com.harvey.system.aspect;

import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.LogOpService;
import com.harvey.system.utils.ServletUtils;
import com.harvey.system.utils.ip.AddressUtils;
import com.harvey.system.utils.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
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
        currentTimeThread.set(System.currentTimeMillis());
        Object result = jp.proceed();
        saveLog(jp, 1, "");;

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
        saveLog(jp, 2, e.toString());;
    }

    private void saveLog(JoinPoint jp, Integer result, String detail) {
        // 执行时长ms
        long duration = System.currentTimeMillis() - currentTimeThread.get();
        currentTimeThread.remove();
        // 类+方法
        String fullMethod = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        List<Object> argList = Arrays.asList(jp.getArgs());
        // 类方法 参数
        log.info("class_method = {}, args = {}", fullMethod, argList);

        // 获取类注解Tag的name值 + 方法注解Operation的summary值
        String module = jp.getTarget().getClass().getAnnotation(Tag.class).name() + "-"
                + ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(Operation.class).summary();
        HttpServletRequest request = ServletUtils.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        String requestUrl = request.getRequestURL().toString();
        LoginUserVO loginUserVO = SecurityUtil.getLoginUserVO();
        LogOpDto logOpDto = LogOpDto.builder()
                .userId(ObjectUtils.isEmpty(loginUserVO) ? 0L : loginUserVO.getUserId())
                .operator(ObjectUtils.isEmpty(loginUserVO) ? "" : loginUserVO.getUsername())
                .module(module)
                .requestUri(requestUrl)
                .method(fullMethod)
                .params(argList.toString())
                .ip(ip)
                .location(address)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .duration(duration)
                .result(result)
                .detail(detail)
                .build();
        logOpService.saveLogOp(logOpDto);
        if (duration < 1000) {
            log.info("afterController method: " + fullMethod + ", run time = " + duration + " ms");
        } else {
            // 标记慢请求
            log.info("afterControllerSlow method: " + fullMethod + ", run time = " + duration + " ms");
        }
    }
}
