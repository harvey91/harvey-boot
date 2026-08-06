package com.harvey.quartz.schedule;

import com.harvey.common.utils.SpringUtils;
import com.harvey.common.utils.StringUtils;
import com.harvey.quartz.model.entity.Job;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 定时任务反射调用工具类
 * <p>
 * 支持调用目标字符串格式: beanName.methodName() 或 beanName.methodName('参数1', 2, true)
 *
 * @author Harvey
 * @since 2026-08-06
 */
public class JobInvokeUtil {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("-?\\d+\\.\\d+");

    private JobInvokeUtil() {
    }

    public static void invokeMethod(Job job) throws Exception {
        String invokeTarget = job.getInvokeTarget();
        if (StringUtils.isBlank(invokeTarget)) {
            throw new RuntimeException("定时任务调用目标字符串不能为空");
        }

        Object bean = SpringUtils.getBean(getBeanName(invokeTarget));
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);

        if (StringUtils.isNotEmpty(methodParams)) {
            invokeMethod(bean, methodName, methodParams);
        } else {
            invokeMethod(bean, methodName);
        }
    }

    private static void invokeMethod(Object bean, String methodName) throws Exception {
        Method method = AopUtils.getTargetClass(bean).getMethod(methodName);
        method.invoke(bean);
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws Exception {
        Class<?>[] methodParamTypes = new Class[methodParams.size()];
        Object[] methodParamValues = new Object[methodParams.size()];
        for (int i = 0; i < methodParams.size(); i++) {
            Object[] param = methodParams.get(i);
            methodParamTypes[i] = (Class<?>) param[0];
            methodParamValues[i] = param[1];
        }
        Method method = AopUtils.getTargetClass(bean).getMethod(methodName, methodParamTypes);
        method.invoke(bean, methodParamValues);
    }

    private static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    private static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    private static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
        // 逗号分隔,但引号内的逗号不拆分
        String[] params = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> methodParams = new ArrayList<>();
        for (String param : params) {
            Object[] methodParam = getMethodParam(StringUtils.trim(param));
            if (methodParam != null) {
                methodParams.add(methodParam);
            }
        }
        return methodParams;
    }

    private static Object[] getMethodParam(String param) {
        if ((param.startsWith("'") && param.endsWith("'")) || (param.startsWith("\"") && param.endsWith("\""))) {
            return new Object[]{String.class, param.substring(1, param.length() - 1)};
        } else if (INTEGER_PATTERN.matcher(param).matches()) {
            Long value = Long.valueOf(param);
            if (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE) {
                return new Object[]{Integer.class, value.intValue()};
            }
            return new Object[]{Long.class, value};
        } else if (DECIMAL_PATTERN.matcher(param).matches()) {
            return new Object[]{Double.class, Double.valueOf(param)};
        } else if ("true".equalsIgnoreCase(param) || "false".equalsIgnoreCase(param)) {
            return new Object[]{Boolean.class, Boolean.valueOf(param)};
        }
        return new Object[]{String.class, param};
    }
}
