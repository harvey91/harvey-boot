package com.harvey.system.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harvey.system.base.RespResult;
import com.harvey.system.enums.ErrorCodeEnum;
import com.harvey.system.exception.BadParameterException;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.utils.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Harvey
 * @date 2024-11-06 22:09
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public RespResult<String> handleException(Throwable e) {
        log.error(e.getMessage(), e);
        return RespResult.fail(e.getMessage());
    }

    /**
     * 参数缺少或错误
     * @param e
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class, BindException.class})
    public RespResult<?> paramExceptionHandler(Exception e) {
        if (e instanceof BindException) {
            if (e instanceof MethodArgumentNotValidException) {
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
                List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return RespResult.error(String.join(",", msgList));
            }

            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            List<String> error = fieldErrors.stream().map(field -> field.getField() + ":" + field.getRejectedValue()).toList();
            String errorMsg = ErrorCodeEnum.PARAM_ERROR.getMsg() + ":" + error;
            return RespResult.error(errorMsg);
        }
        return RespResult.error();
    }

    /**
     * RequestParam参数的校验
     *
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public <T> RespResult<T> processException(ConstraintViolationException e) {
        log.error("ConstraintViolationException:{}", e.getMessage());
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("；"));
        return RespResult.error(msg);
    }

    /**
     * RequestBody参数的校验
     *
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> RespResult<T> processException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException:{}", e.getMessage());
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
        return RespResult.error(msg);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public <T> RespResult<T> processException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return RespResult.error();
    }

    /**
     * MissingServletRequestParameterException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public <T> RespResult<T> processException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return RespResult.error();
    }

    /**
     * MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public <T> RespResult<T> processException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return RespResult.error("类型错误");
    }

    /**
     * ServletException
     */
    @ExceptionHandler(ServletException.class)
    public <T> RespResult<T> processException(ServletException e) {
        log.error(e.getMessage(), e);
        return RespResult.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public <T> RespResult<T> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因：{}", e.getMessage(), e);
        return RespResult.error(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public <T> RespResult<T> handleJsonProcessingException(JsonProcessingException e) {
        log.error("Json转换异常，异常原因：{}", e.getMessage(), e);
        return RespResult.error(e.getMessage());
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> RespResult<T> processException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        String errorMessage = "请求体不可为空";
        Throwable cause = e.getCause();
        if (cause != null) {
            errorMessage = convertMessage(cause);
        }
        return RespResult.error(errorMessage);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public <T> RespResult<T> handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error(e.getMessage(), e);
        String errorMsg = e.getMessage();
        if (StringUtils.isNotBlank(errorMsg) && errorMsg.contains("denied to user")) {
            return RespResult.error();
        } else {
            return RespResult.error(e.getMessage());
        }
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public <T> RespResult<T> processSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        log.error(e.getMessage(), e);
        return RespResult.error(e.getMessage());
    }


    @ExceptionHandler(BusinessException.class)
    public RespResult<String> handleServiceException(BusinessException e) {
        log.error(e.getMessage(), e);
        return RespResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadParameterException.class)
    public RespResult<String> handleBadParameterException(BusinessException e) {
        log.error(e.getMessage(), e);
        return RespResult.fail(ErrorCodeEnum.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 传参类型错误时，用于消息转换
     *
     * @param throwable 异常
     * @return 错误信息
     */
    private String convertMessage(Throwable throwable) {
        String error = throwable.toString();
        String regulation = "\\[\"(.*?)\"]+";
        Pattern pattern = Pattern.compile(regulation);
        Matcher matcher = pattern.matcher(error);
        String group = "";
        if (matcher.find()) {
            String matchString = matcher.group();
            matchString = matchString.replace("[", "").replace("]", "");
            matchString = "%s字段类型错误".formatted(matchString.replaceAll("\"", ""));
            group += matchString;
        }
        return group;
    }

}
