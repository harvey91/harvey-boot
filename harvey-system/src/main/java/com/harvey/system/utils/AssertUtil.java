package com.harvey.system.utils;

import com.harvey.common.enums.ErrorCodeEnum;
import com.harvey.system.exception.BusinessException;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 断言 工具类
 *
 * @author Harvey
 * @date 2024-12-17 10:06
 **/
public class AssertUtil {

    /**
     * 当表达式为true时，抛出异常
     *
     * @param expression
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, ErrorCodeEnum.FAIL);

    }

    /**
     * 当表达式为true时，抛出异常
     *
     * @param expression
     * @param msg
     */
    public static void isTrue(boolean expression, String msg) {
        isTrue(expression, ErrorCodeEnum.FAIL.getCode(), msg);
    }


    /**
     * 当表达式为true时，抛出异常
     *
     * @param expression
     * @param codeEnum
     */
    public static void isTrue(boolean expression, ErrorCodeEnum codeEnum) {
        isTrue(expression, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 当表达式为true时，抛出异常
     *
     * @param expression
     * @param codeEnum
     * @param msg
     */
    public static void isTrue(boolean expression, ErrorCodeEnum codeEnum, String msg) {
        isTrue(expression, codeEnum.getCode(), msg);
    }

    /**
     * 当表达式为true时，抛出异常
     *
     * @param expression
     * @param code
     * @param msg
     */
    public static void isTrue(boolean expression, int code, String msg) {
        if (expression) {
            throw new BusinessException(code, msg);
        }
    }

    /**
     * 当字符串为空或长度为0时，抛出异常
     *
     * @param text
     */
    public static void isBlank(String text) {
        isBlank(text, ErrorCodeEnum.FAIL);
    }

    /**
     * 当字符串为空或长度为0时，抛出异常
     *
     * @param text
     * @param codeEnum
     */
    public static void isBlank(String text, ErrorCodeEnum codeEnum) {
        isBlank(text, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 当字符串为空或长度为0时，抛出异常
     *
     * @param text
     * @param msg
     */
    public static void isBlank(String text, String msg) {
        isBlank(text, ErrorCodeEnum.FAIL.getCode(), msg);
    }

    /**
     * 当字符串为空或长度为0时，抛出异常
     *
     * @param text
     * @param code
     * @param msg
     */
    public static void isBlank(String text, int code, String msg) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(code, msg);
        }
    }

    /**
     * 当对象为空时，抛出异常
     *
     * @param obj
     */
    public static void isEmpty(Object obj) {
        isEmpty(obj, ErrorCodeEnum.FAIL);
    }

    /**
     * 当对象为空时，抛出异常
     *
     * @param obj
     * @param codeEnum
     */
    public static void isEmpty(Object obj, ErrorCodeEnum codeEnum) {
        isEmpty(obj, codeEnum.getCode(), codeEnum.getMsg());
    }


    /**
     * 当对象为空时，抛出异常
     *
     * @param obj
     * @param msg
     */
    public static void isEmpty(Object obj, String msg) {
        isEmpty(obj, ErrorCodeEnum.FAIL.getCode(), msg);
    }

    /**
     * 当对象为空时，抛出异常
     *
     * @param obj
     * @param code
     * @param msg
     */
    public static void isEmpty(Object obj, int code, String msg) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new BusinessException(code, msg);
        }
    }

    /**
     * 字符串转换成日期,出错抛异常
     *
     * @param timeString
     * @param formatString
     * @param codeEnum
     */
    public static void strIsDate(String timeString, String formatString, ErrorCodeEnum codeEnum) {
        if (StringUtils.isBlank(formatString)) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            Date date = format.parse(timeString);
        } catch (ParseException e) {
            throw new BusinessException(codeEnum);
        }
    }
}
